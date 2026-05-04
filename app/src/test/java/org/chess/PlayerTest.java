package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.chess.organization.Player;
import org.chess.dataTypes.Color;

public class PlayerTest {
    

    @Test
    void testPlayerCreation() {
        Player player = new Player("Player 1", Color.WHITE);
        assertNotNull(player);
        assertEquals("Player 1", player.getName());
        assertEquals(Color.WHITE, player.getColor());
    }

}
