package com.example.sd_62.common.seeder;

import com.example.sd_62.product.entity.Gender;
import com.example.sd_62.product.repository.GenderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenderSeeder implements BaseSeeder {

    private final GenderRepository genderRepository;

    @Override
    public void seed() {
        if (genderRepository.count() == 0) {
            log.info("🔄 Seeding genders...");
            
            List<Gender> genders = Arrays.asList(
                Gender.builder()
                    .name("Nam")
                    .build(),
                Gender.builder()
                    .name("Nữ")
                    .build(),
                Gender.builder()
                    .name("Unisex")
                    .description("Phù hợp cho cả nam và nữ")
                    .build(),
                Gender.builder()
                    .name("Trẻ em Nam")
                    .description("Giày cho bé trai")
                    .build(),
                Gender.builder()
                    .name("Trẻ em Nữ")
                    .description("Giày cho bé gái")
                    .build(),
                Gender.builder()
                    .name("Trẻ sơ sinh")
                    .description("Giày cho trẻ sơ sinh")
                    .build()
            );
            
            genderRepository.saveAll(genders);
            log.info("✅ Seeded {} genders", genders.size());
        } else {
            log.info("⚠️ Genders already exist ({} records), skipping...", genderRepository.count());
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}