package com.bgauction.notificationservice.kafka.eventHandler;

import com.bgauction.notificationservice.feign.Game;
import com.bgauction.notificationservice.feign.GatewayClient;
import com.bgauction.notificationservice.feign.User;
import com.bgauction.notificationservice.kafka.event.AuctionUpdateEvent;
import com.bgauction.notificationservice.model.Notification;
import com.bgauction.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Log4j2
public class EventHandler {

    private final GatewayClient gatewayClient;

    private final NotificationService notificationService;

    @KafkaListener(topics = "outbid-event-topic")
    public void handleOutbidEvent(AuctionUpdateEvent event) {
        log.info("Received message from outbid-event-topic with event: {}", event.toString());
        String notificationText = createNotificationTextAboutOutbid(event);
        Long recipientId = event.getWinnerId();
        String email = getUserEmail(recipientId);
        createAndSaveNotification(recipientId, email, notificationText, event.getCreatedAt());
    }

    @KafkaListener(topics = "new-bid-event-topic")
    public void handleNewBidEvent(AuctionUpdateEvent event) {
        log.info("Received message from new-bid-event-topic with event: {}", event.toString());
        String notificationText = createNotificationTextToSellerAboutNewBid(event);
        Long recipientId = event.getSellerId();
        String email = getUserEmail(recipientId);
        createAndSaveNotification(recipientId, email, notificationText, event.getCreatedAt());
    }

    @KafkaListener(topics = "auction-finished-event-topic")
    public void handleAuctionFinishedEvent(AuctionUpdateEvent event) {
        log.info("Received message from auction-finished-event-topic with event: {}", event.toString());
        createAndSendAuctionFinishedNotificationToSeller(event);
        if (event.getWinnerId() != null) {
            createAndSendAuctionFinishedNotificationToWinner(event);
        }
    }

    private void createAndSendAuctionFinishedNotificationToSeller(AuctionUpdateEvent event) {
        String notificationText = createAuctionFinishedNotificationTextToSeller(event);
        Long recipientId = event.getSellerId();
        String email = getUserEmail(recipientId);
        createAndSaveNotification(recipientId, email, notificationText, event.getCreatedAt());
    }

    private void createAndSendAuctionFinishedNotificationToWinner(AuctionUpdateEvent event) {
        String notificationText = createAuctionFinishedNotificationTextToWinner(event);
        Long recipientId = event.getWinnerId();
        String email = getUserEmail(recipientId);
        createAndSaveNotification(recipientId, email, notificationText, event.getCreatedAt());
    }

    private void createAndSaveNotification(
            Long recipientId, String email, String notificationText, LocalDateTime created) {
        Notification notification = Notification.builder()
                .userId(recipientId)
                .email(email)
                .notificationText(notificationText)
                .createdAt(created)
                .sendAt(LocalDateTime.now())
                .build();
        /* Method that sends email - not implemented */
        notificationService.saveNotification(notification);
    }

    private String createNotificationTextAboutOutbid(AuctionUpdateEvent event) {
        StringBuilder builder = new StringBuilder("Your bid was outbid!")
                .append(System.lineSeparator());
        addAuctionLink(builder, event.getAuctionId());
        addGameName(builder, event.getGameId());
        return builder.toString();
    }

    private String createNotificationTextToSellerAboutNewBid(AuctionUpdateEvent event) {
        StringBuilder builder = new StringBuilder("New bid in the auction you announced!")
                .append(System.lineSeparator());
        addAuctionLink(builder, event.getAuctionId());
        addGameName(builder, event.getGameId());
        addGamePrice(builder, event.getCurrentPrice());
        return builder.toString();
    }

    private String createAuctionFinishedNotificationTextToSeller(AuctionUpdateEvent event) {
        StringBuilder builder = new StringBuilder("The auction you announced has finished!")
                .append(System.lineSeparator());
        addGameName(builder, event.getGameId());
        if (event.getWinnerId() == null) {
            builder.append("Unfortunately, no one took part in the auction you announced.")
                    .append(System.lineSeparator())
                    .append("The game has not been sold, but you can try to put it up for auction again.")
                    .append(System.lineSeparator());
        } else {
            addGamePrice(builder, event.getCurrentPrice());
            addAuctionWinnerEmail(builder, event.getWinnerId());
        }
        addGamePrice(builder, event.getCurrentPrice());
        return builder.toString();
    }

    private String createAuctionFinishedNotificationTextToWinner(AuctionUpdateEvent event) {
        StringBuilder builder = new StringBuilder("Congratulations! You won the auction!")
                .append(System.lineSeparator());
        addGameName(builder, event.getGameId());
        addGamePrice(builder, event.getCurrentPrice());
        addSellerEmail(builder, event.getSellerId());
        return builder.toString();
    }

    private void addAuctionLink(StringBuilder builder, Long auctionId) {
        builder.append("Auction: link/")
                .append(auctionId)
                .append(System.lineSeparator());
    }

    private void addGameName(StringBuilder builder, Long gameId) {
        ResponseEntity<Game> response = gatewayClient.getGameById(gameId);
        String gameName = "not found";
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            gameName = Objects.requireNonNull(response.getBody()).getTitle();
        }
        builder.append("Game name: ")
                .append(gameName)
                .append(System.lineSeparator());
    }

    private void addGamePrice(StringBuilder builder, BigDecimal price) {
        builder.append("Game price: ")
                .append(price)
                .append(System.lineSeparator());
    }

    private void addAuctionWinnerEmail(StringBuilder builder, Long userId) {
        String email = getUserEmail(userId);
        builder.append("Buyer's email for contact: ")
                .append(email)
                .append(System.lineSeparator());
    }

    private void addSellerEmail(StringBuilder builder, Long userId) {
        String email = getUserEmail(userId);
        builder.append("Seller's email for contact: ")
                .append(email)
                .append(System.lineSeparator());
    }

    private String getUserEmail(Long userId) {
        ResponseEntity<User> response = gatewayClient.getUserById(userId);
        String email = "not found";
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            email = Objects.requireNonNull(response.getBody()).getEmail();
        }
        return email;
    }
}
