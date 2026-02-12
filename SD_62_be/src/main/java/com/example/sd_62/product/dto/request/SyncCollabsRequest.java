package com.example.sd_62.product.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SyncCollabsRequest {

    @NotNull(message = "ID sản phẩm không được để trống")
    private Integer productId;
    
    private List<Integer> collabIds;
}