package com.code.ecommercebackend.services.historySearch;

import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.HistorySearch;
import com.code.ecommercebackend.repositories.HistorySearchRepository;
import com.code.ecommercebackend.repositories.customizations.historySearch.HistorySearchRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistorySearchServiceImpl implements HistorySearchService {
    private final HistorySearchRepositoryCustom historySearchRepositoryCustom;
    private final HistorySearchRepository historySearchRepository;

    @Override
    public List<HistorySearch> getHistorySearch(String userId, String textSearch) {
        if(textSearch == null || textSearch.isEmpty()) {
            if(userId == null || userId.isEmpty()) {
                return historySearchRepositoryCustom.findTopHistorySearch(15);
            } else {
                return historySearchRepositoryCustom.findTop15OrderByCreatedAtDesc(userId);
            }
        } else if(userId != null && !userId.isEmpty()) {
            return historySearchRepositoryCustom.findHistorySearchByUserId(userId, textSearch);
        }
        return historySearchRepositoryCustom.findTopHistorySearch(textSearch, 15);
    }

    @Override
    public void saveHistorySearch(String userId, String content) {
        HistorySearch historySearch = new HistorySearch();
        if(userId != null && !userId.isEmpty()) {
            historySearch.setUserId(userId);
        } else {
            Optional<HistorySearch> optionalHistorySearch = historySearchRepository.findByUserIdAndContent(userId, content);
            optionalHistorySearch.ifPresent(historySearchRepository::delete);
        }
        historySearch.setContent(content);
        historySearch.normalizerName(content);

        historySearchRepository.save(historySearch);
    }

    @Override
    public void deleteHistorySearchById(String historySearchId) {
        historySearchRepository.deleteById(historySearchId);
    }
}
