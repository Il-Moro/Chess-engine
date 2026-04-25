package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;
import org.chess.pieces.*;

public class RockTest {
    
    // colonne e righe
    @Test
    void openBoardRock() {

        ChessBoard board = new ChessBoard();
        
        // posizioni da calcolare
        Rock rock1 = new Rock(new Position(0,0), "white");
        Rock rock2 = new Rock(new Position(4,3), "white");
        Rock rock3 = new Rock(new Position(6,5), "white");

        Set<Position> positionCalculated1 = rock1.getLegalMoves(board);
        Set<Position> positionCalculated2 = rock2.getLegalMoves(board);
        Set<Position> positionCalculated3 = rock3.getLegalMoves(board);
        
        // controlli minimal:
        
        // cella corrrente 
        assertFalse(positionCalculated1.contains(rock1.getPosition()), "Problem: home-square 1");
        assertFalse(positionCalculated2.contains(rock2.getPosition()), "Problem: home-square 2");
        assertFalse(positionCalculated3.contains(rock3.getPosition()), "Problem: home-square 3");
        
        // numero di posizioni:
        assertEquals(14, positionCalculated1.size(), "Problem: number of legal movements 1");
        assertEquals(14, positionCalculated2.size(), "Problem: number of legal movements 2") ;
        assertEquals(14, positionCalculated3.size(), "Problem: number of legal movements 3") ;

        // estremi:
        assertTrue(positionCalculated1.contains(new Position(0, 7)), "Extreme column 1");
        assertTrue(positionCalculated1.contains(new Position(7, 0)), "Extrrme row 1");

        assertTrue(positionCalculated2.contains(new Position(4, 7)), "Extreme column 1");
        assertTrue(positionCalculated2.contains(new Position(7, 3)), "Extreme row 1");
        assertTrue(positionCalculated2.contains(new Position(4, 0)), "Extreme column 1");
        assertTrue(positionCalculated2.contains(new Position(0, 3)), "Extreme row 1");        
    }

    @Test
    void blockByOtherPiece() {
        
        ChessBoard board = new ChessBoard();
        
        Rock blackRock = new Rock(new Position(1,2),"black");
        Rock whiteRock = new Rock(new Position(6,2),"white");

        board.setPiece(whiteRock);
        board.setPiece(blackRock);

        Set<Position> positionsForWhiteRock = whiteRock.getLegalMoves(board);

        // numero di mosse
        assertEquals(13, positionsForWhiteRock.size(), "Problema: numero di mosse bloccate da pezzo avversario");

        // posizioni escluse
        assertFalse(positionsForWhiteRock.contains(whiteRock.getPosition()), "Problemea: posizione da escludere");
        assertFalse(positionsForWhiteRock.contains(new Position(1, 6)), "problema: posizione da escludere");
    }
}