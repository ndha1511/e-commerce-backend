package com.code.ecommercebackend.services.payment;

import com.code.ecommercebackend.configurations.VnpayConfig;
import com.code.ecommercebackend.dtos.request.payment.KafkaMessageOrder;
import com.code.ecommercebackend.dtos.request.payment.OrderItem;
import com.code.ecommercebackend.dtos.request.payment.OrderRequest;
import com.code.ecommercebackend.dtos.response.ResponseGHTK;
import com.code.ecommercebackend.dtos.response.payment.Fee;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.models.enums.*;
import com.code.ecommercebackend.repositories.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final OrderRepository orderRepository;
    private final VoucherRepository voucherRepository;
    private final VoucherUsageRepository voucherUsageRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final VariantRepository variantRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final PromotionRepository promotionRepository;
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
        double totalAmount = 0;
        double amount;
        double discount = 0;
        List<ProductOrder> productOrders = new ArrayList<>();


        for (OrderItem item : orderRequest.getOrderItems()) {
            Variant variant = variantRepository.findById(item.getVariantId())
                    .orElseThrow(() -> new DataNotFoundException("variant not found"));
            ProductAttribute productAttribute = productAttributeRepository
                    .findByProductId(variant.getProduct().getId()).get(0);
            List<AttributeValue> attrValues = productAttribute.getAttributeValues();
            AttributeValue attrValue = attrValues.stream().filter(value -> value.getValue()
                    .equals(variant.getAttributeValue1())).findFirst().orElse(null);
            String image = attrValue != null ? attrValue.getImage() : null;
            Optional<Promotion> optPromotion = promotionRepository.findFirstByCurrentDateAndProductId(variant.getProduct().getId(),
                    Sort.by(Sort.Direction.DESC, "startDate"));
            if (optPromotion.isPresent()) {
                Promotion promotion = optPromotion.get();

                if (promotion.getDiscountType().equals(DiscountType.PERCENT)) {
                    discount = variant.getPrice() * promotion.getDiscountValue();
                } else {
                    discount = promotion.getDiscountValue();
                }

            }
            amount = (variant.getPrice() - discount) * item.getQuantity();
            totalAmount += amount;
            ProductOrder productOrder = ProductOrder.builder()
                    .price(variant.getPrice() - discount)
                    .image((image != null) ? image : variant.getProduct().getThumbnail())
                    .productId(variant.getProduct().getId())
                    .productName(variant.getProduct().getProductName())
                    .attributes(List.of(variant.getAttributeValue1(),
                            variant.getAttributeValue2()))
                    .quantity(item.getQuantity())
                    .amount(amount)
                    .build();
            productOrders.add(productOrder);


        }
        Order order = getOrder(orderRequest, totalAmount, productOrders);
        orderRepository.save(order);
        kafkaTemplate.send("order-topic", new KafkaMessageOrder(
                order, orderRequest
        ));
        kafkaTemplate.send("inventory-topic", new KafkaMessageOrder(
                order, orderRequest
        ));
//        kafkaTemplate.setMessageConverter(new KafkaMessageOrderConverter());
        return order;
    }


    @Override
    public String payment(HttpServletRequest req) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = Integer.parseInt(req.getParameter("amount")) * 100L;

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
        return VnpayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    @Override
    public boolean paymentSuccess(HttpServletRequest req) {
        String vnpResponseCode = req.getParameter("vnp_ResponseCode");
        if (vnpResponseCode == null || !vnpResponseCode.equals("00")) {
            return false;
        }
        String orderId = req.getParameter("orderId");
        if (orderId == null) return false;
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order orderInfo = order.get();
            orderInfo.setPaymentStatus(PaymentStatus.PAID);
            orderInfo.setPaymentDate(LocalDateTime.now());
            orderRepository.save(orderInfo);
            return true;
        }
        return false;
    }

    private Order getOrder(OrderRequest orderRequest, double totalAmount, List<ProductOrder> productOrders) throws DataNotFoundException {
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        if (orderRequest.getPaymentMethod().equals(PaymentMethod.ATM)) {
            order.setPaymentStatus(PaymentStatus.UNPAID);
        }
        order.setProductOrders(productOrders);
        order.setShippingAddress(orderRequest.getUserAddress());
        order.setNote(orderRequest.getNote());
        order.setShippingAmount(orderRequest.getDeliveryFee());
        Voucher voucher = voucherRepository.findByCode(orderRequest.getVoucherCode())
                .orElse(null);
        if (voucher != null) {
            checkVoucher(order, voucher, orderRequest.getUserId());
            calcVoucher(order, voucher);
        }
        order.calcFinalAmount();
        return order;
    }

    private void checkVoucher(Order order, Voucher voucher, String userId) throws DataNotFoundException {
        if (voucher.getQuantity() <= 0) {
            throw new DataNotFoundException("out of voucher quantity");
        }

        if (!voucher.isApplyAll()) {
            Set<String> users = voucher.getApplyFor();
            if (!users.contains(userId)) {
                throw new DataNotFoundException("user not found");
            }
        }

        if (order.getTotalAmount() < voucher.getMinOrder())
            throw new DataNotFoundException("not enough order");

        if (voucherUsageRepository.existsByVoucherIdAndUserId(voucher.getId(), userId)) {
            throw new DataNotFoundException("voucher usage");
        }

        VoucherUsage voucherUsage = new VoucherUsage();
        voucherUsage.setVoucherId(voucher.getId());
        voucherUsage.setUserId(userId);
        voucherUsageRepository.save(voucherUsage);

        voucher.setQuantity(voucher.getQuantity() - 1);
        voucherRepository.save(voucher);
    }

    private void calcVoucher(Order order, Voucher voucher) {
        if (voucher.getDiscountType().equals(DiscountType.PERCENT)) {
            double discount = voucher.getDiscountValue() * order.getTotalAmount();
            order.setVoucherAmount(Math.min(discount, voucher.getMaxPrice()));
        } else {
            order.setVoucherAmount(voucher.getDiscountValue());
        }
    }


}
