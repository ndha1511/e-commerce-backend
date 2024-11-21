package com.code.ecommercebackend.services.historySearch;

import com.code.ecommercebackend.models.HistorySearch;

import java.util.List;

public interface HistorySearchService {
    List<HistorySearch> getHistorySearch(String userId, String textSearch);
    void saveHistorySearch(String userId, String content);
}
