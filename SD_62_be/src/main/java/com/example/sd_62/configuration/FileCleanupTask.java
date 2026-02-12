package com.example.sd_62.configuration;

import com.example.sd_62.product.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileCleanupTask {

    private final ProductImageRepository productImageRepository;
    
    @Value("${file.upload-dir}")
    private String uploadDir;

    // Chạy lúc 2h sáng mỗi ngày
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupOrphanFiles() {
        log.info("🧹 Starting file cleanup task...");
        long startTime = System.currentTimeMillis();
        
        try {
            // 1. Lấy tất cả imageUrl từ database
            List<String> validPaths = productImageRepository.findAllImageUrls();
            
            // 2. Duyệt thư mục uploads
            Path productsPath = Paths.get(uploadDir, "products");
            if (!Files.exists(productsPath)) {
                log.info("📁 Upload directory does not exist: {}", productsPath);
                return;
            }
            
            final int[] deletedCount = {0};
            
            Files.walk(productsPath)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        String relativePath = "/uploads/products/" + 
                                productsPath.relativize(path).toString().replace("\\", "/");
                        
                        if (!validPaths.contains(relativePath)) {
                            try {
                                Files.deleteIfExists(path);
                                deletedCount[0]++;
                                log.debug("🗑️ Deleted orphan file: {}", path);
                            } catch (IOException e) {
                                log.error("Cannot delete file: {}", path);
                            }
                        }
                    });
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("✅ File cleanup completed: {} files deleted in {}ms", deletedCount[0], duration);
            
        } catch (IOException e) {
            log.error("❌ Error during cleanup: {}", e.getMessage());
        }
    }
    
    // Xóa thư mục variant không còn trong database
    @Scheduled(cron = "0 30 2 * * ?") // 2:30 sáng
    public void cleanupEmptyDirectories() {
        log.info("🧹 Starting empty directory cleanup...");
        
        try {
            Path productsPath = Paths.get(uploadDir, "products");
            if (!Files.exists(productsPath)) return;
            
            // Lấy tất cả variant ID từ database
            List<Integer> validVariantIds = productImageRepository.findAll().stream()
                    .map(pi -> pi.getProductVariant().getId())
                    .distinct()
                    .collect(Collectors.toList());
            
            Files.list(productsPath)
                    .filter(Files::isDirectory)
                    .forEach(dir -> {
                        String dirName = dir.getFileName().toString();
                        try {
                            int variantId = Integer.parseInt(dirName);
                            if (!validVariantIds.contains(variantId)) {
                                // Kiểm tra thư mục rỗng
                                try (var stream = Files.list(dir)) {
                                    if (stream.findAny().isEmpty()) {
                                        Files.delete(dir);
                                        log.info("🗑️ Deleted empty directory: {}", dir);
                                    }
                                }
                            }
                        } catch (NumberFormatException | IOException e) {
                            // Ignore
                        }
                    });
            
        } catch (IOException e) {
            log.error("❌ Error during directory cleanup: {}", e.getMessage());
        }
    }
}