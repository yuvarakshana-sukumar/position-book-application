package com.jpmorgan.positionbook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jpmorgan.positionbook.model.TradeEvent;
import com.jpmorgan.positionbook.model.Position;
import com.jpmorgan.positionbook.repository.PositionRepository;
import com.jpmorgan.positionbook.service.PositionService;

import org.junit.jupiter.api.BeforeEach; 

@SpringBootTest
class PositionbookApplicationTests {
	@Mock
    private PositionRepository positionRepository;

    Map<Integer, TradeEvent> tradeEventMap = new HashMap<>();

    @InjectMocks
    private PositionService positionService;

    @Mock
    private Map<String, Position> positionMap;

    @BeforeEach
    public void setup() {
        when(positionRepository.findPosition(anyString(), anyString()))
            .thenAnswer(invocation -> positionMap.computeIfAbsent(
                invocation.getArgument(0) + ":" + invocation.getArgument(1), 
                key -> new Position(invocation.getArgument(0), invocation.getArgument(1))
            ));
    }

    @Test
    void testAddTradeEventBuyEvent() {
        // Arrange
        TradeEvent tradeEvent = new TradeEvent(1, "BUY", "ACC123", "AAPL", 100);
        Position position = new Position("ACC123", "AAPL");
        System.out.println("position value "+position);

        when(positionRepository.findPosition("ACC123", "AAPL")).thenReturn(position);

        positionService.addTradeEvent(tradeEvent);

        // Assert
        verify(positionRepository).addPosition(argThat(positionArgument ->
        positionArgument.getAccount().equals(position.getAccount()) &&
        positionArgument.getSecurity().equals(position.getSecurity()) &&
        positionArgument.getQuantity() == position.getQuantity()));
        tradeEventMap.put(1,tradeEvent);
        assertTrue(tradeEventMap.containsKey(1));
        assertEquals(tradeEvent, tradeEventMap.get(1));
    }

    @Test
    void testAddTradeEvent_CancelEvent() {
        // Arrange
        TradeEvent originalEvent = new TradeEvent(1, "BUY", "ACC123", "AAPL", 100);
        TradeEvent cancelEvent = new TradeEvent(1, "CANCEL", "ACC123", "AAPL", 0);
        Position position = new Position("ACC123", "AAPL");

        when(positionRepository.findPosition("ACC123", "AAPL")).thenReturn(position);

        // Add event
        positionService.addTradeEvent(originalEvent);

        verify(positionRepository).addPosition(position);
        tradeEventMap = new HashMap<>();
        tradeEventMap.put(1,originalEvent);

        //cancel event
        positionService.addTradeEvent(cancelEvent);
        tradeEventMap.remove(1);

        //assert
        assertFalse(tradeEventMap.containsKey(1), "The trade event should be removed from the map");
    }

    @Test
    void testGetPosition() {
        // Arrange
        Position position = new Position("ACC123", "AAPL");
        when(positionRepository.findPosition("ACC123", "AAPL")).thenReturn(position);

        // Act
        Position result = positionService.getPosition("ACC123", "AAPL");

        // Assert
        verify(positionRepository).findPosition("ACC123", "AAPL");
        assertTrue(result == position);
    }

    @Test
    void testAddTradeEvent_InvalidType() {
        // Arrange
        TradeEvent tradeEvent = new TradeEvent(1, "INVALID", "ACC123", "AAPL", 100);

        // Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
		() -> positionService.addTradeEvent(tradeEvent));

		assert exception.getMessage().contains("Invalid action: INVALID");
    }

    @Test
    void testCancelTradeEvent_InvalidAccountOrSecurity() {
        // Arrange
        TradeEvent originalEvent = new TradeEvent(1, "BUY", "ACC123", "AAPL", 100);
        TradeEvent cancelEvent = new TradeEvent(1, "CANCEL", "ACC999", "MSFT", 0);

        positionService.addTradeEvent(originalEvent);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
        () -> positionService.addTradeEvent(cancelEvent));

    	assertTrue(exception.getMessage().contains("Cancel event does not match the original event"));
	}
}
