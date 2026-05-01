package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;
import org.chess.pieces.*;


public class PawnBlackTest {
    @Test 
    void InitialPushOfBlackPawn(){
        ChessBoard board = new ChessBoard();
        Pawn pawn = new Pawn(new Position(6,3), "black");

        board.setPiece(pawn);

        Set<Position> movesCalculated = pawn.getLegalMoves(board);

        assertEquals(4, movesCalculated.size());
        assertTrue(movesCalculated.contains(new Position(5, 3)), "posizione 1");
        assertTrue(movesCalculated.contains(new Position(4, 3)), "posizione 2");
    }
    void OtherCasesPawn(){
        ChessBoard board = new ChessBoard();
        Pawn pawn = new Pawn(new Position(4,3), "black");

        board.setPiece(pawn);

        Set<Position> movesCalculated = pawn.getLegalMoves(board);

        assertEquals(3, movesCalculated.size());
    }    
}
