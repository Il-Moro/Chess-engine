package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;
import org.chess.pieces.*;

public class QueenTest {
    @Test
    void openBoardQueen(){
        
        // centrale
        ChessBoard board1 = new ChessBoard();
        Queen queenCentre = new Queen(new Position(4,4), "white");
        
        board1.setPiece(queenCentre);

        Set<Position> positionsCalculated1 = queenCentre.getPotentialMoves(board1);
        assertEquals(27, positionsCalculated1.size());

        //a lato
        ChessBoard board2 = new ChessBoard();
        Queen queenEdge = new Queen(new Position(0,0), "white");
        
        board2.setPiece(queenEdge);

        Set<Position> positionsCalculated2 = queenEdge.getPotentialMoves(board2);
        assertEquals(21, positionsCalculated2.size());
    }

    @Test
    void blockByAdversaryPieceQueen(){

        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(new Position(4,4), "white");
        Rock rock = new Rock(new Position(5, 5), "black");
        
        board.setPiece(queen);
        board.setPiece(rock);

        Set<Position> positionsCalculated = queen.getPotentialMoves(board);
        assertEquals(25, positionsCalculated.size());
    }

    @Test 
    void blockByOwnPieceQueen(){
        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(new Position(4,4), "white");
        Rock rock = new Rock(new Position(5, 5), "white");
        
        board.setPiece(queen);
        board.setPiece(rock);

        Set<Position> positionsCalculated = queen.getPotentialMoves(board);
        assertEquals(25, positionsCalculated.size());
    }

    @Test 
    void surroundedByAdversaryPiecesQueen(){
        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(new Position(7,7), "white");

        board.setPiece(queen);
        board.setPiece(new Rock(new Position(7,6),"black"));
        board.setPiece(new Rock(new Position(6,6),"black"));
        board.setPiece(new Rock(new Position(6,7),"black"));

        Set<Position> positionsCalculated = queen.getPotentialMoves(board);
        assertEquals(3, positionsCalculated.size());
    }

    @Test 
    void surroundedByOwnPiecesQueen(){
        ChessBoard board = new ChessBoard();
        Queen queen = new Queen(new Position(7,7), "white");

        board.setPiece(queen);
        board.setPiece(new Rock(new Position(7,6),"white"));
        board.setPiece(new Rock(new Position(6,6),"white"));
        board.setPiece(new Rock(new Position(6,7),"white"));

        Set<Position> positionsCalculated = queen.getPotentialMoves(board);
        assertEquals(3, positionsCalculated.size());
    }
}
