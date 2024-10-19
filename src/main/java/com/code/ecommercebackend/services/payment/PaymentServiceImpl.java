package com.code.ecommercebackend.services.payment;

import com.code.ecommercebackend.configurations.VnpayConfig;
import com.code.ecommercebackend.dtos.request.payment.OrderItem;
import com.code.ecommercebackend.dtos.request.payment.OrderRequest;
import com.code.ecommercebackend.dtos.response.ResponseGHTK;
import com.code.ecommercebackend.dtos.response.payment.Fee;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.models.enums.DiscountType;
import com.code.ecommercebackend.models.enums.OrderStatus;
import com.code.ecommercebackend.models.enums.PaymentMethod;
import com.code.ecommercebackend.models.enums.PaymentStatus;
import com.code.ecommercebackend.repositories.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final RestTemplate restTemplate;
    private final CartRepository cartRepository;
    private final VariantRepository variantRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final OrderRepository orderRepository;
    @Value("${api.ghtk.url}")
    private String ghtkUrl;
    @Value("${api.ghtk.token}")
    private String ghtkToken;
    @Value("${front-end-url}")
    private String vnp_ReturnUrl;

    @Override
    public Fee calcFee(String pickProvince,
                       String pickDistrict,
                       String province,
                       String district,
                       int weight, int value) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghtkToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = ghtkUrl + "/fee?pick_province=" + pickProvince + "&pick_district=" + pickDistrict + "&province=" + province +
               "&district=" + district + "&weight=" + weight + "&value=" + value;
        ResponseEntity<ResponseGHTK> response = restTemplate.exchange(url,
                HttpMethod.GET, entity,
                ResponseGHTK.class);
        return Objects.requireNonNull(response.getBody()).getFee();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order order(OrderRequest orderRequest) throws DataNotFoundException {
        if(orderRequest.getOrderFrom().equals("cart")) {
            Cart cart = cartRepository.findByUserId(orderRequest.getUserId())
                    .orElseThrow(() -> new DataNotFoundException("cart not found"));
            List<ProductCart> pCarts = cart.getProductCarts();
            List<ProductOrder> productOrders = new ArrayList<>();
            double totalAmount = 0;
            for (ProductCart pCart : pCarts) {
                double amount;
                double discount = 0;
                for(OrderItem item: orderRequest.getOrderItems()) {
                    if(item.getVariantId().equals(pCart.getVariantId())) {
                        Variant variant = variantRepository.findById(pCart.getVariantId())
                                .orElseThrow(() -> new DataNotFoundException("variant not found"));
                        checkInventory(variant.getProduct().getId(), variant.getId(), pCart.getQuantity());
                        Optional<Promotion> optPromotion = promotionRepository.findFirstByCurrentDateAndProductId(variant.getProduct().getId(),
                                Sort.by(Sort.Direction.DESC, "startDate"));
                        if(optPromotion.isPresent()) {
                            Promotion promotion = optPromotion.get();

                            if(promotion.getDiscountType().equals(DiscountType.PERCENT)) {
                                discount = variant.getPrice() * promotion.getDiscountValue();
                            } else {
                                discount = promotion.getDiscountValue();
                            }
                           
                        }
                        amount = (variant.getPrice() - discount) * pCart.getQuantity();
                        totalAmount += amount;
                        ProductOrder productOrder = ProductOrder.builder()
                                .price(variant.getPrice() - discount)
                                .image(variant.getProduct().getThumbnail())
                                .productId(variant.getProduct().getId())
                                .productName(variant.getProduct().getProductName())
                                .attributes(Set.of(variant.getAttributeValue1(),
                                        variant.getAttributeValue2()))
                                .quantity(pCart.getQuantity())
                                .amount(amount)
                                .build();
                        productOrders.add(productOrder);
                        pCarts.remove(pCart);
                        if(pCarts.isEmpty()) {
                            break;
                        }
                    }
                }
                if(pCarts.isEmpty()) {
                    break;
                }
            }
            cart.setProductCarts(pCarts);
            cartRepository.save(cart);
            Order order = getOrder(orderRequest, totalAmount, productOrders);
            return orderRepository.save(order);
        }
        double totalAmount = 0;
        List<ProductOrder> productOrders = new ArrayList<>();
        for(OrderItem item: orderRequest.getOrderItems()) {
            double amount;
            double discount = 0;
            Variant variant = variantRepository.findById(item.getVariantId())
                    .orElseThrow(() -> new DataNotFoundException("variant not found"));
            checkInventory(variant.getProduct().getId(), variant.getId(), item.getQuantity());
            Optional<Promotion> optPromotion = promotionRepository.findFirstByCurrentDateAndProductId(variant.getProduct().getId(),
                    Sort.by(Sort.Direction.DESC, "startDate"));
            if(optPromotion.isPresent()) {
                Promotion promotion = optPromotion.get();

                if(promotion.getDiscountType().equals(DiscountType.PERCENT)) {
                    discount = variant.getPrice() * promotion.getDiscountValue();
                } else {
                    discount = promotion.getDiscountValue();
                }
                
            }
            amount = (variant.getPrice() - discount) * item.getQuantity();
            totalAmount += amount;
            ProductOrder productOrder = ProductOrder.builder()
                    .price(variant.getPrice() - discount)
                    .image(variant.getProduct().getThumbnail())
                    .productId(variant.getProduct().getId())
                    .productName(variant.getProduct().getProductName())
                    .attributes(Set.of(variant.getAttributeValue1(),
                            variant.getAttributeValue2()))
                    .quantity(item.getQuantity())
                    .amount(amount)
                    .build();
            productOrders.add(productOrder);

        }
        Order order = getOrder(orderRequest, totalAmount, productOrders);

        return orderRepository.save(order);
    }

    @Override
    public String payment(HttpServletRequest req)  {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = Integer.parseInt(req.getParameter("amount"))* 100L;

        String orderId = req.getParameter("orderId");

        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
        String vnp_IpAddr = VnpayConfig.getIpAddress(req);
        String bankCode = req.getParameter("bankCode");

        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }

        vnp_Params.put("vnp_Locale", "vn");
        String vnp_ReturnUrl_rs = vnp_ReturnUrl;
        vnp_ReturnUrl_rs += "/payment/result";
        String APP_RETURN_URL = "http://localhost:8080/api/v1/payment/success";
        vnp_Params.put("vnp_ReturnUrl", APP_RETURN_URL + "?orderId=" + orderId +
                "&return_url=" + vnp_ReturnUrl_rs);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return VnpayConfig.vnp_PayUrl  + "?" + queryUrl;
    }

    @Override
    public boolean paymentSuccess(HttpServletRequest req) {
        String vnpResponseCode = req.getParameter("vnp_ResponseCode");
        if(vnpResponseCode == null || !vnpResponseCode.equals("00")) {
            return false;
        }
        String orderId = req.getParameter("orderId");
        if(orderId == null) return false;
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()) {
            Order orderInfo = order.get();
            orderInfo.setPaymentStatus(PaymentStatus.PAID);
            orderInfo.setPaymentDate(LocalDateTime.now());
            orderRepository.save(orderInfo);
            return true;
        }
        return false;
    }

    private Order getOrder(OrderRequest orderRequest, double totalAmount, List<ProductOrder> productOrders) {
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        if(orderRequest.getPaymentMethod().equals(PaymentMethod.ATM)) {
            order.setPaymentStatus(PaymentStatus.UNPAID);
        }
        order.setProductOrders(productOrders);
        order.setShippingAddress(orderRequest.getUserAddress());
        order.setNote(orderRequest.getNote());
        order.setShippingAmount(orderRequest.getDeliveryFee());
        order.calcFinalAmount();
        return order;
    }

    private void checkInventory(String productId, String variantId, int quantity) throws DataNotFoundException {
        List<Inventory> inventory = inventoryRepository.findByProductIdAndVariantIdOrderByImportDate(productId, variantId);
        boolean flag = false;
        if(inventory.isEmpty()) {
            throw new DataNotFoundException("out of in stock");
        } else {
            for (Inventory inventoryItem : inventory) {
                if(inventoryItem.getImportQuantity() - inventoryItem.getSaleQuantity() >= quantity) {
                    flag = true;
                    inventoryItem.setSaleQuantity(inventoryItem.getSaleQuantity() + quantity);
                    inventoryRepository.save(inventoryItem);
                    Product product = productRepository.findById(inventoryItem.getProductId())
                            .orElseThrow(() -> new DataNotFoundException("product not found"));
                    product.setTotalQuantity(product.getTotalQuantity() - quantity);
                    product.setBuyQuantity(product.getBuyQuantity() + quantity);
                    productRepository.save(product);
                    break;
                }
            }
            if(!flag) {
                throw new DataNotFoundException("out of in stock");
            }
        }

    }


}
