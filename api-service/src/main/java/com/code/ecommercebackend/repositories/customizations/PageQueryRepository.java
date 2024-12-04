package com.code.ecommercebackend.repositories.customizations;

import com.code.ecommercebackend.dtos.response.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class PageQueryRepository<T> {

    @Autowired
    protected MongoTemplate mongoTemplate;

    protected static final Pattern FILTER_PATTERN = Pattern.compile("(.*?)([<>]=?|:|-|!|=)([^-]*)");

    protected static final Pattern SORT_PATTERN = Pattern.compile("(\\w+?)(:)(asc|desc)");


    public PageResponse<T> getPageData(int pageNo, int size, String[] search, String[] sort, Class<T> clazz) {
        Query query = new Query();
        if (search != null) {
            for (String s : search) {
                addConditionQuery(query, s);
            }
        }
        if (sort != null) {
            for (String s : sort) {
                addSort(query, s);
            }
        }

        long total = mongoTemplate.count(query, clazz);
        // define $skip: (pageNo - 1) * size $limit: size
        Pageable pageable = PageRequest.of(pageNo - 1, size);
        // apply $skip $limit
        query.with(pageable);
        List<T> result = mongoTemplate.find(query, clazz);

        Page<T> page = new PageImpl<>(result, pageable, total);

        return new PageResponse<>(
                pageNo,
                result.size(),
                page.getTotalPages(),
                page.getContent()
        );
    }

    // condition = 'field:value'
    protected void addConditionQuery(Query query, String condition) {
        Matcher matcher = FILTER_PATTERN.matcher(condition);
        if (matcher.find()) {
            String field = matcher.group(1);
            String operator = matcher.group(2);
            String value = matcher.group(3);
            query.addCriteria(getCriteria(field, operator, value));
        }
    }

    // field:asc|desc
    protected void addSort(Query query, String sort) {
        Matcher matcher = SORT_PATTERN.matcher(sort);
        if (matcher.find()) {
            String field = matcher.group(1);
            String order = matcher.group(3);
            if (order.equals("asc")) {
                query.with(Sort.by(Sort.Direction.ASC, field));
            } else {
                query.with(Sort.by(Sort.Direction.DESC, field));
            }
        }
    }

    protected Criteria getCriteria(String field, String operator, String value) {
        if (value.equals("null")) {
            return Criteria.where(field).isNull();
        }

        // Kiểm tra xem giá trị có phải là một số không (số nguyên hoặc số thực)
        boolean isNumeric = value.matches("-?\\d+(\\.\\d+)?");  // Kiểm tra nếu là số nguyên hoặc số thực

        // Nếu giá trị là số, chuyển đổi và xử lý
        if (isNumeric) {
            double doubleValue = Double.parseDouble(value);  // Chuyển giá trị sang kiểu double

            return switch (operator) {
                case "<" -> Criteria.where(field).lt(doubleValue);  // So sánh bé hơn
                case ">" -> Criteria.where(field).gt(doubleValue);  // So sánh lớn hơn
                case "<=" -> Criteria.where(field).lte(doubleValue);  // So sánh bé hơn hoặc bằng
                case ">=" -> Criteria.where(field).gte(doubleValue);  // So sánh lớn hơn hoặc bằng
                case "=" -> Criteria.where(field).is(doubleValue);  // So sánh bằng
                case "!=" -> Criteria.where(field).ne(doubleValue);  // So sánh không bằng
                default -> Criteria.where(field).regex(value.trim(), "i");  // Nếu không phải toán tử số, sử dụng regex
            };
        }

        // Nếu không phải là số, xử lý chuỗi (kể cả trường hợp tìm kiếm theo "rating:true", "rating:false", ...)
        return switch (operator) {
            case "=" -> Criteria.where(field).is(value.equals("false") ? false : value.equals("true") ? true : value);
            case "!=" -> Criteria.where(field).ne(value);
            case "-" -> Criteria.where(field).exists(false);  // Trường không tồn tại
            default -> Criteria.where(field).regex(value.trim(), "i");  // Sử dụng regex cho trường hợp khác
        };
    }


}
