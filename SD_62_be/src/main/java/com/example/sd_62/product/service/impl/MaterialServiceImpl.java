package com.example.sd_62.product.service.impl;

import com.example.sd_62.common.exception.ApiException;
import com.example.sd_62.common.util.MapperUtils;
import com.example.sd_62.product.dto.request.MaterialRequest;
import com.example.sd_62.product.dto.response.MaterialResponse;
import com.example.sd_62.product.entity.Material;
import com.example.sd_62.product.repository.MaterialRepository;
import com.example.sd_62.product.service.MaterialService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    // ==================== CRUD CƠ BẢN ====================

    @Override
    @Transactional
    public void save(Integer id, MaterialRequest dto) {
        // Kiểm tra trùng tên
        if (id == null) {
            if (materialRepository.existsByNameIgnoreCase(dto.getName())) {
                throw new ApiException("Tên chất liệu đã tồn tại: " + dto.getName(), "409");
            }
        } else {
            if (materialRepository.existsByNameIgnoreCaseAndIdNot(dto.getName(), id)) {
                throw new ApiException("Tên chất liệu đã tồn tại: " + dto.getName(), "409");
            }
        }

        Material material;
        if (id == null) {
            material = MapperUtils.map(dto, Material.class);
        } else {
            material = materialRepository.findById(id)
                    .orElseThrow(() -> new ApiException("Không tìm thấy chất liệu có ID: " + id, "404"));
            MapperUtils.mapToExisting(dto, material);
        }

        materialRepository.save(material);
        log.info("✅ Saved material: {}", material.getName());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy chất liệu có ID: " + id, "404"));
        
        materialRepository.delete(material); // Xóa cứng vì entity không có status
        log.info("🗑️ Deleted material ID: {} - {}", id, material.getName());
    }

    @Override
    public MaterialResponse getById(Integer id) {
        return materialRepository.findById(id)
                .map(MaterialResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy chất liệu có ID: " + id, "404"));
    }

    @Override
    public List<MaterialResponse> getAll() {
        return materialRepository.findAllByOrderByNameAsc().stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== TÌM KIẾM CƠ BẢN ====================

    @Override
    public MaterialResponse getByName(String name) {
        return materialRepository.findByName(name)
                .map(MaterialResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy chất liệu với tên: " + name, "404"));
    }

    @Override
    public List<MaterialResponse> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        
        return materialRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(MaterialResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== KIỂM TRA ====================

    @Override
    public boolean existsByName(String name) {
        return materialRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return materialRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }
}