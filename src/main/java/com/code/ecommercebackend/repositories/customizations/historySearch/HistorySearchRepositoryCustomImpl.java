package com.code.ecommercebackend.repositories.customizations.historySearch;

import com.code.ecommercebackend.models.HistorySearch;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistorySearchRepositoryCustomImpl implements HistorySearchRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<HistorySearch> findTop15OrderByCreatedAtDesc(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        query.limit(15);
        List<HistorySearch> historySearches = mongoTemplate.find(query, HistorySearch.class);
        if(historySearches.size() < 15){
            int lackSize = 15 - historySearches.size();
            historySearches.addAll(findTopHistorySearch(lackSize));
        }
        return historySearches;
    }

    @Override
    public List<HistorySearch> findHistorySearchByUserId(String userId, String textSearch) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.addCriteria(Criteria.where("searchContent").regex("^" + textSearch, "i"));
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        query.limit(15);
        List<HistorySearch> historySearches = mongoTemplate.find(query, HistorySearch.class);
        if(historySearches.size() < 15){
            int lackSize = 15 - historySearches.size();
            historySearches.addAll(findTopHistorySearch(textSearch, lackSize));
        }
        return historySearches;
    }

    @Override
    public List<HistorySearch> findTopHistorySearch(int limit) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("content").count().as("count"),

                Aggregation.sort(Sort.by(Sort.Direction.DESC, "count")),

                Aggregation.limit(limit)
        );
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "history_search", Document.class);

        List<HistorySearch> historySearches = new ArrayList<>();
        for (Document doc : results.getMappedResults()) {
            HistorySearch historySearch = new HistorySearch();
            historySearch.setContent(doc.getString("_id"));
            historySearches.add(historySearch);
        }
        return historySearches;
    }

    @Override
    public List<HistorySearch> findTopHistorySearch(String textSearch, int limit) {
        Aggregation aggregation = Aggregation.newAggregation(

                Aggregation.unwind("search_content"),
                Aggregation.match(Criteria.where("search_content").regex("^" + textSearch, "i")),
                Aggregation.group("content").count().as("count"),

                Aggregation.sort(Sort.by(Sort.Direction.DESC, "count")),

                Aggregation.limit(limit)
        );
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "history_search", Document.class);

        List<HistorySearch> historySearches = new ArrayList<>();
        for (Document doc : results.getMappedResults()) {
            HistorySearch historySearch = new HistorySearch();
            historySearch.setContent(doc.getString("_id"));
            historySearches.add(historySearch);
        }
        return historySearches;
    }

}
