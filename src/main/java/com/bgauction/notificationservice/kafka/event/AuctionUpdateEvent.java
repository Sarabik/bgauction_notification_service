package com.bgauction.notificationservice.kafka.event;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionUpdateEvent {
    @NotNull
    @Positive
    private Long auctionId;

    @NotNull
    @Positive
    private Long sellerId;

    private Long winnerId;

    @NotNull
    @Positive
    private Long gameId;

    @NotNull
    @Positive
    private BigDecimal currentPrice;

    @NotNull
    private LocalDateTime createdAt;
}
