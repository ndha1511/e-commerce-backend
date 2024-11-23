package com.code.ecommercebackend.services.excel;

import com.code.ecommercebackend.dtos.request.product.ProductExcel;
import com.code.ecommercebackend.dtos.request.product.VariantExcel;
import com.code.ecommercebackend.dtos.response.category.CategoryResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.mappers.product.ProductMapper;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {

    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final InventoryRepository inventoryRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public ByteArrayInputStream generateExcelImportProduct(CategoryResponse category) throws IOException, DataNotFoundException {
        List<String> categoryStr = new ArrayList<>();
        categoryToArray(category, categoryStr, category.getCategoryName());
        List<String> brandStr = brandRepository.findAll().stream()
                .map(Brand::getBrandName).toList();


        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Thêm sản phẩm hàng loạt");

        // Tạo Data Validation
        if (categoryStr.isEmpty()) {
            throw new IllegalArgumentException("Category list is empty.");
        }

        Row row = sheet.createRow(0);
        row.setHeightInPoints(60);
        Row row1 = sheet.createRow(1);
        row1.setHeightInPoints(60);
        Row row2 = sheet.createRow(2);
        row2.setHeightInPoints(60);


        String[] headers = {
                "Ngành hàng", "Tên sản phẩm", "Mô tả sản phẩm", "Mã sản phẩm",
                "Tên nhóm phân loại hàng 1", "Tên phân loại hàng cho nhóm phân loai hàng 1", "Hình ảnh mỗi phân loại",
                "Tên nhóm phân loại hàng 2", "Tên phân loại hàng cho nhóm phân loai hàng 2",
                "Giá nhập", "Giá bán", "Kho hàng", "SKU phân loại",
                "Thương hiệu", "Ảnh bìa", "Hình ảnh 1", "Hình ảnh 2", "Hình ảnh 3",
                "Hình ảnh 4", "Hình ảnh 5", "Hình ảnh 6", "Hình ảnh 7", "Hình ảnh 8", "Chất liệu", "Xuất xứ", "Cân nặng"
        };

        String[] rowValue1 = {
                "Bắt buộc", "Bắt buộc", "Bắt buộc", "Bắt buộc",
                "Bắt buộc", "Bắt buộc", "Tùy chọn", "Tùy chọn", "Tùy chọn", "Bắt buộc",
                "Bắt buộc", "Bắt buộc", "Tùy chọn", "Bắt buộc", "Bắt buộc", "Bắt buộc",
                "Bắt buộc", "Tùy chọn", "Tùy chọn", "Tùy chọn", "Tùy chọn",
                "Tùy chọn", "Tùy chọn", "Bắt buộc", "Bắt buộc", "Bắt buộc"
        };

        String[] rowValue2 = {
                "Chọn từ hộp thả xuống, vui lòng chọn đúng Tên Ngành Hàng",
                "Vui lòng nhập từ 10 đến 120 ký tự cho tên sản phẩm",
                "Vui lòng nhập từ 100 đến 3000 ký tự cho mô tả sản phẩm",
                "Vui lòng nhập 1-100 ký tự cho mã sản phẩm",
                "Nhập các ký tự từ 1 đến 14 cho tên phân loại",
                "Nhập các ký tự từ 1 đến 20 cho tên tùy chọn",
                "Nhập URL của hình ảnh sản phẩm",
                "Nhập các ký tự từ 1 đến 14 cho tên phân loại",
                "Nhập các ký tự từ 1 đến 20 cho tên tùy chọn",
                "Vui lòng nhập 10000 đến 100000000",
                "Vui lòng nhập 10000 đến 100000000",
                "Vui lòng nhập từ 0 đến 999,999 cho kho sản phẩm",
                "Vui lòng nhập ít hơn 100 ký tự để cho sku",
                "Chọn từ hộp thả xuống, vui lòng chọn đúng Tên Thương Hiệu",
                "Nhập URL của hình ảnh sản phẩm",
                "Nhập URL của hình ảnh sản phẩm",
                "Nhập URL của hình ảnh sản phẩm",
                "Nhập URL của hình ảnh sản phẩm",
                "Nhập URL của hình ảnh sản phẩm",
                "Nhập URL của hình ảnh sản phẩm",
                "Nhập URL của hình ảnh sản phẩm",
                "Nhập URL của hình ảnh sản phẩm",
                "Nhập URL của hình ảnh sản phẩm",
                "Vui lòng nhập từ 10 đến 120 ký tự cho chất liệu",
                "Vui lòng nhập từ 10 đến 120 ký tự cho xuất xứ",
                "Vui lòng nhập từ 1 đến 100000 cho cân nặng"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(ExcelStyle.headerStyle(wb));
        }

        for (int i = 0; i < rowValue1.length; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellValue(rowValue1[i]);
            cell.setCellStyle(ExcelStyle.cellStyle(wb));
        }

        for (int i = 0; i < rowValue2.length; i++) {
            Cell cell = row2.createCell(i);
            cell.setCellValue(rowValue2[i]);
            cell.setCellStyle(ExcelStyle.cellStyle(wb));
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            int width = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, width + 2000);

        }

        CellRangeAddressList addressList = new CellRangeAddressList(3, 1000, 0, 0);
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(categoryStr.toArray(new String[0]));
        DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setSuppressDropDownArrow(true);
        dataValidation.setShowErrorBox(true);
        sheet.addValidationData(dataValidation);

        CellRangeAddressList addressList1 = new CellRangeAddressList(3, 1000, 13, 13);
        DataValidationHelper validationHelper1 = sheet.getDataValidationHelper();
        DataValidationConstraint constraint1 = validationHelper1.createExplicitListConstraint(brandStr.toArray(new String[0]));
        DataValidation dataValidation1 = validationHelper1.createValidation(constraint1, addressList1);
        dataValidation1.setSuppressDropDownArrow(true);
        dataValidation1.setShowErrorBox(true);
        sheet.addValidationData(dataValidation1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        return new ByteArrayInputStream(out.toByteArray());


    }

    @Override
    public void exportExcel() {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importProductExcel(MultipartFile file) throws IOException, DataNotFoundException {
        Workbook workbook = getWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        List<ProductExcel> productExcelList = new ArrayList<>();
        for (int i = 3; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            ProductExcel productExcel = new ProductExcel();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) continue;
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        setProductValue(productExcelList, productExcel, j, i, cell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        setProductValue(productExcelList, productExcel, j, i, cell.getNumericCellValue());
                        break;
                    default:
                        break;
                }
            }
            if (!productExcelList.contains(productExcel)) {
                productExcelList.add(productExcel);
            } else {
                int index = productExcelList.indexOf(productExcel);
                productExcelList.set(index, productExcel);
            }
        }
        Set<String> inventoryIds = new HashSet<>();
        double totalPrice = 0;
        int totalQuantity = 0;
        for (ProductExcel productExcel : productExcelList) {
            Product product = productMapper.toProductFromProductExcel(productExcel);
            product.createUrlPath();
            product.normalizerName();
            product.setNumId(productRepository.count() + 1);
            productRepository.save(product);
            List<ProductAttribute> attributes = productExcel.getAttributes()
                    .stream().peek(attr -> attr.setProductId(product.getId())).toList();
            productAttributeRepository.saveAll(attributes);
            for (VariantExcel variantExcel : productExcel.getVariants()) {
                Variant variant = new Variant();
                variant.setProduct(product);
                variant.setPrice(variantExcel.getPrice());
                variant.setSku(variantExcel.getSku());
                variant.setAttributeValue1(variantExcel.getAttributeValue1());
                variant.setAttributeValue2(variantExcel.getAttributeValue2());
                variantRepository.save(variant);
                InventoryDetail inventory = new InventoryDetail();
                inventory.setProductId(product.getId());
                inventory.setVariantId(variant.getId());
                inventory.setImportDate(LocalDateTime.now());
                inventory.setImportQuantity(variantExcel.getQuantity());
                inventory.setImportPrice(variantExcel.getImportPrice());
                inventoryRepository.save(inventory);
                inventoryIds.add(inventory.getId());
                totalPrice += variantExcel.getImportPrice() * variantExcel.getQuantity();
                totalQuantity += variantExcel.getQuantity();
            }
        }

        // purchase order
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderDate(LocalDateTime.now());
        purchaseOrder.setInventories(inventoryIds);
        purchaseOrder.setTotalPrice(totalPrice);
        purchaseOrder.setImportStaffName("admin");
        purchaseOrder.setTotalQuantity(totalQuantity);
        purchaseOrderRepository.save(purchaseOrder);


    }

    private void categoryToArray(CategoryResponse categoryResponse, List<String> categoryList, String root) {
        if ((categoryResponse.getChildren() != null) && (!categoryResponse.getChildren().isEmpty())) {
            List<CategoryResponse> children = categoryResponse.getChildren();
            for (CategoryResponse child : children) {
                String newRoot = root + " > " + child.getCategoryName();
                categoryToArray(child, categoryList, newRoot);
            }
        } else {
            categoryList.add(root);

        }

    }

    private void setProductValue(
            List<ProductExcel> productExcelList,
            ProductExcel productExcel,
            int cellIdx,
            int rowIdx,
            Object value) throws DataNotFoundException {
        List<ProductAttribute> attributes;
        List<AttributeValue> attributeValues;
        List<VariantExcel> variants;
        AttributeValue attributeValue;
        List<String> images;
        VariantExcel variant;
        int rowMinus = 0;
        if(!productExcelList.isEmpty() && productExcelList.size() >= 2) {
            for (int i = 0; i < productExcelList.size() - 1; i++) {
                ProductExcel productExcel1 = productExcelList.get(i);
                if(productExcel1.getVariants() != null) {
                    rowMinus += productExcel1.getVariants().size();
                }
            }
        }
        switch (cellIdx) {
            // category
            case 0:
                if (value == null) throw new DataNotFoundException("category must be not null");
                String categoryStr = (String) value;
                String[] categoryNames = categoryStr.split(" > ");
                Set<String> categoryIdList = new LinkedHashSet<>();
                String parentId = null;
                for (String categoryName : categoryNames) {
                    Category category;
                    if (parentId == null) {
                        category = categoryRepository.findByCategoryName(categoryName)
                                .orElseThrow(() -> new DataNotFoundException("category not found"));
                    } else {
                        category = categoryRepository.findByCategoryNameAndParentId(categoryName, parentId)
                                .orElseThrow(() -> new DataNotFoundException("category not found"));
                    }

                    parentId = category.getId();
                    categoryIdList.add(category.getId());
                }
                productExcel.setCategories(categoryIdList);
                break;
            // product name
            case 1:
                if (value == null) throw new DataNotFoundException("product name must be not null");
                String productName = (String) value;
                productExcel.setProductName(productName);
                break;
            // product description
            case 2:
                if (value == null) throw new DataNotFoundException("description must be not null");
                String description = (String) value;
                productExcel.setDescription(description);
                break;

            // id
            case 3:
                if (value == null) throw new DataNotFoundException("id must be not null");
                String id = (String) value;
                productExcel.setExcelId(id);
                if (productExcelList.contains(productExcel)) {
                    productExcel.init(productExcelList.get(productExcelList.indexOf(productExcel)));
                }
                break;

            // attribute name 1
            case 4:
                if (value == null) throw new DataNotFoundException("attribute name 1 must be not null");
                String attributeName = (String) value;
                attributes = productExcel.getAttributes();
                if (attributes == null || attributes.isEmpty()) {
                    attributes = new ArrayList<>();
                    ProductAttribute productAttribute = new ProductAttribute();
                    productAttribute.setAttributeName(attributeName);
                    attributes.add(productAttribute);
                    productExcel.setAttributes(attributes);
                }
                break;

            // attribute 1 value
            case 5:
                if (value == null) throw new DataNotFoundException("attribute name 1 value must be not null");
                String attributeValueStr = (String) value;
                attributes = productExcel.getAttributes();
                attributeValues = attributes.get(0).getAttributeValues();

                if (attributeValues == null || attributeValues.isEmpty()) {
                    attributeValues = new ArrayList<>();
                }
                attributeValue = new AttributeValue();
                attributeValue.setValue(attributeValueStr);
                if (!attributeValues.contains(attributeValue)) {
                    attributeValues.add(attributeValue);
                }
                attributes.get(0).setAttributeValues(attributeValues);
                variants = productExcel.getVariants();
                if (variants == null) {
                    variants = new ArrayList<>();
                }
                variant = new VariantExcel();
                variant.setAttributeValue1(attributeValueStr);
                variants.add(variant);
                productExcel.setVariants(variants);
                break;

            // attribute 1 image
            case 6:
                if (value != null) {
                    attributes = productExcel.getAttributes();
                    attributeValues = attributes.get(0).getAttributeValues();
                    Optional<AttributeValue> attrValue = attributeValues.stream()
                            .filter(v -> (v.getImage() != null && v.getImage().equals(value)))
                            .findFirst();
                    if (attrValue.isEmpty()) {
                        attributeValues.get(attributeValues.size() - 1).setImage((String) value);
                    }
                    attributes.get(0).setAttributeValues(attributeValues);
                }
                break;

            // attribute name 2
            case 7:
                if (value != null) {
                    attributes = productExcel.getAttributes();
                    if (attributes.size() < 2) {
                        ProductAttribute productAttribute = new ProductAttribute();
                        productAttribute.setAttributeName((String) value);
                        attributes.add(productAttribute);
                        productExcel.setAttributes(attributes);

                    }
                }
                break;

            // attribute 2 value
            case 8:
                if (value != null) {
                    attributes = productExcel.getAttributes();
                    if (attributes.size() == 2) {
                        attributeValues = attributes.get(1).getAttributeValues();
                        if (attributeValues == null) {
                            attributeValues = new ArrayList<>();
                        }
                        attributeValue = new AttributeValue();
                        attributeValue.setValue((String) value);
                        if (!attributeValues.contains(attributeValue)) {
                            attributeValues.add(attributeValue);
                        }
                        attributes.get(1).setAttributeValues(attributeValues);
                        productExcel.setAttributes(attributes);
                    }
                    variants = productExcel.getVariants();
                    if(variants.size() == 1) {
                        variants.get(0).setAttributeValue2((String) value);
                    } else {
                        variants.get(rowIdx - 3 - rowMinus).setAttributeValue2((String) value);
                    }

                    productExcel.setVariants(variants);
                }


                break;

            // import price
            case 9:
                if (value == null) throw new DataNotFoundException("import price must be not null");

                double importPrice = (double) value;
                variants = productExcel.getVariants();
                if(variants.size() == 1) {
                    variants.get(0).setImportPrice(importPrice);
                } else {
                    variants.get(rowIdx - 3 - rowMinus).setImportPrice(importPrice);
                }
                productExcel.setVariants(variants);
                break;

            // price
            case 10:
                if (value == null) throw new DataNotFoundException("price must be not null");

                double price = (double) value;
                variants = productExcel.getVariants();
                if(variants.size() == 1) {
                    variants.get(0).setPrice(price);
                } else {
                    variants.get(rowIdx - 3 - rowMinus).setPrice(price);
                }

                productExcel.setVariants(variants);
                if (productExcel.getRegularPrice() == 0) {
                    productExcel.setRegularPrice(price);
                } else {
                    productExcel.setRegularPrice(Math.min(price, productExcel.getRegularPrice()));
                }
                break;

            // quantity
            case 11:
                if (value == null) throw new DataNotFoundException("quantity must be not null");
                int quantity = (int) ((double) value);
                variants = productExcel.getVariants();
                if(variants.size() == 1) {
                    variants.get(0).setQuantity(quantity);
                } else {
                    variants.get(rowIdx - 3 - rowMinus).setQuantity(quantity);
                }
                productExcel.setVariants(variants);
                productExcel.setTotalQuantity(productExcel.getTotalQuantity() + quantity);
                break;

            // sku
            case 12:
                if (value != null) {
                    variants = productExcel.getVariants();
                    if(variants.size() == 1) {
                        variants.get(0).setQuantity(productExcel.getTotalQuantity());
                    } else {
                        variants.get(rowIdx - 3 - rowMinus).setQuantity(productExcel.getTotalQuantity());
                    }

                    productExcel.setVariants(variants);
                }
                break;

            // brand
            case 13:
                if (value == null) throw new DataNotFoundException("brand must be not null");
                Brand brand = brandRepository.findByBrandName((String) value)
                        .orElseThrow(() -> new DataNotFoundException("brand not found"));
                productExcel.setBrandId(brand.getId());
                break;

            // thumbnail
            case 14:
                if (value == null) throw new DataNotFoundException("thumbnail must be not null");
                productExcel.setThumbnail((String) value);
                break;

            // image required
            case 15:
            case 16:
                if (value == null) throw new DataNotFoundException("image must be not null");
                images = productExcel.getImages();
                if (images == null) {
                    images = new ArrayList<>();
                }
                if (!images.contains((String) value))
                    images.add((String) value);
                productExcel.setImages(images);
                break;

            // image option
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
                if (value != null) {
                    images = productExcel.getImages();
                    if (images == null) {
                        images = new ArrayList<>();
                    }
                    if (!images.contains((String) value))
                        images.add((String) value);
                    productExcel.setImages(images);
                }
                break;

            // tag
            case 23:
            case 24:
                if (value == null) throw new DataNotFoundException("image must be not null");
                List<Tag> tags = productExcel.getTags();
                if (tags == null) {
                    tags = new ArrayList<>();
                }
                Tag tag = new Tag();
                tag.setTagName(cellIdx == 23 ? "Chất liệu" : "Xuất xứ");
                tag.setTagValue((String) value);
                if (!tags.contains(tag)) {
                    tags.add(tag);
                }
                productExcel.setTags(tags);
                break;

            // weight
            case 25:
                if (value == null) throw new DataNotFoundException("weight must be not null");
                productExcel.setWeight((int) ((double) value));
                break;
            default:
                break;
        }
    }

    private Workbook getWorkbook(MultipartFile file) throws IOException {
        Workbook workbook;
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        if (fileName.endsWith(".xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        } else if (fileName.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else {
            throw new IllegalArgumentException("File do not end with .xls or .xlsx");
        }
        return workbook;
    }

}
