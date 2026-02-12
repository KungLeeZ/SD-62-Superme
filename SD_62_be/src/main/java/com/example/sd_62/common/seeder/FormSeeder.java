package com.example.sd_62.common.seeder;

import com.example.sd_62.product.entity.Form;
import com.example.sd_62.product.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FormSeeder implements BaseSeeder {

    private final FormRepository formRepository;

    @Override
    public void seed() {
        if (formRepository.count() == 0) {
            log.info("🔄 Seeding forms...");
            
            List<Form> forms = Arrays.asList(
                Form.builder()
                    .name("Sneakers")
                    .description("Giày thể thao đa dụng, phù hợp mọi hoạt động")
                    .build(),
                Form.builder()
                    .name("Running")
                    .description("Giày chạy bộ - thiết kế nhẹ, đàn hồi tốt")
                    .build(),
                Form.builder()
                    .name("Basketball")
                    .description("Giày bóng rổ - cao cổ, hỗ trợ cổ chân")
                    .build(),
                Form.builder()
                    .name("Sandals")
                    .description("Dép quai ngang - thoáng mát, dễ đi")
                    .build(),
                Form.builder()
                    .name("Slip-on")
                    .description("Xỏ lười - tiện lợi, không dây")
                    .build(),
                Form.builder()
                    .name("High-top")
                    .description("Cổ cao - thời trang, bảo vệ cổ chân")
                    .build(),
                Form.builder()
                    .name("Low-top")
                    .description("Cổ thấp - nhẹ nhàng, linh hoạt")
                    .build(),
                Form.builder()
                    .name("Boots")
                    .description("Bốt - ấm áp, phong cách")
                    .build(),
                Form.builder()
                    .name("Oxford")
                    .description("Giày tây công sở - lịch lãm")
                    .build(),
                Form.builder()
                    .name("Loafers")
                    .description("Giày lười công sở - thanh lịch")
                    .build()
            );
            
            formRepository.saveAll(forms);
            log.info("✅ Seeded {} forms", forms.size());
        } else {
            log.info("⚠️ Forms already exist ({} records), skipping...", formRepository.count());
        }
    }

    @Override
    public int getOrder() {
        return 3;
    }
}