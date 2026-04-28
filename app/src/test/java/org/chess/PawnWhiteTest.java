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
        Pawn pawn = new Pawn(new Position(1,4), "white");

        board.setPiece(pawn);

        Set<Position> movesCalculated = pawn.getLegalMoves(board);

        assertEquals(2, movesCalculated.size());
        assertTrue(movesCalculated.contains(new Position(2, 4)));
        assertTrue(movesCalculated.contains(new Position(3, 4)));
    }

    @Test 
    void freePushOfWhitePawn(){
        ChessBoard board = new ChessBoard();
        Pawn pawn = new Pawn(new Position(3, 4), "white");

        board.setPiece(pawn);
        
        Set<Position> movesCalculated = pawn.getLegalMoves(board);
        
        assertEquals(1, movesCalculated.size());
        assertTrue(movesCalculated.contains(new Position(4, 4)));
    }


    @Test 
    void whitePawnBlocked(){
        ChessBoard board = new ChessBoard();
        Pawn pawn = new Pawn(new Position(1,4), "white");
        Bishop bishop = new Bishop(new Position(2, 4), "white");

        board.setPiece(pawn);
        board.setPiece(bishop);
        
        Set<Position> movesCalculate = pawn.getLegalMoves(board);

        assertEquals(0, movesCalculate.size());
    }

    @Test 
    void whitePawnCanCapture(){
        ChessBoard board = new ChessBoard();
        Pawn pawn = new Pawn(new Position(1,4), "white");
        Rock rock = new Rock(new Position(2, 5), "black");
        Bishop bishop = new Bishop(new Position(2,3), "black");

        board.setPiece(pawn);
        board.setPiece(rock);
        board.setPiece(bishop);

        Set<Position> moveCalculated = pawn.getLegalMoves(board);

        assertEquals(4, moveCalculated.size());
    }

    void whiteEnPassant(){
    }

    void whitePromotion(){
    }
    
}
