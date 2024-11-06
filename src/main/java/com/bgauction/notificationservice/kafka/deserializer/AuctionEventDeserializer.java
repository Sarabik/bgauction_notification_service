package com.bgauction.notificationservice.kafka.deserializer;

import com.bgauction.notificationservice.kafka.event.AuctionUpdateEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Arrays;
import java.util.Map;

@Log4j2
public class AuctionEventDeserializer implements Deserializer<AuctionUpdateEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public AuctionUpdateEvent deserialize(String topic, byte[] data) {
        log.info("Start deserialization");
        try {
            if (data == null) {
                log.info("Received null data for deserialization in topic: {}", topic);
                return null;
            }
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(data, AuctionUpdateEvent.class);
        } catch (Exception e) {
            log.error("Error during deserialization", e);
            return null;
        }
    }

    @Override
    public void close() {
    }
}
