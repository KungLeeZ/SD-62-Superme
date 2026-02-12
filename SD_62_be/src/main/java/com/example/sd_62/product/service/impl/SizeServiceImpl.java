package com.example.sd_62.product.service.impl;

import com.example.sd_62.common.exception.ApiException;
import com.example.sd_62.product.dto.response.SizeResponse;
import com.example.sd_62.product.entity.Size;
import com.example.sd_62.product.repository.SizeRepository;
import com.example.sd_62.product.service.SizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;

    @Override
    public List<SizeResponse> getAll() {
        return sizeRepository.findAllByOrderByJpAsc().stream()
                .map(SizeResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public SizeResponse getById(Integer id) {
        return sizeRepository.findById(id)
                .map(SizeResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy size có ID: " + id, "404"));
    }

    @Override
    public SizeResponse getByJp(BigDecimal jp) {
        return sizeRepository.findByJp(jp)
                .map(SizeResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy size JP: " + jp, "404"));
    }

    @Override
    public SizeResponse getByEu(BigDecimal eu) {
        return sizeRepository.findByEu(eu)
                .map(SizeResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy size EU: " + eu, "404"));
    }

    @Override
    public SizeResponse getByUsMen(BigDecimal usMen) {
        return sizeRepository.findByUsMen(usMen)
                .map(SizeResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy size US Men: " + usMen, "404"));
    }

    @Override
    public SizeResponse getByUsWomen(BigDecimal usWomen) {
        return sizeRepository.findByUsWomen(usWomen)
                .map(SizeResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy size US Women: " + usWomen, "404"));
    }

    @Override
    public SizeResponse getByFootLength(BigDecimal footLength) {
        return sizeRepository.findByFootLength(footLength)
                .map(SizeResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy size với chiều dài chân: " + footLength, "404"));
    }

    @Override
    public List<SizeResponse> getByJpRange(BigDecimal from, BigDecimal to) {
        if (from.compareTo(to) > 0) {
            throw new ApiException("Giá trị từ phải nhỏ hơn hoặc bằng đến", "400");
        }
        
        return sizeRepository.findByJpBetween(from, to).stream()
                .map(SizeResponse::new)
                .collect(Collectors.toList());
    }
}