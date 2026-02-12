package com.example.sd_62.product.service.impl;

import com.example.sd_62.common.exception.ApiException;
import com.example.sd_62.common.util.MapperUtils;
import com.example.sd_62.product.dto.request.FormRequest;
import com.example.sd_62.product.dto.response.FormResponse;
import com.example.sd_62.product.entity.Form;
import com.example.sd_62.product.repository.FormRepository;
import com.example.sd_62.product.service.FormService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormServiceImpl implements FormService {

    private final FormRepository formRepository;

    // ==================== CRUD CƠ BẢN ====================

    @Override
    @Transactional
    public void save(Integer id, FormRequest dto) {
        // Kiểm tra trùng tên
        if (id == null) {
            if (formRepository.existsByNameIgnoreCase(dto.getName())) {
                throw new ApiException("Tên form sản phẩm đã tồn tại: " + dto.getName(), "409");
            }
        } else {
            if (formRepository.existsByNameIgnoreCaseAndIdNot(dto.getName(), id)) {
                throw new ApiException("Tên form sản phẩm đã tồn tại: " + dto.getName(), "409");
            }
        }

        Form form;
        if (id == null) {
            form = MapperUtils.map(dto, Form.class);
        } else {
            form = formRepository.findById(id)
                    .orElseThrow(() -> new ApiException("Không tìm thấy form sản phẩm có ID: " + id, "404"));
            MapperUtils.mapToExisting(dto, form);
        }

        formRepository.save(form);
        log.info("✅ Saved form: {}", form.getName());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy form sản phẩm có ID: " + id, "404"));
        
        formRepository.delete(form); // Xóa cứng vì entity không có status
        log.info("🗑️ Deleted form ID: {} - {}", id, form.getName());
    }

    @Override
    public FormResponse getById(Integer id) {
        return formRepository.findById(id)
                .map(FormResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy form sản phẩm có ID: " + id, "404"));
    }

    @Override
    public List<FormResponse> getAll() {
        return formRepository.findAllByOrderByNameAsc().stream()
                .map(FormResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== TÌM KIẾM CƠ BẢN ====================

    @Override
    public FormResponse getByName(String name) {
        return formRepository.findByName(name)
                .map(FormResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy form sản phẩm với tên: " + name, "404"));
    }

    @Override
    public List<FormResponse> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        
        return formRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(FormResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== KIỂM TRA ====================

    @Override
    public boolean existsByName(String name) {
        return formRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return formRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }
}