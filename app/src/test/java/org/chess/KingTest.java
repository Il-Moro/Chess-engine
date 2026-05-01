package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;
import org.chess.pieces.*; 

public class KingTest {
    @Test 
    void OpenBoardKing(){
        ChessBoard board = new ChessBoard();
        King king = new King(new Position(3,4), "white");

        board.setPiece(king);
        
        Set<Position> positionsCalculated = king.getPotentialMoves(board);

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
        ChessBoard board = new ChessBoard();
        King king = new King(new Position(0, 0), "white");
        Bishop bishop = new Bishop(new Position(1, 0), "white");
        Rock rock = new Rock(new Position(1, 1), "white");
        Queen queen = new Queen(new Position(0, 1), "white");

        board.setPiece(king);
        board.setPiece(bishop);
        board.setPiece(rock);
        board.setPiece(queen);

        Set<Position> movesCalculated = king.getPotentialMoves(board);

        assertEquals(3, movesCalculated.size());
    }

    @Test
    void surroundedByAdversarialPiecesKing(){
        ChessBoard board = new ChessBoard();
        King king = new King(new Position(0, 0), "black");
        Bishop bishop = new Bishop(new Position(1, 0), "black");
        Rock rock = new Rock(new Position(1, 1), "black");
        Queen queen = new Queen(new Position(0, 1), "black");

        board.setPiece(king);
        board.setPiece(bishop);
        board.setPiece(rock);
        board.setPiece(queen);

        Set<Position> movesCalculated = king.getPotentialMoves(board);

        assertEquals(3, movesCalculated.size());
    }
}
