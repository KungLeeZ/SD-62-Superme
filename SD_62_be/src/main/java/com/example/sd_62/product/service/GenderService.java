package com.example.sd_62.product.service;

import com.example.sd_62.product.dto.response.GenderResponse;

import java.util.List;

public interface GenderService {

    List<GenderResponse> getAll();
}