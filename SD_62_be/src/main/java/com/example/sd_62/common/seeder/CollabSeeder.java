package com.example.sd_62.common.seeder;

import com.example.sd_62.product.dto.request.CollabRequest;
import com.example.sd_62.product.service.CollabService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CollabSeeder implements BaseSeeder {

    private final CollabService collabService;

    @Override
    public void seed() {
        if (collabService.getAll().isEmpty()) {
            log.info("🔄 Seeding collabs...");

            List<CollabRequest> collabs = Arrays.asList(
                createRequest("Adidas x Parley", "Environmental", 2023),
                createRequest("Nike x Off-White", "Fashion", 2022),
                createRequest("Vans x Disney", "Entertainment", 2023),
                createRequest("Converse x NBA", "Sports", 2023),
                createRequest("Puma x Rihanna", "Celebrity", 2021),
                createRequest("New Balance x J.Crew", "Retail", 2022),
                createRequest("ASICS x Gel-Lyte", "Anniversary", 2020),
                createRequest("Balenciaga x Gucci", "Luxury", 2021),
                createRequest("Adidas x Kanye West", "Celebrity", 2015),
                createRequest("Nike x Travis Scott", "Celebrity", 2024)
            );

            for (CollabRequest request : collabs) {
                collabService.save(null, request);
            }

            log.info("✅ Seeded {} collabs", collabs.size());
        } else {
            log.info("⚠️ Collabs already exist, skipping...");
        }
    }

    private CollabRequest createRequest(String name, String type, Integer year) {
        CollabRequest request = new CollabRequest();
        request.setName(name);
        request.setType(type);
        request.setYear(year);
        return request;
    }

    @Override
    public int getOrder() {
        return 9; // Sau ColorSeeder
    }
}