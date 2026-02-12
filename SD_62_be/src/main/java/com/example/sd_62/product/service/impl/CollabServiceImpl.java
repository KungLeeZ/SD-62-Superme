package com.example.sd_62.product.service.impl;

import com.example.sd_62.common.exception.ApiException;
import com.example.sd_62.common.util.MapperUtils;
import com.example.sd_62.product.dto.request.CollabRequest;
import com.example.sd_62.product.dto.response.CollabResponse;
import com.example.sd_62.product.entity.Collab;
import com.example.sd_62.product.repository.CollabRepository;
import com.example.sd_62.product.service.CollabService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollabServiceImpl implements CollabService {

    private final CollabRepository collabRepository;

    @Override
    @Transactional
    public void save(Integer id, CollabRequest dto) {
        // Kiểm tra trùng tên
        if (id == null) {
            if (collabRepository.existsByNameIgnoreCase(dto.getName())) {
                throw new ApiException("Tên collab đã tồn tại: " + dto.getName(), "409");
            }
        } else {
            if (collabRepository.existsByNameIgnoreCaseAndIdNot(dto.getName(), id)) {
                throw new ApiException("Tên collab đã tồn tại: " + dto.getName(), "409");
            }
        }

        Collab collab;
        if (id == null) {
            collab = MapperUtils.map(dto, Collab.class);
        } else {
            collab = collabRepository.findById(id)
                    .orElseThrow(() -> new ApiException("Không tìm thấy collab có ID: " + id, "404"));
            MapperUtils.mapToExisting(dto, collab);
        }

        collabRepository.save(collab);
        log.info("✅ Saved collab: {}", collab.getName());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Collab collab = collabRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy collab có ID: " + id, "404"));
        
        collabRepository.delete(collab); // Xóa cứng vì entity không có status
        log.info("🗑️ Deleted collab ID: {}", id);
    }

    @Override
    public CollabResponse getById(Integer id) {
        return collabRepository.findById(id)
                .map(CollabResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy collab có ID: " + id, "404"));
    }

    @Override
    public List<CollabResponse> getAll() {
        return collabRepository.findAllByOrderByNameAsc().stream()
                .map(CollabResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CollabResponse> getAllActive() {
        // Vì Collab không có status, method này trả về tất cả
        return getAll();
    }

    @Override
    public CollabResponse getByName(String name) {
        return collabRepository.findByName(name)
                .map(CollabResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy collab với tên: " + name, "404"));
    }

    @Override
    public List<CollabResponse> getByType(String type) {
        List<Collab> collabs = collabRepository.findByType(type);
        if (collabs.isEmpty()) {
            throw new ApiException("Không tìm thấy collab nào với type: " + type, "404");
        }
        return collabs.stream()
                .map(CollabResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CollabResponse> getByYear(Integer year) {
        List<Collab> collabs = collabRepository.findByYear(year);
        if (collabs.isEmpty()) {
            throw new ApiException("Không tìm thấy collab nào với năm: " + year, "404");
        }
        return collabs.stream()
                .map(CollabResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CollabResponse> getByYearRange(Integer startYear, Integer endYear) {
        if (startYear > endYear) {
            throw new ApiException("Năm bắt đầu phải nhỏ hơn năm kết thúc", "400");
        }
        
        List<Collab> collabs = collabRepository.findByYearBetween(startYear, endYear);
        return collabs.stream()
                .map(CollabResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CollabResponse> getByTypeAndYear(String type, Integer year) {
        List<Collab> collabs = collabRepository.findByTypeAndYear(type, year);
        return collabs.stream()
                .map(CollabResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return collabRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return collabRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }
}