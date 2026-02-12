package com.example.sd_62.product.service.impl;

import com.example.sd_62.common.exception.ApiException;
import com.example.sd_62.product.dto.response.ProductImageResponse;
import com.example.sd_62.product.entity.ProductImage;
import com.example.sd_62.product.entity.ProductVariant;
import com.example.sd_62.product.repository.ProductImageRepository;
import com.example.sd_62.product.repository.ProductVariantRepository;
import com.example.sd_62.product.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductVariantRepository productVariantRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    // ==================== UPLOAD ====================

    @Override
    @Transactional
    public ProductImageResponse uploadImage(Integer variantId, MultipartFile file, Integer sortOrder) {
        // 1. Kiểm tra variant
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ApiException("Không tìm thấy biến thể sản phẩm với ID: " + variantId, "404"));

        // 2. Kiểm tra file
        validateFile(file);

        try {
            // 3. Tạo tên file duy nhất
            String fileName = generateFileName(file);
            
            // 4. Tạo đường dẫn
            Path uploadPath = Paths.get(uploadDir, "products", variantId.toString());
            Path filePath = uploadPath.resolve(fileName);
            
            // 5. Tạo thư mục nếu chưa tồn tại
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 6. Lưu file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("💾 File saved: {}", filePath);
            
            // 7. Tạo URL
            String imageUrl = "/uploads/products/" + variantId + "/" + fileName;
            
            // 8. Xác định sortOrder
            if (sortOrder == null) {
                Integer maxSortOrder = productImageRepository.findMaxSortOrderByVariantId(variantId);
                sortOrder = (maxSortOrder == null) ? 0 : maxSortOrder + 1;
            }
            
            // 9. Lưu database
            ProductImage productImage = ProductImage.builder()
                    .productVariant(variant)
                    .imageUrl(imageUrl)
                    .sortOrder(sortOrder)
                    .build();
            
            productImage = productImageRepository.save(productImage);
            log.info("✅ Uploaded image for variant {}: {}", variantId, fileName);
            
            return new ProductImageResponse(productImage);
            
        } catch (IOException e) {
            log.error("❌ Cannot upload file: {}", e.getMessage());
            throw new ApiException("Không thể upload file: " + e.getMessage(), "500");
        }
    }

    @Override
    @Transactional
    public List<ProductImageResponse> uploadMultipleImages(Integer variantId, List<MultipartFile> files) {
        List<ProductImageResponse> responses = new ArrayList<>();
        List<Path> uploadedFiles = new ArrayList<>();
        List<ProductImage> savedImages = new ArrayList<>();
        
        try {
            // Kiểm tra variant tồn tại
            ProductVariant variant = productVariantRepository.findById(variantId)
                    .orElseThrow(() -> new ApiException("Không tìm thấy biến thể sản phẩm với ID: " + variantId, "404"));
            
            Integer currentSortOrder = productImageRepository.findMaxSortOrderByVariantId(variantId);
            currentSortOrder = (currentSortOrder == null) ? 0 : currentSortOrder + 1;
            
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                
                // Validate file
                validateFile(file);
                
                // Generate filename
                String fileName = generateFileName(file);
                Path uploadPath = Paths.get(uploadDir, "products", variantId.toString());
                Path filePath = uploadPath.resolve(fileName);
                
                // Tạo thư mục
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                // Lưu file
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                uploadedFiles.add(filePath);
                
                // Tạo URL
                String imageUrl = "/uploads/products/" + variantId + "/" + fileName;
                
                // Lưu database
                ProductImage productImage = ProductImage.builder()
                        .productVariant(variant)
                        .imageUrl(imageUrl)
                        .sortOrder(currentSortOrder++)
                        .build();
                
                savedImages.add(productImageRepository.save(productImage));
            }
            
            // Tạo response
            responses = savedImages.stream()
                    .map(ProductImageResponse::new)
                    .collect(Collectors.toList());
            
            log.info("✅ Uploaded {} images for variant {}", responses.size(), variantId);
            return responses;
            
        } catch (Exception e) {
            // Rollback: Xóa các file đã upload
            for (Path path : uploadedFiles) {
                try {
                    Files.deleteIfExists(path);
                    log.info("🗑️ Rollback deleted file: {}", path);
                } catch (IOException ex) {
                    log.error("Cannot delete file during rollback: {}", path);
                }
            }
            log.error("❌ Upload failed: {}", e.getMessage());
            throw new ApiException("Upload thất bại: " + e.getMessage(), "500");
        }
    }

    // ==================== GET ====================

    @Override
    public ProductImageResponse getImageById(Integer imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ApiException("Không tìm thấy ảnh với ID: " + imageId, "404"));
        return new ProductImageResponse(image);
    }

    @Override
    public List<ProductImageResponse> getImagesByVariantId(Integer variantId) {
        if (!productVariantRepository.existsById(variantId)) {
            throw new ApiException("Không tìm thấy biến thể sản phẩm với ID: " + variantId, "404");
        }
        
        return productImageRepository.findByVariantId(variantId).stream()
                .map(ProductImageResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== UPDATE ====================

    @Override
    @Transactional
    public ProductImageResponse updateSortOrder(Integer imageId, Integer sortOrder) {
        if (sortOrder < 0) {
            throw new ApiException("Thứ tự không được âm", "400");
        }
        
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ApiException("Không tìm thấy ảnh với ID: " + imageId, "404"));
        
        image.setSortOrder(sortOrder);
        image = productImageRepository.save(image);
        
        log.info("🔄 Updated sort order for image {}: {}", imageId, sortOrder);
        return new ProductImageResponse(image);
    }

    @Override
    @Transactional
    public ProductImageResponse updateImageUrl(Integer imageId, String newImageUrl) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ApiException("Không tìm thấy ảnh với ID: " + imageId, "404"));
        
        // Xóa file cũ
        try {
            String oldImageUrl = image.getImageUrl();
            if (oldImageUrl != null && oldImageUrl.startsWith("/uploads/")) {
                Path oldPath = Paths.get(uploadDir, oldImageUrl.replace("/uploads/", ""));
                Files.deleteIfExists(oldPath);
                log.info("🗑️ Deleted old file: {}", oldPath);
            }
        } catch (IOException e) {
            log.error("Cannot delete old file: {}", e.getMessage());
        }
        
        image.setImageUrl(newImageUrl);
        image = productImageRepository.save(image);
        
        log.info("🔄 Updated image URL for image {}: {}", imageId, newImageUrl);
        return new ProductImageResponse(image);
    }

    // ==================== DELETE ====================

    @Override
    @Transactional
    public void deleteImage(Integer imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ApiException("Không tìm thấy ảnh với ID: " + imageId, "404"));
        
        // Xóa file vật lý
        try {
            String imageUrl = image.getImageUrl();
            if (imageUrl != null && imageUrl.startsWith("/uploads/")) {
                Path filePath = Paths.get(uploadDir, imageUrl.replace("/uploads/", ""));
                Files.deleteIfExists(filePath);
                log.info("🗑️ Deleted file: {}", filePath);
            }
        } catch (IOException e) {
            log.error("Cannot delete file: {}", e.getMessage());
        }
        
        // Xóa database
        productImageRepository.delete(image);
        log.info("🗑️ Deleted image ID: {}", imageId);
    }

    @Override
    @Transactional
    public void deleteAllImagesByVariantId(Integer variantId) {
        if (!productVariantRepository.existsById(variantId)) {
            throw new ApiException("Không tìm thấy biến thể sản phẩm với ID: " + variantId, "404");
        }
        
        // Xóa tất cả file trong thư mục
        try {
            Path variantUploadPath = Paths.get(uploadDir, "products", variantId.toString());
            if (Files.exists(variantUploadPath)) {
                Files.walk(variantUploadPath)
                        .sorted((a, b) -> -a.compareTo(b)) // Xóa file trước, folder sau
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                                log.info("🗑️ Deleted: {}", path);
                            } catch (IOException e) {
                                log.error("Cannot delete: {}", path);
                            }
                        });
                log.info("🗑️ Deleted directory: {}", variantUploadPath);
            }
        } catch (IOException e) {
            log.error("Cannot delete directory: {}", e.getMessage());
        }
        
        // Xóa database
        productImageRepository.deleteByVariantId(variantId);
        log.info("🗑️ Deleted all images for variant ID: {}", variantId);
    }

    @Override
    @Transactional
    public void deleteImagesByIds(List<Integer> imageIds) {
        for (Integer imageId : imageIds) {
            try {
                deleteImage(imageId);
            } catch (Exception e) {
                log.error("Cannot delete image ID {}: {}", imageId, e.getMessage());
            }
        }
        log.info("🗑️ Deleted {} images", imageIds.size());
    }

    // ==================== VALIDATE & UTILITY ====================

    @Override
    public boolean isImageBelongToVariant(Integer imageId, Integer variantId) {
        return productImageRepository.existsByIdAndProductVariantId(imageId, variantId);
    }

    @Override
    public long countImagesByVariantId(Integer variantId) {
        return productImageRepository.countByProductVariantId(variantId);
    }

    @Override
    public String getImagePath(String imageUrl) {
        if (imageUrl != null && imageUrl.startsWith("/uploads/")) {
            return imageUrl.replace("/uploads/", "");
        }
        return null;
    }

    // ==================== PRIVATE METHODS ====================

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApiException("File không được để trống", "400");
        }
        
        // Kiểm tra content type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ApiException("Chỉ được upload file ảnh", "400");
        }
        
        // Kiểm tra extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            List<String> allowedExtensions = List.of(".jpg", ".jpeg", ".png", ".gif", ".webp");
            if (!allowedExtensions.contains(extension)) {
                throw new ApiException("Định dạng file không được hỗ trợ. Chỉ hỗ trợ: " + allowedExtensions, "400");
            }
        }
        
        // Kiểm tra dung lượng (mặc định 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new ApiException("File không được vượt quá 5MB", "400");
        }
    }

    private String generateFileName(MultipartFile file) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        return String.format("%s_%s%s", timestamp, uuid, extension);
    }
}