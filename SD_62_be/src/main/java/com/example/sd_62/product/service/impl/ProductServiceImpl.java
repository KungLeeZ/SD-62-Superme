package com.example.sd_62.product.service.impl;

import com.example.sd_62.common.exception.ApiException;
import com.example.sd_62.common.util.MapperUtils;
import com.example.sd_62.product.dto.request.ProductRequest;
import com.example.sd_62.product.dto.request.ProductSearchRequest;
import com.example.sd_62.product.dto.response.ProductResponse;
import com.example.sd_62.product.entity.*;
import com.example.sd_62.product.enums.ProductStatus;
import com.example.sd_62.product.repository.*;
import com.example.sd_62.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final GenderRepository genderRepository;
    private final MaterialRepository materialRepository;
    private final FormRepository formRepository;
    private final ProductReleaseRepository productReleaseRepository;

    @Transactional
    @Override
    public void save(Integer id, ProductRequest dto) {
        // Kiểm tra trùng productCode - sử dụng method từ repository
        if (id == null) {
            if (productRepository.existsByProductCodeIgnoreCase(dto.getProductCode())) {
                throw new ApiException("Mã sản phẩm đã tồn tại", "409");
            }
        } else {
            if (productRepository.existsByProductCodeIgnoreCaseAndIdNot(dto.getProductCode(), id)) {
                throw new ApiException("Mã sản phẩm đã tồn tại", "409");
            }
        }

        Product model;
        if (id == null) {
            model = MapperUtils.map(dto, Product.class);
            model.setCreatedAt(LocalDateTime.now());
            model.setStatus(ProductStatus.ACTIVE);
        } else {
            model = productRepository.findById(id)
                    .orElseThrow(() -> new ApiException("Không tìm thấy sản phẩm có ID: " + id, "404"));
            MapperUtils.mapToExisting(dto, model);
            model.setUpdatedAt(LocalDateTime.now());
        }

        // Gán các mối quan hệ
        setRelationships(model, dto);

        productRepository.save(model);
    }

    private void setRelationships(Product model, ProductRequest dto) {
        if (dto.getBrandId() != null) {
            model.setBrand(brandRepository.findById(dto.getBrandId())
                    .orElseThrow(() -> new ApiException("Không tìm thấy thương hiệu", "404")));
        }

        if (dto.getGenderId() != null) {
            model.setGender(genderRepository.findById(dto.getGenderId())
                    .orElseThrow(() -> new ApiException("Không tìm thấy giới tính", "404")));
        }

        if (dto.getMaterialId() != null) {
            model.setMaterial(materialRepository.findById(dto.getMaterialId())
                    .orElseThrow(() -> new ApiException("Không tìm thấy chất liệu", "404")));
        }

        if (dto.getFormId() != null) {
            model.setForm(formRepository.findById(dto.getFormId())
                    .orElseThrow(() -> new ApiException("Không tìm thấy form sản phẩm", "404")));
        }

        if (dto.getProductReleaseId() != null) {
            model.setProductRelease(productReleaseRepository.findById(dto.getProductReleaseId())
                    .orElseThrow(() -> new ApiException("Không tìm thấy đợt phát hành", "404")));
        }
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy sản phẩm có ID: " + id, "404"));

        if (product.getStatus() == ProductStatus.INACTIVE) {
            throw new ApiException("Sản phẩm đã ở trạng thái INACTIVE", "400");
        }

        product.setStatus(ProductStatus.INACTIVE);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        log.info("Đã xóa mềm sản phẩm ID: {}", id);
    }

    @Transactional
    @Override
    public void restore(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy sản phẩm có ID: " + id, "404"));

        if (product.getStatus() == ProductStatus.ACTIVE) {
            throw new ApiException("Sản phẩm đang ở trạng thái ACTIVE", "400");
        }

        product.setStatus(ProductStatus.ACTIVE);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        log.info("Đã khôi phục sản phẩm ID: {}", id);
    }

    @Override
    public ProductResponse getById(Integer id) {
        return productRepository.findById(id)
                .map(ProductResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy sản phẩm có ID: " + id, "404"));
    }

    @Override
    public List<ProductResponse> getAll(String status) {
        List<Product> products;

        if (status != null && !status.isEmpty()) {
            try {
                ProductStatus productStatus = ProductStatus.valueOf(status.toUpperCase());
                products = productRepository.findByStatus(productStatus, Pageable.unpaged()).getContent();
            } catch (IllegalArgumentException e) {
                throw new ApiException("Status không hợp lệ: " + status, "400");
            }
        } else {
            products = productRepository.findAll();
        }

        return products.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductResponse> getAllPaging(String status, Pageable pageable) {
        Page<Product> productPage;

        if (status != null && !status.isEmpty()) {
            try {
                ProductStatus productStatus = ProductStatus.valueOf(status.toUpperCase());
                productPage = productRepository.findByStatus(productStatus, pageable);
            } catch (IllegalArgumentException e) {
                throw new ApiException("Status không hợp lệ: " + status, "400");
            }
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return productPage.map(ProductResponse::new);
    }

    @Override
    public Page<ProductResponse> searchProducts(ProductSearchRequest searchRequest, Pageable pageable) {
        // Xử lý sort nếu có trong searchRequest
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            Sort.Direction direction = Sort.Direction.ASC;
            if ("DESC".equalsIgnoreCase(searchRequest.getSortDirection())) {
                direction = Sort.Direction.DESC;
            }
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(direction, searchRequest.getSortBy())
            );
        }

        Page<Product> productPage = productRepository.searchProducts(
                searchRequest.getKeyword(),
                searchRequest.getBrandId(),
                searchRequest.getGenderId(),
                searchRequest.getMaterialId(),
                searchRequest.getFormId(),
                searchRequest.getProductReleaseId(),
                searchRequest.getStatus(),
                searchRequest.getFromDate(),
                searchRequest.getToDate(),
                pageable
        );

        return productPage.map(ProductResponse::new);
    }

    @Override
    public List<ProductResponse> searchProducts(ProductSearchRequest searchRequest) {
        Page<Product> productPage = productRepository.searchProducts(
                searchRequest.getKeyword(),
                searchRequest.getBrandId(),
                searchRequest.getGenderId(),
                searchRequest.getMaterialId(),
                searchRequest.getFormId(),
                searchRequest.getProductReleaseId(),
                searchRequest.getStatus(),
                searchRequest.getFromDate(),
                searchRequest.getToDate(),
                Pageable.unpaged()
        );

        return productPage.getContent().stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByProductCode(String productCode) {
        return productRepository.existsByProductCodeIgnoreCase(productCode);
    }

    @Override
    public boolean existsByProductCodeAndIdNot(String productCode, Integer id) {
        return productRepository.existsByProductCodeIgnoreCaseAndIdNot(productCode, id);
    }

    @Override
    public long countByStatus(String status) {
        try {
            ProductStatus productStatus = ProductStatus.valueOf(status.toUpperCase());
            return productRepository.countByStatus(productStatus);
        } catch (IllegalArgumentException e) {
            throw new ApiException("Status không hợp lệ: " + status, "400");
        }
    }
}