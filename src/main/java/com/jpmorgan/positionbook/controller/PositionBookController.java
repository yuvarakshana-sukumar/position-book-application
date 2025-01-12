package com.jpmorgan.positionbook.controller;

import com.jpmorgan.positionbook.model.Position;
import com.jpmorgan.positionbook.model.TradeEvent;
import com.jpmorgan.positionbook.service.PositionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@RestController
@RequestMapping("/jpmorgan/position")
public class PositionBookController {

    private static final Logger logger = LoggerFactory.getLogger(PositionBookController.class);
    private final PositionService positionService;

    public PositionBookController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> addTradeEvent(@RequestBody TradeEvent tradeEvent) {
        logger.info("Received trade event: {}", tradeEvent);
        try {
            positionService.addTradeEvent(tradeEvent);
            logger.info("Trade event processed successfully.");
            return ResponseEntity.ok("Trade event processed successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Error processing trade event: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/retrieve")
    public ResponseEntity<Position> getPosition(@RequestParam String account, @RequestParam String security) {
        logger.debug("Retrieving position for account: {} and security: {}", account, security);
        Position position = positionService.getPosition(account, security);
        if (position != null) {
            logger.debug("Position found: {}", position);
            return ResponseEntity.ok(position);
        } else {
            logger.warn("Position not found for account: {} and security: {}", account, security);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
