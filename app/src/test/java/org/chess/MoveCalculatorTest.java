package org.chess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.movementsRules.*;
import org.chess.organization.*;
import org.junit.jupiter.api.Test;

public class MoveCalculatorTest {
    
    // colonne e righe
    @Test
    void horizontalAndVertical_openBoard() {

        ChessBoard board = new ChessBoard();
        
        // posizioni da calcolare
        Position position1 = new Position(1,1);
        Position position2 = new Position(5,4);
        Position position3 = new Position(7,6);

        Set<Position> positionCalculated1 = MoveCalculator.horizontalAndVertical(board, position1);
        Set<Position> positionCalculated2 = MoveCalculator.horizontalAndVertical(board, position2);
        Set<Position> positionCalculated3 = MoveCalculator.horizontalAndVertical(board, position3);
        
        
        // controlli minimal:
        
        // cella corrrente 
        assertFalse(positionCalculated1.contains(position1),"Problem: home-square 1");
        assertFalse(positionCalculated2.contains(position2),"Problem: home-square 2");
        assertFalse(positionCalculated3.contains(position3),"Problem: home-square 3");
        
        // numero di posizioni:
        assertEquals(positionCalculated1.size(), 14, "Problem: number of legal movements 1");
        assertEquals(positionCalculated2.size(), 14, "Problem: number of legal movements 2") ;
        assertEquals(positionCalculated3.size(), 14, "Problem: number of legal movements 3") ;
    }

    

    @Test
    void horizontalAndVertical_blockedByOwnPiece() {
        
    }

    @Test
    void horizontalAndVertical_capturesEnemy() {
        
    }

    // diagonali
    @Test
    void diagonals_openBoard() {
       
    }

    @Test
    void diagonals_blockedByOwnPiece() {
        
    }

    @Test
    void diagonals_capturesEnemy() {
        
    }

    // Pawns

    // king

}