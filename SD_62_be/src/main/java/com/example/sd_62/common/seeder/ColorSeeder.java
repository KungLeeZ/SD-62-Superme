package com.example.sd_62.common.seeder;

import com.example.sd_62.product.entity.Color;
import com.example.sd_62.product.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ColorSeeder implements BaseSeeder {

    private final ColorRepository colorRepository;

    @Override
    public void seed() {
        if (colorRepository.count() == 0) {
            log.info("🔄 Seeding colors...");
            
            List<Color> colors = Arrays.asList(
                // Basic colors
                Color.builder()
                    .name("Black")
                    .hex("#000000")
                    .description("Màu đen - sang trọng, dễ phối đồ")
                    .build(),
                Color.builder()
                    .name("White")
                    .hex("#FFFFFF")
                    .description("Màu trắng - tinh khiết, thanh lịch")
                    .build(),
                Color.builder()
                    .name("Red")
                    .hex("#FF0000")
                    .description("Màu đỏ - nổi bật, cá tính")
                    .build(),
                Color.builder()
                    .name("Blue")
                    .hex("#0000FF")
                    .description("Màu xanh dương - trầm lắng, lịch lãm")
                    .build(),
                Color.builder()
                    .name("Green")
                    .hex("#008000")
                    .description("Màu xanh lá - tươi mới, tự nhiên")
                    .build(),
                Color.builder()
                    .name("Gray")
                    .hex("#808080")
                    .description("Màu xám - trung tính, hiện đại")
                    .build(),
                Color.builder()
                    .name("Yellow")
                    .hex("#FFD700")
                    .description("Màu vàng - tươi sáng, năng động")
                    .build(),
                
                // Additional colors
                Color.builder()
                    .name("Brown")
                    .hex("#8B4513")
                    .description("Màu nâu - ấm áp, cổ điển")
                    .build(),
                Color.builder()
                    .name("Pink")
                    .hex("#FFC0CB")
                    .description("Màu hồng - dễ thương, nhẹ nhàng")
                    .build(),
                Color.builder()
                    .name("Purple")
                    .hex("#800080")
                    .description("Màu tím - huyền bí, quý phái")
                    .build(),
                Color.builder()
                    .name("Orange")
                    .hex("#FFA500")
                    .description("Màu cam - nhiệt huyết, sôi động")
                    .build(),
                Color.builder()
                    .name("Navy")
                    .hex("#000080")
                    .description("Màu xanh than - lịch sự, trang nhã")
                    .build(),
                Color.builder()
                    .name("Beige")
                    .hex("#F5F5DC")
                    .description("Màu be - nhẹ nhàng, thanh lịch")
                    .build(),
                Color.builder()
                    .name("Silver")
                    .hex("#C0C0C0")
                    .description("Màu bạc - hiện đại, tương lai")
                    .build(),
                Color.builder()
                    .name("Gold")
                    .hex("#FFD700")
                    .description("Màu vàng kim - sang trọng, đẳng cấp")
                    .build(),
                
                // Limited/Seasonal colors
                Color.builder()
                    .name("Coral")
                    .hex("#FF7F50")
                    .description("Màu san hô - xu hướng mùa hè")
                    .build(),
                Color.builder()
                    .name("Mint")
                    .hex("#98FB98")
                    .description("Màu bạc hà - tươi mát")
                    .build(),
                Color.builder()
                    .name("Lavender")
                    .hex("#E6E6FA")
                    .description("Màu oải hương - lãng mạn")
                    .build()
            );
            
            colorRepository.saveAll(colors);
            log.info("✅ Seeded {} colors", colors.size());
            
        } else {
            log.info("⚠️ Colors already exist ({} records), skipping...", colorRepository.count());
        }
    }

    @Override
    public int getOrder() {
        return 8; // Sau SizeSeeder
    }
}