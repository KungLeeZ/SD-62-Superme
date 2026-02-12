package com.example.sd_62.product.dto.response;

import com.example.sd_62.product.entity.ProductRelease;
import com.example.sd_62.product.enums.DropStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ProductReleaseResponse {

    private Integer id;
    private String code;
    private String name;
    
    // Season info
    private Integer seasonId;
    private String seasonName;
    private String seasonCode;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String startDateFormatted;
    private String endDateFormatted;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private DropStatus status;
    private String statusName;
    
    // Trạng thái hiện tại
    private String currentStatus;

    public ProductReleaseResponse(ProductRelease release) {
        this.id = release.getId();
        this.code = release.getCode();
        this.name = release.getName();
        
        if (release.getSeason() != null) {
            this.seasonId = release.getSeason().getId();
            this.seasonName = release.getSeason().getName();
            this.seasonCode = release.getSeason().getSeasonCode();
        }
        
        this.startDate = release.getStartDate();
        this.endDate = release.getEndDate();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.startDateFormatted = release.getStartDate() != null ? release.getStartDate().format(formatter) : null;
        this.endDateFormatted = release.getEndDate() != null ? release.getEndDate().format(formatter) : null;
        
        this.createdAt = release.getCreatedAt();
        this.updatedAt = release.getUpdatedAt();
        this.status = release.getStatus();
        this.statusName = release.getStatus() != null ? release.getStatus().name() : null;
        
        // Xác định trạng thái hiện tại
        LocalDateTime now = LocalDateTime.now();
        if (release.getStatus() == DropStatus.ENDED) {
            this.currentStatus = "Đã đóng";
        } else if (release.getStartDate() != null && release.getStartDate().isAfter(now) || release.getStatus() == DropStatus.UPCOMING) {
            this.currentStatus = "Sắp diễn ra";
        } else if (release.getEndDate() != null && release.getEndDate().isBefore(now) || release.getStatus() == DropStatus.ENDED) {
            this.currentStatus = "Đã kết thúc";
        } else if (release.getStartDate() != null && release.getEndDate() != null &&
                   release.getStartDate().isBefore(now) && release.getEndDate().isAfter(now)
                || release.getStatus() == DropStatus.ACTIVE) {
            this.currentStatus = "Đang diễn ra";
        } else {
            this.currentStatus = "Chưa xác định";
        }
    }
}