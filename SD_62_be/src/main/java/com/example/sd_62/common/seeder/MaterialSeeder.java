package com.example.sd_62.common.seeder;

import com.example.sd_62.product.entity.Material;
import com.example.sd_62.product.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MaterialSeeder implements BaseSeeder {

    private final MaterialRepository materialRepository;

    @Override
    public void seed() {
        if (materialRepository.count() == 0) {
            log.info("🔄 Seeding materials...");
            
            List<Material> materials = Arrays.asList(
                Material.builder()
                    .name("Canvas")
                    .description("Vải bố - bền, thoáng khí, dễ vệ sinh")
                    .build(),
                Material.builder()
                    .name("Leather")
                    .description("Da thật - cao cấp, bền, sang trọng")
                    .build(),
                Material.builder()
                    .name("Synthetic Leather")
                    .description("Da công nghiệp - giá tốt, dễ bảo quản")
                    .build(),
                Material.builder()
                    .name("Mesh")
                    .description("Lưới - siêu nhẹ, thoáng khí tối đa")
                    .build(),
                Material.builder()
                    .name("Suede")
                    .description("Da lộn - mềm mại, sang trọng")
                    .build(),
                Material.builder()
                    .name("Knit")
                    .description("Đan - co giãn, ôm chân")
                    .build(),
                Material.builder()
                    .name("Nylon")
                    .description("Nylon - nhẹ, nhanh khô")
                    .build(),
                Material.builder()
                    .name("Rubber")
                    .description("Cao su - đế giày, chống trơn")
                    .build(),
                Material.builder()
                    .name("EVA")
                    .description("Nhựa EVA - đế giày siêu nhẹ")
                    .build(),
                Material.builder()
                    .name("Gore-Tex")
                    .description("Vải chống nước - thoáng khí, chống thấm")
                    .build()
            );
            
            materialRepository.saveAll(materials);
            log.info("✅ Seeded {} materials", materials.size());
        } else {
            log.info("⚠️ Materials already exist ({} records), skipping...", materialRepository.count());
        }
    }

    @Override
    public int getOrder() {
        return 4;
    }
}