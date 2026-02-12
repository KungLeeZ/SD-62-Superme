package com.example.sd_62.product.service.impl;

import com.example.sd_62.common.exception.ApiException;
import com.example.sd_62.common.util.MapperUtils;
import com.example.sd_62.product.dto.request.ProductReleaseRequest;
import com.example.sd_62.product.dto.response.ProductReleaseResponse;
import com.example.sd_62.product.entity.ProductRelease;
import com.example.sd_62.product.entity.Season;
import com.example.sd_62.product.enums.DropStatus;
import com.example.sd_62.product.repository.ProductReleaseRepository;
import com.example.sd_62.product.repository.SeasonRepository;
import com.example.sd_62.product.service.ProductReleaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductReleaseServiceImpl implements ProductReleaseService {

    private final ProductReleaseRepository productReleaseRepository;
    private final SeasonRepository seasonRepository;

    // ==================== CRUD CƠ BẢN ====================

    @Override
    @Transactional
    public void save(Integer id, ProductReleaseRequest dto) {
        // Kiểm tra trùng code
        if (id == null) {
            if (productReleaseRepository.existsByCodeIgnoreCase(dto.getCode())) {
                throw new ApiException("Mã đợt phát hành đã tồn tại: " + dto.getCode(), "409");
            }
            if (productReleaseRepository.existsByNameIgnoreCase(dto.getName())) {
                throw new ApiException("Tên đợt phát hành đã tồn tại: " + dto.getName(), "409");
            }
        } else {
            if (productReleaseRepository.existsByCodeIgnoreCaseAndIdNot(dto.getCode(), id)) {
                throw new ApiException("Mã đợt phát hành đã tồn tại: " + dto.getCode(), "409");
            }
            if (productReleaseRepository.existsByNameIgnoreCaseAndIdNot(dto.getName(), id)) {
                throw new ApiException("Tên đợt phát hành đã tồn tại: " + dto.getName(), "409");
            }
        }

        // Kiểm tra Season
        Season season = seasonRepository.findById(dto.getSeasonId())
                .orElseThrow(() -> new ApiException("Không tìm thấy mùa có ID: " + dto.getSeasonId(), "404"));

        // Kiểm tra ngày
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            if (dto.getStartDate().isAfter(dto.getEndDate())) {
                throw new ApiException("Ngày bắt đầu phải trước ngày kết thúc", "400");
            }
        }

        ProductRelease productRelease;
        if (id == null) {
            productRelease = MapperUtils.map(dto, ProductRelease.class);
            productRelease.setCreatedAt(LocalDateTime.now());
            productRelease.setStatus(DropStatus.ACTIVE);
        } else {
            productRelease = productReleaseRepository.findById(id)
                    .orElseThrow(() -> new ApiException("Không tìm thấy đợt phát hành có ID: " + id, "404"));
            MapperUtils.mapToExisting(dto, productRelease);
            productRelease.setUpdatedAt(LocalDateTime.now());
        }

        productRelease.setSeason(season);
        productReleaseRepository.save(productRelease);
        log.info("✅ Saved product release: {} - {}", productRelease.getCode(), productRelease.getName());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        ProductRelease productRelease = productReleaseRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy đợt phát hành có ID: " + id, "404"));

        if (productRelease.getStatus() == DropStatus.ACTIVE) {
            throw new ApiException("Đợt phát hành đã ở trạng thái ACTIVE", "400");
        }

        productRelease.setStatus(DropStatus.ACTIVE);
        productRelease.setUpdatedAt(LocalDateTime.now());
        productReleaseRepository.save(productRelease);
        log.info("🗑️ Soft deleted product release ID: {}", id);
    }

    @Override
    @Transactional
    public void restore(Integer id) {
        ProductRelease productRelease = productReleaseRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy đợt phát hành có ID: " + id, "404"));

        if (productRelease.getStatus() == DropStatus.ACTIVE) {
            throw new ApiException("Đợt phát hành đang ở trạng thái ACTIVE", "400");
        }

        productRelease.setStatus(DropStatus.ACTIVE);
        productRelease.setUpdatedAt(LocalDateTime.now());
        productReleaseRepository.save(productRelease);
        log.info("🔄 Restored product release ID: {}", id);
    }

    @Override
    public ProductReleaseResponse getById(Integer id) {
        return productReleaseRepository.findById(id)
                .map(ProductReleaseResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy đợt phát hành có ID: " + id, "404"));
    }

    @Override
    public List<ProductReleaseResponse> getAll(String status) {
        List<ProductRelease> releases;
        
        if (status != null && !status.isEmpty()) {
            try {
                DropStatus dropStatus = DropStatus.valueOf(status.toUpperCase());
                releases = productReleaseRepository.findByStatus(dropStatus);
            } catch (IllegalArgumentException e) {
                throw new ApiException("Status không hợp lệ: " + status, "400");
            }
        } else {
            releases = productReleaseRepository.findAllByOrderByStartDateDesc();
        }

        return releases.stream()
                .map(ProductReleaseResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductReleaseResponse> getAllPaging(String status, Pageable pageable) {
        Page<ProductRelease> releasePage;
        
        if (status != null && !status.isEmpty()) {
            try {
                DropStatus dropStatus = DropStatus.valueOf(status.toUpperCase());
                releasePage = productReleaseRepository.findByStatus(dropStatus, pageable);
            } catch (IllegalArgumentException e) {
                throw new ApiException("Status không hợp lệ: " + status, "400");
            }
        } else {
            releasePage = productReleaseRepository.findAll(pageable);
        }

        return releasePage.map(ProductReleaseResponse::new);
    }

    // ==================== TÌM THEO MÙA ====================

    @Override
    public List<ProductReleaseResponse> getBySeasonId(Integer seasonId) {
        if (!seasonRepository.existsById(seasonId)) {
            throw new ApiException("Không tìm thấy mùa có ID: " + seasonId, "404");
        }
        return productReleaseRepository.findBySeasonId(seasonId).stream()
                .map(ProductReleaseResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductReleaseResponse> getBySeasonIdPaging(Integer seasonId, Pageable pageable) {
        if (!seasonRepository.existsById(seasonId)) {
            throw new ApiException("Không tìm thấy mùa có ID: " + seasonId, "404");
        }
        return productReleaseRepository.findBySeasonId(seasonId, pageable)
                .map(ProductReleaseResponse::new);
    }

    // ==================== TÌM THEO THỜI GIAN ====================

    @Override
    public List<ProductReleaseResponse> getCurrentReleases() {
        return productReleaseRepository.findCurrentReleases(LocalDateTime.now(), DropStatus.ACTIVE).stream()
                .map(ProductReleaseResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductReleaseResponse> getByDateRange(LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            throw new ApiException("Ngày bắt đầu phải trước ngày kết thúc", "400");
        }
        return productReleaseRepository.findByStartDateBetween(from, to).stream()
                .map(ProductReleaseResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== TÌM KIẾM ====================

    @Override
    public ProductReleaseResponse getByCode(String code) {
        return productReleaseRepository.findByCode(code)
                .map(ProductReleaseResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy đợt phát hành với mã: " + code, "404"));
    }

    @Override
    public ProductReleaseResponse getByName(String name) {
        return productReleaseRepository.findByName(name)
                .map(ProductReleaseResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy đợt phát hành với tên: " + name, "404"));
    }

    @Override
    public List<ProductReleaseResponse> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll(null);
        }
        return productReleaseRepository
                .findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(ProductReleaseResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductReleaseResponse> searchPaging(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllPaging(null, pageable);
        }
        return productReleaseRepository
                .findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(keyword, keyword, pageable)
                .map(ProductReleaseResponse::new);
    }

    // ==================== KIỂM TRA ====================

    @Override
    public boolean existsByCode(String code) {
        return productReleaseRepository.existsByCodeIgnoreCase(code);
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, Integer id) {
        return productReleaseRepository.existsByCodeIgnoreCaseAndIdNot(code, id);
    }

    @Override
    public boolean existsByName(String name) {
        return productReleaseRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return productReleaseRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    // ==================== THỐNG KÊ ====================

    @Override
    public long countByStatus(String status) {
        try {
            DropStatus dropStatus = DropStatus.valueOf(status.toUpperCase());
            return productReleaseRepository.countByStatus(dropStatus);
        } catch (IllegalArgumentException e) {
            throw new ApiException("Status không hợp lệ: " + status, "400");
        }
    }
}