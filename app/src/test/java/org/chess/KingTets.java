package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;
import org.chess.pieces.*; 

public class KingTets {
    @Test 
    void OpenBoardKing(){
        ChessBoard board = new ChessBoard();
        King king = new King(new Position(3,4), "white");

        board.setPiece(king);
        
        Set<Position> positionsCalculated = king.getLegalMoves(board);

        assertEquals(8, positionsCalculated.size());
        
        assertTrue(positionsCalculated.contains(new Position(4, 3)));
        assertTrue(positionsCalculated.contains(new Position(4, 4)));
        assertTrue(positionsCalculated.contains(new Position(4, 5)));
        assertTrue(positionsCalculated.contains(new Position(3, 5)));
        assertTrue(positionsCalculated.contains(new Position(2, 5)));
        assertTrue(positionsCalculated.contains(new Position(2, 4)));
        assertTrue(positionsCalculated.contains(new Position(2, 3)));
        assertTrue(positionsCalculated.contains(new Position(3, 3)));
    }

    @Test 
    void surroundedByOwnPiecesKing(){
        


    }

    void surroudedByAdversaryPiecesKing(){

    }


}
