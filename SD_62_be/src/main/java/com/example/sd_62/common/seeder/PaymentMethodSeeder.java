package com.example.sd_62.common.seeder;

import com.example.sd_62.order.entity.PaymentMethod;
import com.example.sd_62.order.enums.PaymentStatus;
import com.example.sd_62.order.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentMethodSeeder implements BaseSeeder {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public void seed() {
        if (paymentMethodRepository.count() == 0) {
            log.info("🔄 Seeding payment methods...");

            List<PaymentMethod> paymentMethods = Arrays.asList(
                    PaymentMethod.builder()
                            .name("COD")
                            .description("Thanh toán khi nhận hàng - Cash On Delivery")
                            .status(PaymentStatus.ACTIVE)
                            .build(),
                    PaymentMethod.builder()
                            .name("VNPAY")
                            .description("Thanh toán qua cổng VNPAY - Hỗ trợ Internet Banking, ATM")
                            .status(PaymentStatus.ACTIVE)
                            .build(),
                    PaymentMethod.builder()
                            .name("MOMO")
                            .description("Thanh toán qua ví điện tử MoMo")
                            .status(PaymentStatus.ACTIVE)
                            .build(),
                    PaymentMethod.builder()
                            .name("ZALOPAY")
                            .description("Thanh toán qua ví điện tử ZaloPay")
                            .status(PaymentStatus.ACTIVE)
                            .build(),
                    PaymentMethod.builder()
                            .name("Chuyển khoản ngân hàng")
                            .description("Chuyển khoản trực tiếp qua tài khoản ngân hàng")
                            .status(PaymentStatus.ACTIVE)
                            .build(),
                    PaymentMethod.builder()
                            .name("Thẻ tín dụng")
                            .description("Thanh toán bằng thẻ Visa, MasterCard, JCB")
                            .status(PaymentStatus.ACTIVE)
                            .build(),
                    PaymentMethod.builder()
                            .name("ShopeePay")
                            .description("Thanh toán qua ví ShopeePay")
                            .status(PaymentStatus.INACTIVE)
                            .build(),
                    PaymentMethod.builder()
                            .name("Trả góp")
                            .description("Trả góp qua thẻ tín dụng hoặc công ty tài chính")
                            .status(PaymentStatus.INACTIVE)
                            .build(),
                    PaymentMethod.builder()
                            .name("Apple Pay")
                            .description("Thanh toán qua Apple Pay")
                            .status(PaymentStatus.INACTIVE)
                            .build(),
                    PaymentMethod.builder()
                            .name("Google Pay")
                            .description("Thanh toán qua Google Pay")
                            .status(PaymentStatus.INACTIVE)
                            .build()
            );

            paymentMethodRepository.saveAll(paymentMethods);
            log.info("✅ Seeded {} payment methods", paymentMethods.size());

            // Log chi tiết
            long activeCount = paymentMethods.stream()
                    .filter(pm -> pm.getStatus() == PaymentStatus.ACTIVE)
                    .count();
            long inactiveCount = paymentMethods.stream()
                    .filter(pm -> pm.getStatus() == PaymentStatus.INACTIVE)
                    .count();

            log.info("   📊 Active: {} methods, Inactive: {} methods", activeCount, inactiveCount);

        } else {
            log.info("⚠️ Payment methods already exist ({} records), skipping...",
                    paymentMethodRepository.count());
        }
    }

    @Override
    public int getOrder() {
        return 6;
    }
}