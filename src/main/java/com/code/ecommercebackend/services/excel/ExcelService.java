package com.code.ecommercebackend.services.excel;

import com.code.ecommercebackend.exceptions.DataNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface ExcelService {
    ByteArrayInputStream generateExcelImportProduct(String categoryId) throws IOException, DataNotFoundException;
    void exportExcel();
    void importProductExcel(MultipartFile file) throws IOException, DataNotFoundException;
}
