package com.example.sd_62.product.service.impl;

import com.example.sd_62.common.exception.ApiException;
import com.example.sd_62.common.util.MapperUtils;
import com.example.sd_62.product.dto.request.ColorRequest;
import com.example.sd_62.product.dto.response.ColorResponse;
import com.example.sd_62.product.entity.Color;
import com.example.sd_62.product.repository.ColorRepository;
import com.example.sd_62.product.service.ColorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;

    // ==================== CRUD CƠ BẢN ====================

    @Override
    @Transactional
    public void save(Integer id, ColorRequest dto) {
        // Kiểm tra trùng tên
        if (id == null) {
            if (colorRepository.existsByNameIgnoreCase(dto.getName())) {
                throw new ApiException("Tên màu đã tồn tại: " + dto.getName(), "409");
            }
            if (dto.getHex() != null && colorRepository.existsByHexIgnoreCase(dto.getHex())) {
                throw new ApiException("Mã hex đã tồn tại: " + dto.getHex(), "409");
            }
        } else {
            if (colorRepository.existsByNameIgnoreCaseAndIdNot(dto.getName(), id)) {
                throw new ApiException("Tên màu đã tồn tại: " + dto.getName(), "409");
            }
            if (dto.getHex() != null && colorRepository.existsByHexIgnoreCaseAndIdNot(dto.getHex(), id)) {
                throw new ApiException("Mã hex đã tồn tại: " + dto.getHex(), "409");
            }
        }

        Color color;
        if (id == null) {
            color = MapperUtils.map(dto, Color.class);
        } else {
            color = colorRepository.findById(id)
                    .orElseThrow(() -> new ApiException("Không tìm thấy màu sắc có ID: " + id, "404"));
            MapperUtils.mapToExisting(dto, color);
        }

        colorRepository.save(color);
        log.info("✅ Saved color: {} - {}", color.getName(), color.getHex());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy màu sắc có ID: " + id, "404"));
        
        colorRepository.delete(color); // Xóa cứng vì entity không có status
        log.info("🗑️ Deleted color ID: {} - {}", id, color.getName());
    }

    @Override
    public ColorResponse getById(Integer id) {
        return colorRepository.findById(id)
                .map(ColorResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy màu sắc có ID: " + id, "404"));
    }

    @Override
    public List<ColorResponse> getAll() {
        return colorRepository.findAllByOrderByNameAsc().stream()
                .map(ColorResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== TÌM KIẾM CƠ BẢN ====================

    @Override
    public ColorResponse getByName(String name) {
        return colorRepository.findByName(name)
                .map(ColorResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy màu sắc với tên: " + name, "404"));
    }

    @Override
    public ColorResponse getByHex(String hex) {
        return colorRepository.findByHex(hex)
                .map(ColorResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy màu sắc với mã hex: " + hex, "404"));
    }

    @Override
    public List<ColorResponse> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        
        return colorRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(ColorResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== KIỂM TRA ====================

    @Override
    public boolean existsByName(String name) {
        return colorRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return colorRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    @Override
    public boolean existsByHex(String hex) {
        return colorRepository.existsByHexIgnoreCase(hex);
    }

    @Override
    public boolean existsByHexAndIdNot(String hex, Integer id) {
        return colorRepository.existsByHexIgnoreCaseAndIdNot(hex, id);
    }
}