package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;
import org.chess.pieces.*;

public class KnightTest {
    
    @Test
    void openBoardKnightAtCenter(){
        
        ChessBoard board = new ChessBoard();
        Knight knight = new Knight(new Position(3, 3), "white");

        board.setPiece(knight);

        Set<Position> positionCalculated = knight.getLegalMoves(board);
        
        assertEquals(8, positionCalculated.size());
        
        assertTrue(positionCalculated.contains(new Position(5, 2)), "Posizione 1");
        assertTrue(positionCalculated.contains(new Position(5, 4)), "Posizione 2");

        assertTrue(positionCalculated.contains(new Position(2, 5)), "Posizione 3");
        assertTrue(positionCalculated.contains(new Position(2, 5)), "Posizione 4");
        
        assertTrue(positionCalculated.contains(new Position(1, 4)), "Posizione 5");
        assertTrue(positionCalculated.contains(new Position(1, 2)), "Posizione 6");

        assertTrue(positionCalculated.contains(new Position(2, 1)), "Posizione 7");
        assertTrue(positionCalculated.contains(new Position(4, 1)), "Posizione 8");
    }

    @Test 
    void openBoardKnightAtEdge(){
        ChessBoard board = new ChessBoard();
        Knight knight = new Knight(new Position(0, 0), "white");

        board.setPiece(knight);

        Set<Position> positionCalculated = knight.getLegalMoves(board);
        
        assertEquals(2, positionCalculated.size());
        
        assertTrue(positionCalculated.contains(new Position(2, 1)), "Posizione 1");
        assertTrue(positionCalculated.contains(new Position(1, 2)), "Posizione 2");
    }
    


}
