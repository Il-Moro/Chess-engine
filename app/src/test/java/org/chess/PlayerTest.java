package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.chess.organization.Player;

public class PlayerTest {
    

    @Test
    void testPlayerCreation() {
        Player player = new Player("Player 1", "White");
        assertNotNull(player);
        assertEquals("Player 1", player.getName());
        assertEquals("White", player.getColor());
    }

}
