package com.example.sd_62.common.seeder;

import com.example.sd_62.product.entity.Season;
import com.example.sd_62.product.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeasonSeeder implements BaseSeeder {

    private final SeasonRepository seasonRepository;

    @Override
    public void seed() {
        if (seasonRepository.count() == 0) {
            log.info("🔄 Seeding seasons...");
            
            int currentYear = Year.now().getValue();
            
            List<Season> seasons = Arrays.asList(
                // Quá khứ
                Season.builder()
                    .seasonCode("SS2023")
                    .name("Xuân Hè 2023")
                    .year(2023)
                    .description("Bộ sưu tập Xuân Hè 2023")
                    .build(),
                Season.builder()
                    .seasonCode("FW2023")
                    .name("Thu Đông 2023")
                    .year(2023)
                    .description("Bộ sưu tập Thu Đông 2023")
                    .build(),
                
                // Hiện tại
                Season.builder()
                    .seasonCode("SS" + currentYear)
                    .name("Xuân Hè " + currentYear)
                    .year(currentYear)
                    .description("Bộ sưu tập Xuân Hè " + currentYear)
                    .build(),
                Season.builder()
                    .seasonCode("FW" + currentYear)
                    .name("Thu Đông " + currentYear)
                    .year(currentYear)
                    .description("Bộ sưu tập Thu Đông " + currentYear)
                    .build(),
                
                // Tương lai
                Season.builder()
                    .seasonCode("SS" + (currentYear + 1))
                    .name("Xuân Hè " + (currentYear + 1))
                    .year(currentYear + 1)
                    .description("Bộ sưu tập Xuân Hè " + (currentYear + 1))
                    .build(),
                Season.builder()
                    .seasonCode("FW" + (currentYear + 1))
                    .name("Thu Đông " + (currentYear + 1))
                    .year(currentYear + 1)
                    .description("Bộ sưu tập Thu Đông " + (currentYear + 1))
                    .build(),
                
                // Limited Edition
                Season.builder()
                    .seasonCode("LE2024")
                    .name("Limited Edition 2024")
                    .year(2024)
                    .description("Phiên bản giới hạn 2024")
                    .build(),
                Season.builder()
                    .seasonCode("ANNIV10")
                    .name("Kỷ niệm 10 năm")
                    .year(2024)
                    .description("Bộ sưu tập kỷ niệm 10 năm thành lập")
                    .build()
            );
            
            seasonRepository.saveAll(seasons);
            log.info("✅ Seeded {} seasons", seasons.size());
        } else {
            log.info("⚠️ Seasons already exist ({} records), skipping...", seasonRepository.count());
        }
    }

    @Override
    public int getOrder() {
        return 5;
    }
}