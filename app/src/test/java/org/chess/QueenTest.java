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

        Set<Position> positionsCalculated1 = queenCentre.getLegalMoves(board1);
        assertEquals(27, positionsCalculated1.size());

        //a lato
        ChessBoard board2 = new ChessBoard();
        Queen queenEdge = new Queen(new Position(0,0), "white");
        
        board2.setPiece(queenEdge);

        Set<Position> positionsCalculated2 = queenEdge.getLegalMoves(board2);
        assertEquals(21, positionsCalculated2.size());
    }

    @Test
    void blockByAdversaryPieceQueen(){
        
    }

    @Test 
    void blockByOwnPieceQueen(){

    }

    @Test 
    void surroundedByAdversaryPiecesQueen(){

    }

    @Test 
    void surroundedByOwnPiecesQueen(){

    }
}
