package com.code.ecommercebackend.repositories.customizations.historySearch;

import com.code.ecommercebackend.models.HistorySearch;

import java.util.List;

public interface HistorySearchRepositoryCustom {
    List<HistorySearch> findTop15OrderByCreatedAtDesc(String userId);
    List<HistorySearch> findHistorySearchByUserId(String userId, String textSearch);
    List<HistorySearch> findTopHistorySearch(int limit);
    List<HistorySearch> findTopHistorySearch(String textSearch, int limit);
}
