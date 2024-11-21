package com.code.ecommercebackend.crawlData.controller;

import com.code.ecommercebackend.crawlData.model.ProductDetail;
import com.code.ecommercebackend.crawlData.res.TikiRes;
import com.code.ecommercebackend.crawlData.service.CrawlDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/crawl")
public class CrawlController {

    private final CrawlDataService crawlDataService;

    @GetMapping
    public TikiRes getProducts(@RequestParam String url,
                               @RequestParam String[] brands,
                               @RequestParam Set<String> categories,
                               @RequestParam(required = false, defaultValue = "50000") long venue,
                               @RequestParam(required = false, defaultValue = "20") int quantity) {
        return crawlDataService.getProducts(url, brands, categories, venue, quantity);
    }

    @GetMapping("/detail")
    public ProductDetail getProductDetail(@RequestParam String url) {
        return crawlDataService.getProductDetail(url);
    }

    @GetMapping("/preview")
    public TikiRes previewData(@RequestParam String url) {
        return crawlDataService.previewData(url);
    }
}
