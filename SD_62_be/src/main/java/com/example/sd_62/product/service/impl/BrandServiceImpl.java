package com.example.sd_62.product.service.impl;

import com.example.sd_62.common.exception.ApiException;
import com.example.sd_62.common.util.MapperUtils;
import com.example.sd_62.product.dto.request.BrandRequest;
import com.example.sd_62.product.dto.response.BrandResponse;
import com.example.sd_62.product.entity.Brand;
import com.example.sd_62.product.enums.BrandStatus;
import com.example.sd_62.product.repository.BrandRepository;
import com.example.sd_62.product.service.BrandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    // ==================== CRUD CƠ BẢN ====================

    @Override
    @Transactional
    public void save(Integer id, BrandRequest dto) {
        // Kiểm tra trùng code
        if (id == null) {
            if (brandRepository.existsByCodeIgnoreCase(dto.getCode())) {
                throw new ApiException("Mã thương hiệu đã tồn tại: " + dto.getCode(), "409");
            }
            if (brandRepository.existsByNameIgnoreCase(dto.getName())) {
                throw new ApiException("Tên thương hiệu đã tồn tại: " + dto.getName(), "409");
            }
        } else {
            if (brandRepository.existsByCodeIgnoreCaseAndIdNot(dto.getCode(), id)) {
                throw new ApiException("Mã thương hiệu đã tồn tại: " + dto.getCode(), "409");
            }
            if (brandRepository.existsByNameIgnoreCaseAndIdNot(dto.getName(), id)) {
                throw new ApiException("Tên thương hiệu đã tồn tại: " + dto.getName(), "409");
            }
        }

        Brand brand;
        if (id == null) {
            brand = MapperUtils.map(dto, Brand.class);
            brand.setStatus(BrandStatus.ACTIVE);
        } else {
            brand = brandRepository.findById(id)
                    .orElseThrow(() -> new ApiException("Không tìm thấy thương hiệu có ID: " + id, "404"));
            MapperUtils.mapToExisting(dto, brand);
        }

        brandRepository.save(brand);
        log.info("✅ Saved brand: {} - {}", brand.getCode(), brand.getName());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy thương hiệu có ID: " + id, "404"));

        if (brand.getStatus() == BrandStatus.INACTIVE) {
            throw new ApiException("Thương hiệu đã ở trạng thái INACTIVE", "400");
        }

        brand.setStatus(BrandStatus.INACTIVE);
        brandRepository.save(brand);
        log.info("🗑️ Soft deleted brand ID: {}", id);
    }

    @Override
    @Transactional
    public void restore(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy thương hiệu có ID: " + id, "404"));

        if (brand.getStatus() == BrandStatus.ACTIVE) {
            throw new ApiException("Thương hiệu đang ở trạng thái ACTIVE", "400");
        }

        brand.setStatus(BrandStatus.ACTIVE);
        brandRepository.save(brand);
        log.info("🔄 Restored brand ID: {}", id);
    }

    @Override
    public BrandResponse getById(Integer id) {
        return brandRepository.findById(id)
                .map(BrandResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy thương hiệu có ID: " + id, "404"));
    }

    @Override
    public List<BrandResponse> getAll(String status) {
        List<Brand> brands;

        if (status != null && !status.isEmpty()) {
            try {
                BrandStatus brandStatus = BrandStatus.valueOf(status.toUpperCase());
                brands = brandRepository.findByStatusOrderByNameAsc(brandStatus);
            } catch (IllegalArgumentException e) {
                throw new ApiException("Status không hợp lệ: " + status + ". Chấp nhận: ACTIVE, INACTIVE", "400");
            }
        } else {
            brands = brandRepository.findAllByOrderByNameAsc();
        }

        return brands.stream()
                .map(BrandResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== TÌM KIẾM CƠ BẢN ====================

    @Override
    public BrandResponse getByCode(String code) {
        return brandRepository.findByCode(code)
                .map(BrandResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy thương hiệu với mã: " + code, "404"));
    }

    @Override
    public BrandResponse getByName(String name) {
        return brandRepository.findByName(name)
                .map(BrandResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy thương hiệu với tên: " + name, "404"));
    }

    @Override
    public List<BrandResponse> getByStatus(BrandStatus status) {
        return brandRepository.findByStatusOrderByNameAsc(status).stream()
                .map(BrandResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== KIỂM TRA ====================

    @Override
    public boolean existsByCode(String code) {
        return brandRepository.existsByCodeIgnoreCase(code);
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, Integer id) {
        return brandRepository.existsByCodeIgnoreCaseAndIdNot(code, id);
    }

    @Override
    public boolean existsByName(String name) {
        return brandRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return brandRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    // ==================== THỐNG KÊ ====================

    @Override
    public long countByStatus(String status) {
        try {
            BrandStatus brandStatus = BrandStatus.valueOf(status.toUpperCase());
            return brandRepository.countByStatus(brandStatus);
        } catch (IllegalArgumentException e) {
            throw new ApiException("Status không hợp lệ: " + status, "400");
        }
    }
}