package com.example.sd_62.product.service.impl;

import com.example.sd_62.common.exception.ApiException;
import com.example.sd_62.common.util.MapperUtils;
import com.example.sd_62.product.dto.request.SeasonRequest;
import com.example.sd_62.product.dto.response.SeasonResponse;
import com.example.sd_62.product.entity.Season;
import com.example.sd_62.product.repository.SeasonRepository;
import com.example.sd_62.product.service.SeasonService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeasonServiceImpl implements SeasonService {

    private final SeasonRepository seasonRepository;

    // ==================== CRUD CƠ BẢN ====================

    @Override
    @Transactional
    public void save(Integer id, SeasonRequest dto) {
        // Kiểm tra trùng season code
        if (id == null) {
            if (seasonRepository.existsBySeasonCodeIgnoreCase(dto.getSeasonCode())) {
                throw new ApiException("Mã mùa đã tồn tại: " + dto.getSeasonCode(), "409");
            }
            if (seasonRepository.existsByNameIgnoreCase(dto.getName())) {
                throw new ApiException("Tên mùa đã tồn tại: " + dto.getName(), "409");
            }
        } else {
            if (seasonRepository.existsBySeasonCodeIgnoreCaseAndIdNot(dto.getSeasonCode(), id)) {
                throw new ApiException("Mã mùa đã tồn tại: " + dto.getSeasonCode(), "409");
            }
            if (seasonRepository.existsByNameIgnoreCaseAndIdNot(dto.getName(), id)) {
                throw new ApiException("Tên mùa đã tồn tại: " + dto.getName(), "409");
            }
        }

        Season season;
        if (id == null) {
            season = MapperUtils.map(dto, Season.class);
        } else {
            season = seasonRepository.findById(id)
                    .orElseThrow(() -> new ApiException("Không tìm thấy mùa có ID: " + id, "404"));
            MapperUtils.mapToExisting(dto, season);
        }

        seasonRepository.save(season);
        log.info("✅ Saved season: {} - {}", season.getSeasonCode(), season.getName());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!seasonRepository.existsById(id)) {
            throw new ApiException("Không tìm thấy mùa có ID: " + id, "404");
        }
        
        // Kiểm tra xem season có đang được sử dụng trong ProductRelease không?
        // TODO: Thêm check constraint nếu cần
        
        seasonRepository.deleteById(id);
        log.info("🗑️ Deleted season ID: {}", id);
    }

    @Override
    public SeasonResponse getById(Integer id) {
        return seasonRepository.findById(id)
                .map(SeasonResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy mùa có ID: " + id, "404"));
    }

    @Override
    public List<SeasonResponse> getAll() {
        return seasonRepository.findAllByOrderByYearDescNameAsc().stream()
                .map(SeasonResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<SeasonResponse> getAllPaging(Pageable pageable) {
        return seasonRepository.findAllByOrderByYearDescNameAsc(pageable)
                .map(SeasonResponse::new);
    }

    // ==================== TÌM KIẾM ====================

    @Override
    public SeasonResponse getBySeasonCode(String seasonCode) {
        return seasonRepository.findBySeasonCode(seasonCode)
                .map(SeasonResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy mùa với mã: " + seasonCode, "404"));
    }

    @Override
    public SeasonResponse getByName(String name) {
        return seasonRepository.findByName(name)
                .map(SeasonResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy mùa với tên: " + name, "404"));
    }

    @Override
    public List<SeasonResponse> getByYear(Integer year) {
        List<Season> seasons = seasonRepository.findByYear(year);
        if (seasons.isEmpty()) {
            throw new ApiException("Không tìm thấy mùa nào trong năm: " + year, "404");
        }
        return seasons.stream()
                .map(SeasonResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeasonResponse> getByYearRange(Integer startYear, Integer endYear) {
        if (startYear > endYear) {
            throw new ApiException("Năm bắt đầu phải nhỏ hơn năm kết thúc", "400");
        }
        return seasonRepository.findByYearBetween(startYear, endYear).stream()
                .map(SeasonResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeasonResponse> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return seasonRepository
                .findBySeasonCodeContainingIgnoreCaseOrNameContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(SeasonResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<SeasonResponse> searchPaging(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllPaging(pageable);
        }
        return seasonRepository
                .findBySeasonCodeContainingIgnoreCaseOrNameContainingIgnoreCase(keyword, keyword, pageable)
                .map(SeasonResponse::new);
    }

    // ==================== KIỂM TRA ====================

    @Override
    public boolean existsBySeasonCode(String seasonCode) {
        return seasonRepository.existsBySeasonCodeIgnoreCase(seasonCode);
    }

    @Override
    public boolean existsBySeasonCodeAndIdNot(String seasonCode, Integer id) {
        return seasonRepository.existsBySeasonCodeIgnoreCaseAndIdNot(seasonCode, id);
    }

    @Override
    public boolean existsByName(String name) {
        return seasonRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return seasonRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    // ==================== THỐNG KÊ ====================

    @Override
    public long countByYear(Integer year) {
        return seasonRepository.countByYear(year);
    }
}