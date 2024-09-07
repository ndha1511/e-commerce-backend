package com.code.ecommercebackend.dtos.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private List<T> items;
}
