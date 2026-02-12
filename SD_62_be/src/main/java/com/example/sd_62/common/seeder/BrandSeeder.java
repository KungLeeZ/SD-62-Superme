package com.example.sd_62.common.seeder;

import com.example.sd_62.product.entity.Brand;
import com.example.sd_62.product.enums.BrandStatus;
import com.example.sd_62.product.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BrandSeeder implements BaseSeeder {

    private final BrandRepository brandRepository;

    @Override
    public void seed() {
        if (brandRepository.count() == 0) {
            log.info("🔄 Seeding brands...");
            
            List<Brand> brands = Arrays.asList(
                Brand.builder()
                    .code("ADIDAS")
                    .name("Adidas")
                    .description("Thương hiệu thể thao Đức")
                    .status(BrandStatus.ACTIVE)
                    .build(),
                Brand.builder()
                    .code("NIKE")
                    .name("Nike")
                    .description("Thương hiệu thể thao Mỹ")
                    .status(BrandStatus.ACTIVE)
                    .build(),
                Brand.builder()
                    .code("PUMA")
                    .name("Puma")
                    .description("Thương hiệu thể thao Đức")
                    .status(BrandStatus.ACTIVE)
                    .build(),
                Brand.builder()
                    .code("CONVERSE")
                    .name("Converse")
                    .description("Thương hiệu giày Mỹ")
                    .status(BrandStatus.ACTIVE)
                    .build(),
                Brand.builder()
                    .code("VANS")
                    .name("Vans")
                    .description("Thương hiệu giày trượt")
                    .status(BrandStatus.ACTIVE)
                    .build()
            );
            
            brandRepository.saveAll(brands);
            log.info("✅ Seeded {} brands", brands.size());
        } else {
            log.info("⚠️ Brands already exist, skipping...");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}