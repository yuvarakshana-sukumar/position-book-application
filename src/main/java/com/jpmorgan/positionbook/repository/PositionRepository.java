package com.jpmorgan.positionbook.repository;

import com.jpmorgan.positionbook.model.Position;
import org.springframework.stereotype.Repository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

// Repository for handling positions.
@Repository
public class PositionRepository {

    private static final Logger logger = LoggerFactory.getLogger(PositionRepository.class);

    private final Map<String, Position> positionMap = new HashMap<>();

    // Adds a position to the repository.
    public void addPosition(Position position) {
        logger.debug("Adding position: {}", position);
        positionMap.put(position.getAccount() + ":" + position.getSecurity(), position);
    }

    // Retrieves a position based on account and security.
    public Position findPosition(String account, String security) {
        logger.debug("Retrieve position for account: {} and security: {}",account,security);
        return positionMap.computeIfAbsent(account + ":" + security, key -> new Position(account, security));
    }
}

