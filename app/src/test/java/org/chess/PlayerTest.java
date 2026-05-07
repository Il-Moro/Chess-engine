package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.chess.organization.Player;

public class PlayerTest {
    

    @Test
    void testPlayerCreation() {
        
        Player playerOne = new Player("Player 1", "white");
        assertNotNull(playerOne);
        assertEquals("Player 1", playerOne.getName());
        assertEquals("white", playerOne.getColor());


    }

    @Test
    void testPlayerCreationWithInvalidName() {

        assertThrows(IllegalArgumentException.class, () -> new Player(null, "white"));
        assertThrows(IllegalArgumentException.class, () -> new Player("", "white"));
        assertThrows(IllegalArgumentException.class, () -> new Player("   ","white"));
    }

    @Test
    void testPlayerCreationWithInvalidColor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("Player 1", null);
            assertThrows(IllegalArgumentException.class, () -> new Player("Player 1", ""));
            assertThrows(IllegalArgumentException.class, () -> new Player("   ","   "));
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
