package com.example.sd_62.common.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final List<BaseSeeder> seeders;

    @Override
    public void run(String... args) {
        log.info("🚀 Starting database seeding...");
        log.info("📦 Found {} seeders to execute", seeders.size());
        
        seeders.stream()
                .sorted(Comparator.comparingInt(BaseSeeder::getOrder))
                .forEach(seeder -> {
                    try {
                        long startTime = System.currentTimeMillis();
                        log.info("🔄 Executing {}...", seeder.getClass().getSimpleName());
                        seeder.seed();
                        long endTime = System.currentTimeMillis();
                        log.info("✅ Completed {} in {}ms", 
                            seeder.getClass().getSimpleName(), 
                            (endTime - startTime));
                    } catch (Exception e) {
                        log.error("❌ Error seeding {}: {}", 
                            seeder.getClass().getSimpleName(), e.getMessage());
                        e.printStackTrace();
                    }
                });
        
        log.info("🎉 Database seeding completed successfully!");
    }
}