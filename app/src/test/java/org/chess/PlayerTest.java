package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.chess.organization.Player;
import org.chess.dataTypes.Color;

public class PlayerTest {
    

    @Test
    void testPlayerCreation() {
        
        Player playerOne = new Player("Player 1", Color.WHITE);
        assertNotNull(playerOne);
        assertEquals("Player 1", playerOne.getName());
        assertEquals(Color.WHITE, playerOne.getColor());


    }

    @Test
    void testPlayerCreationWithInvalidName() {

        assertThrows(IllegalArgumentException.class, () -> new Player(null, Color.WHITE));
        assertThrows(IllegalArgumentException.class, () -> new Player("", Color.WHITE));
        assertThrows(IllegalArgumentException.class, () -> new Player("   ",Color.WHITE));
    }

    @Test
    void testPlayerCreationWithInvalidColor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("Player 1", null);
        });
    }

    @Test
    void testChosenPiece(){
        //TODO
    }


    @Test
    void testChosenMoves(){
        // TODO
    }


}
