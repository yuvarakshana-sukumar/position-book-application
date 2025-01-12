package com.jpmorgan.positionbook.service;

import com.jpmorgan.positionbook.repository.PositionRepository;
import com.jpmorgan.positionbook.model.Position;
import com.jpmorgan.positionbook.model.TradeEvent;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

/**
 * Service for handling the business logic related to positions and trades.
 */
@Service
public class PositionService {

    private static final Logger logger = LoggerFactory.getLogger(PositionService.class);
    
    private final PositionRepository positionRepository;

    private final Map<Integer, TradeEvent> tradeEventMap = new HashMap<>();

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    // add trade event
    public void addTradeEvent(TradeEvent tradeEvent) {
        logger.info("Processing trade event: {}", tradeEvent);

        // Logic for processing trade events
        if ("CANCEL".equalsIgnoreCase(tradeEvent.getType())) {
            logger.info("Canceling trade event with ID: {}", tradeEvent.getId());
            cancelTradeEvent(tradeEvent);
        } else {
            logger.info("Adding new trade event with ID: {}", tradeEvent.getId());
            BuyOrSellEvent(tradeEvent);
        }
    }

    // retrieve position for the given account
    public Position getPosition(String account, String security) {
        logger.debug("Fetching position for account: {} and security: {}", account, security);
        return positionRepository.findPosition(account, security);
    }

    //buy or sell event
    private void BuyOrSellEvent(TradeEvent tradeEvent) {
        Position position = positionRepository.findPosition(tradeEvent.getAccount(), tradeEvent.getSecurity());

         // If position is null, create a new position
        if (position == null) {
            position = new Position(tradeEvent.getAccount(), tradeEvent.getSecurity());
        }

        if("BUY".equalsIgnoreCase(tradeEvent.getType())){
            position.updateQuantity(tradeEvent.getQuantity());
        }
        else if ("SELL".equalsIgnoreCase(tradeEvent.getType())){
            position.updateQuantity(tradeEvent.getQuantity());
        }
        else{
            throw new IllegalArgumentException("Invalid action: " + tradeEvent.getType());
        }

        positionRepository.addPosition(position);
        // Store the trade event for possible cancellation
        tradeEventMap.put(tradeEvent.getId(), tradeEvent);
    }

    // cancel event
    private void cancelTradeEvent(TradeEvent cancelEvent) {
        // Retrieve the original trade event by event ID
        TradeEvent originalEvent = tradeEventMap.get(cancelEvent.getId());

        if (originalEvent != null) {
            // The event being canceled must match the account and security
            if (originalEvent.getAccount().equals(cancelEvent.getAccount()) &&
                originalEvent.getSecurity().equals(cancelEvent.getSecurity())) {

                // If it's a BUY, subtract from position
                if ("BUY".equalsIgnoreCase(originalEvent.getType())) {
                    Position position = positionRepository.findPosition(originalEvent.getAccount(), originalEvent.getSecurity());
                    position.updateQuantity(-originalEvent.getQuantity());
                    positionRepository.addPosition(position);
                }
                // If it's a SELL, add back to position
                else if ("SELL".equalsIgnoreCase(originalEvent.getType())) {
                    Position position = positionRepository.findPosition(originalEvent.getAccount(), originalEvent.getSecurity());
                    position.updateQuantity(originalEvent.getQuantity());
                    positionRepository.addPosition(position);
                }

                // Remove the original event from the map as it has been canceled
                tradeEventMap.remove(cancelEvent.getId());
            } else {
                throw new IllegalArgumentException("Cancel event does not match the original event");
            }
        } else {
            throw new IllegalArgumentException("Original event not found for cancellation");
        }
    }
}
