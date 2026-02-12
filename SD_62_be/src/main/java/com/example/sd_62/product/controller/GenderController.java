package com.example.sd_62.product.controller;

import com.example.sd_62.product.dto.response.ApiResponse;
import com.example.sd_62.product.dto.response.GenderResponse;
import com.example.sd_62.product.service.GenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genders")
@RequiredArgsConstructor
@Tag(name = "Gender", description = "API danh mục giới tính (CỐ ĐỊNH)")
public class GenderController {

    private final GenderService genderService;

    @GetMapping
    @Operation(summary = "Lấy tất cả giới tính")
    public ResponseEntity<ApiResponse<List<GenderResponse>>> getAllGenders() {
        List<GenderResponse> responses = genderService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách giới tính thành công", responses));
    }
}