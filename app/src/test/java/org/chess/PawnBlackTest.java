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
        Pawn pawn = new Pawn(new Position(6,3), Colour.BLACK);

        board.setPiece(pawn);

        Set<Position> movesCalculated = pawn.getPotentialMoves(board);

        assertEquals(4, movesCalculated.size());
        assertTrue(movesCalculated.contains(new Position(5, 3)), "posizione 1");
        assertTrue(movesCalculated.contains(new Position(4, 3)), "posizione 2");
    }
    @Test
    void OtherCasesPawn(){
        ChessBoard board = new ChessBoard();
        Pawn pawn = new Pawn(new Position(4,3), Colour.BLACK);

        board.setPiece(pawn);

        Set<Position> movesCalculated = pawn.getPotentialMoves(board);

        assertEquals(3, movesCalculated.size());
    }    

    @Test
    void InitialPushOfBlackPawnBlockedAtOneStep(){
        ChessBoard board = new ChessBoard();
        Pawn pawn = new Pawn(new Position(6, 3), Colour.BLACK);
        board.setPiece(pawn);

        Pawn blockPiece = new Pawn(new Position(5, 3), Colour.WHITE);
        board.setPiece(blockPiece);

        Set<Position> movesCalculated = pawn.getPotentialMoves(board);

        assertEquals(2, movesCalculated.size());
        assertTrue(movesCalculated.contains(new Position(5, 2)));
        assertTrue(movesCalculated.contains(new Position(5, 4)));
        assertFalse(movesCalculated.contains(new Position(5, 3)));
        assertFalse(movesCalculated.contains(new Position(4, 3)));
    }

    @Test
    void InitialPushOfBlackPawnBlockedAtTwoSteps(){
        ChessBoard board = new ChessBoard();
        Pawn pawn = new Pawn(new Position(6, 3), Colour.BLACK);
        board.setPiece(pawn);

        Pawn blockPiece = new Pawn(new Position(4, 3), Colour.WHITE);
        board.setPiece(blockPiece);

        Set<Position> movesCalculated = pawn.getPotentialMoves(board);

        assertEquals(3, movesCalculated.size());
        assertTrue(movesCalculated.contains(new Position(5, 3)));
        assertTrue(movesCalculated.contains(new Position(5, 2)));
        assertTrue(movesCalculated.contains(new Position(5, 4)));
        assertFalse(movesCalculated.contains(new Position(4, 3)));
    }
}
