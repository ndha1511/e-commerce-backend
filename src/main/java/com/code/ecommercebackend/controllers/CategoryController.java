package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.category.CreateCategoryRequest;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.mappers.category.CategoryMapper;
import com.code.ecommercebackend.models.Category;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.services.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;


    @PostMapping
    public Response createCategory(@Valid @RequestBody final CreateCategoryRequest categoryDto) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                categoryService.save(categoryDto, categoryMapper::toCategory)
        );
    }

    @GetMapping
    public Response getCategories(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "40") int size,
                                @RequestParam(required = false) String[] search,
                                @RequestParam(required = false) String[] sort) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                categoryService.getPageData(pageNo, size, search, sort, Category.class)
        );
    }

}
