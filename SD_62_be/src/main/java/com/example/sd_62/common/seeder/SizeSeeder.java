package com.example.sd_62.common.seeder;

import com.example.sd_62.product.entity.Size;
import com.example.sd_62.product.repository.SizeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SizeSeeder implements BaseSeeder {

    private final SizeRepository sizeRepository;

    @Override
    public void seed() {
        if (sizeRepository.count() == 0) {
            log.info("🔄 Seeding sizes...");

            List<Size> sizes = Arrays.asList(
                createSize(21.0, 4.0, 5.0, 35, 21.0),
                createSize(21.5, 4.5, 5.5, 35.5, 21.5),
                createSize(22.0, 5.0, 6.0, 36, 22.0),
                createSize(22.5, 5.5, 6.5, 36.5, 22.5),
                createSize(23.0, 6.0, 7.0, 37, 23.0),
                createSize(23.5, 6.5, 7.5, 37.5, 23.5),
                createSize(24.0, 7.0, 8.0, 38, 24.0),
                createSize(24.5, 7.5, 8.5, 38.5, 24.5),
                createSize(25.0, 7.0, 8.0, 40, 25.0),
                createSize(25.5, 7.5, 8.5, 40.5, 25.5),
                createSize(26.0, 8.0, 9.0, 41, 26.0),
                createSize(26.5, 8.5, 9.5, 42, 26.5),
                createSize(27.0, 9.0, 10.0, 42.5, 27.0),
                createSize(27.5, 9.5, 10.5, 43, 27.5),
                createSize(28.0, 10.0, 11.0, 44, 28.0),
                createSize(28.5, 10.5, 11.5, 44.5, 28.5),
                createSize(29.0, 11.0, 12.0, 45, 29.0),
                createSize(29.5, 11.5, 12.5, 45.5, 29.5),
                createSize(30.0, 12.0, 13.0, 46, 30.0)
            );

            sizeRepository.saveAll(sizes);
            log.info("✅ Seeded {} sizes", sizes.size());
        } else {
            log.info("⚠️ Sizes already exist, skipping...");
        }
    }

    private Size createSize(double jp, double usMen, double usWomen, double eu, double footLength) {
        return Size.builder()
                .jp(BigDecimal.valueOf(jp))
                .usMen(BigDecimal.valueOf(usMen))
                .usWomen(BigDecimal.valueOf(usWomen))
                .eu(BigDecimal.valueOf(eu))
                .footLength(BigDecimal.valueOf(footLength))
                .build();
    }

    @Override
    public int getOrder() {
        return 7;
    }
}