package com.bgauction.notificationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "notifications",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "notification_text", "created"})})
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Positive
    @Column(name = "user_id", updatable = false)
    private Long userId;

    @NotBlank
    @Size(min = 3)
    @Column(name = "email", updatable = false)
    private String email;

    @NotBlank
    @Size(min = 5)
    @Column(name = "notification_text", updatable = false)
    private String notificationText;

    @NotBlank
    @Column(name = "created", updatable = false)
    private LocalDateTime created;
}
