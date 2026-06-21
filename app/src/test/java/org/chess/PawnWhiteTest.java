package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;
import org.chess.pieces.*;


public class PawnWhiteTest {
    @Test 
    void InitialPushOfWhitePawn(){
        ChessBoard board = new ChessBoard();
        Pawn pawn = new Pawn(new Position(1,4), Colour.WHITE);

        board.setPiece(pawn);

        Set<Position> movesCalculated = pawn.getPotentialMoves(board);

        assertEquals(4, movesCalculated.size());
        assertTrue(movesCalculated.contains(new Position(2, 4)));
        assertTrue(movesCalculated.contains(new Position(3, 4)));
    }

    @Test 
    void OtherCasesPawn(){
        ChessBoard board = new ChessBoard();
        Pawn pawn = new Pawn(new Position(3, 4), Colour.WHITE);

        board.setPiece(pawn);
        
        Set<Position> movesCalculated = pawn.getPotentialMoves(board);
        
        assertEquals(3, movesCalculated.size());
        assertTrue(movesCalculated.contains(new Position(4, 4)));
    }    

    @Test
    void InitialPushOfWhitePawnBlockedAtOneStep(){
        ChessBoard board = new ChessBoard();
        Pawn pawn = new Pawn(new Position(1,4), Colour.WHITE);
        board.setPiece(pawn);

        Pawn blockPiece = new Pawn(new Position(2, 4), Colour.BLACK);
        board.setPiece(blockPiece);

        Set<Position> movesCalculated = pawn.getPotentialMoves(board);

        assertEquals(2, movesCalculated.size());
        assertTrue(movesCalculated.contains(new Position(2, 3)));
        assertTrue(movesCalculated.contains(new Position(2, 5)));
        assertFalse(movesCalculated.contains(new Position(2, 4)));
        assertFalse(movesCalculated.contains(new Position(3, 4)));
    }

    @Test
    void InitialPushOfWhitePawnBlockedAtTwoSteps(){
        ChessBoard board = new ChessBoard();
        Pawn pawn = new Pawn(new Position(1,4), Colour.WHITE);
        board.setPiece(pawn);

        Pawn blockPiece = new Pawn(new Position(3, 4), Colour.BLACK);
        board.setPiece(blockPiece);

        Set<Position> movesCalculated = pawn.getPotentialMoves(board);

        assertEquals(3, movesCalculated.size());
        assertTrue(movesCalculated.contains(new Position(2, 4)));
        assertTrue(movesCalculated.contains(new Position(2, 3)));
        assertTrue(movesCalculated.contains(new Position(2, 5)));
        assertFalse(movesCalculated.contains(new Position(3, 4)));
    }
}
