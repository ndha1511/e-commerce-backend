package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.category.CreateCategoryRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.mappers.category.CategoryMapper;
import com.code.ecommercebackend.models.Category;
import com.code.ecommercebackend.services.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    public Response createCategory(@Valid @ModelAttribute final CreateCategoryRequest categoryDto)
    throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                categoryService.save(categoryMapper.toCategory(categoryDto))
        );
    }

    @GetMapping
    public Response getCategories(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "40") int size,
                                @RequestParam(required = false) String[] search,
                                @RequestParam(required = false) String[] sort) {
        List<String> searchList = new ArrayList<>();
        searchList.add("inactive=false");
        if(search != null) {
            searchList.addAll(Arrays.asList(search));
        }
        search = searchList.toArray(new String[0]);
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                categoryService.getPageData(pageNo, size, search, sort, Category.class)
        );
    }

    @GetMapping("/{url}")
    public Response getCategory(@PathVariable final String url)
    throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                categoryService.findCategoryByUrlRoot(url)
        );
    }



}
