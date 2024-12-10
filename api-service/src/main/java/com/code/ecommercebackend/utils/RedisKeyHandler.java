package com.code.ecommercebackend.utils;

import com.code.ecommercebackend.utils.enums.RedisKeyEnum;

import java.util.List;

public class RedisKeyHandler {

    public static String createKeyWithPageQuery(int pageNo,
                                                int pageSize,
                                                String[] search,
                                                String[] sort,
                                                RedisKeyEnum type) {
        StringBuilder key = new StringBuilder(type.getValue());
        key.append(":").append(pageNo);
        key.append(":").append(pageSize);
        if(search != null) {
            for (String s : search) {
                key.append(":").append(s);
            }
        }
        if(sort != null) {
            for (String s : sort) {
                key.append(":").append(s);
            }
        }
        return key.toString();
    }

    public static String createKeyWithPageQuery(int pageNo,
                                                int pageSize,
                                                String[] search,
                                                String[] sort,
                                                String[] options,
                                                RedisKeyEnum type) {
        StringBuilder key = new StringBuilder(type.getValue());
        key.append(":").append(pageNo);
        key.append(":").append(pageSize);
        if(search != null) {
            for (String s : search) {
                key.append(":").append(s);
            }
        }
        if(sort != null) {
            for (String s : sort) {
                key.append(":").append(s);
            }
        }
        if(options != null) {
            for (String s : options) {
                if(s != null) {
                    key.append(":").append(s);
                }
            }
        }
        return key.toString();
    }

    public static String createKeyWithId(String id, RedisKeyEnum type) {
        return type.getValue() + ":" + id;
    }

    public static String createKeyRecommend(long userId, List<Long> productIds, int nRecommend) {
        StringBuilder key = new StringBuilder(RedisKeyEnum.RECOMMEND.getValue());
        key.append(":").append(userId);
        key.append(":").append(nRecommend);
        for(Long productId : productIds) {
            key.append(":").append(productId);
        }
        return key.toString();
    }

}
