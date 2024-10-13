package com.code.ecommercebackend.services.address;

import com.code.ecommercebackend.dtos.response.ResponseGHN;
import com.code.ecommercebackend.dtos.response.address.DistrictResponse;
import com.code.ecommercebackend.dtos.response.address.ProvinceResponse;
import com.code.ecommercebackend.dtos.response.address.WardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestAddressServiceImpl implements RestAddressService{
    @Value("${api.ghn.url}")
    private String ghnUrl;
    @Value("${api.ghtk.url}")
    private String ghtkUrl;
    @Value("${api.ghn.token}")
    private String ghnToken;
    @Value("${api.ghtk.token}")
    private String ghtkToken;
    private final RestTemplate restTemplate;

    @Override
    public ResponseGHN<List<ProvinceResponse>> getProvinces() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<ResponseGHN<List<ProvinceResponse>>> response = restTemplate.exchange(ghnUrl + "/province",
                HttpMethod.GET, entity,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }

    @Override
    public ResponseGHN<List<DistrictResponse>> getDistricts(int provinceId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnToken);
        String url = ghnUrl + "/district?province_id=" + provinceId;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<ResponseGHN<List<DistrictResponse>>> response = restTemplate.exchange(url,
                HttpMethod.GET, entity,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }

    @Override
    public ResponseGHN<List<WardResponse>> getWards(int districtId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnToken);
        String url = ghnUrl + "/ward?district_id=" + districtId;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<ResponseGHN<List<WardResponse>>> response = restTemplate.exchange(url,
                HttpMethod.GET, entity,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }
}
