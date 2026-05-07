package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.chess.organization.Player;

public class PlayerTest {
    

    @Test
    void testPlayerCreation() {
        
        //Player playerOne = new Player("Player 1", "white");
        //assertNotNull(playerOne);
        //assertEquals("Player 1", playerOne.getName());


    }

    @Test
    void testPlayerCreationWithInvalidName() {

        //assertThrows(IllegalArgumentException.class, () -> new Player(null, "white"));
        //assertThrows(IllegalArgumentException.class, () -> new Player("", "white"));
        //assertThrows(IllegalArgumentException.class, () -> new Player("   ","white"));
    }


}
