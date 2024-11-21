package com.code.ecommercebackend.repositories.customizations.order;

import com.code.ecommercebackend.models.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Order> getOrderInYear(int year) {
        LocalDateTime startDate = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year + 1, Month.JANUARY, 1, 0, 0);

        Query query = new Query();
        Criteria criteriaStartDate = Criteria.where("createdAt").gte(startDate);
        Criteria criteriaEndDate = Criteria.where("createdAt").lt(endDate);
        Criteria criteriaStatus = Criteria.where("orderStatus").in("SHIPPED_CONFIRMATION", "RECEIVED");
        query.addCriteria(new Criteria().orOperator(criteriaStartDate, criteriaEndDate).andOperator(criteriaStatus));
        query.with(Sort.by(Sort.Direction.ASC, "createdAt"));

        return mongoTemplate.find(query, Order.class);

    }

    @Override
    public long countByStartDateEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        Query query = new Query();
        Criteria criteriaStartDate = Criteria.where("createdAt").gte(startDate);
        Criteria criteriaEndDate = Criteria.where("createdAt").lt(endDate);
        Criteria criteriaStatus = Criteria.where("orderStatus").in("SHIPPED_CONFIRMATION", "RECEIVED");
        query.addCriteria(new Criteria().orOperator(criteriaStartDate, criteriaEndDate).andOperator(criteriaStatus));
        return mongoTemplate.count(query, Order.class);
    }

    @Override
    public List<Order> findByStartDateEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        Query query = new Query();
        Criteria criteriaStartDate = Criteria.where("createdAt").gte(startDate);
        Criteria criteriaEndDate = Criteria.where("createdAt").lt(endDate);
        Criteria criteriaStatus = Criteria.where("orderStatus").in("SHIPPED_CONFIRMATION", "RECEIVED");
        query.addCriteria(new Criteria().orOperator(criteriaStartDate, criteriaEndDate).andOperator(criteriaStatus));
        return mongoTemplate.find(query, Order.class);
    }
}
