package com.example.sd_62.product.service.impl;

import com.example.sd_62.product.dto.response.GenderResponse;
import com.example.sd_62.product.entity.Gender;
import com.example.sd_62.product.repository.GenderRepository;
import com.example.sd_62.product.service.GenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenderServiceImpl implements GenderService {

    private final GenderRepository genderRepository;

    @Override
    public List<GenderResponse> getAll() {
        return genderRepository.findAllByOrderByNameAsc().stream()
                .map(GenderResponse::new)
                .collect(Collectors.toList());
    }
}