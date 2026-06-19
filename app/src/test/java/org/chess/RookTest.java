package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;
import org.chess.pieces.*;

public class RookTest {
    
    // colonne e righe
    @Test
    void openBoardRook() {

        ChessBoard board = new ChessBoard();
        
        // posizioni da calcolare
        Rook rook1 = new Rook(new Position(0,0), Colour.WHITE);
        Rook rook2 = new Rook(new Position(4,3), Colour.WHITE);
        Rook rook3 = new Rook(new Position(6,5), Colour.WHITE);

        Set<Position> positionCalculated1 = rook1.getPotentialMoves(board);
        Set<Position> positionCalculated2 = rook2.getPotentialMoves(board);
        Set<Position> positionCalculated3 = rook3.getPotentialMoves(board);
        
        // controlli minimal:
        
        // cella corrrente 
        assertFalse(positionCalculated1.contains(rook1.getPosition()), "Problem: home-square 1");
        assertFalse(positionCalculated2.contains(rook2.getPosition()), "Problem: home-square 2");
        assertFalse(positionCalculated3.contains(rook3.getPosition()), "Problem: home-square 3");
        
        // numero di posizioni:
        assertEquals(14, positionCalculated1.size(), "Problem: number of legal movements 1");
        assertEquals(14, positionCalculated2.size(), "Problem: number of legal movements 2") ;
        assertEquals(14, positionCalculated3.size(), "Problem: number of legal movements 3") ;

        // estremi:
        assertTrue(positionCalculated1.contains(new Position(0, 7)), "Extreme column 1");
        assertTrue(positionCalculated1.contains(new Position(7, 0)), "Extreme row 1");

        assertTrue(positionCalculated2.contains(new Position(4, 7)), "Extreme column 1");
        assertTrue(positionCalculated2.contains(new Position(7, 3)), "Extreme row 1");
        assertTrue(positionCalculated2.contains(new Position(4, 0)), "Extreme column 1");
        assertTrue(positionCalculated2.contains(new Position(0, 3)), "Extreme row 1");        
    }

    @Test
    void blockByAdversaryPiece() {
        
        ChessBoard board = new ChessBoard();
        
        Rook blackRook = new Rook(new Position(1,2),Colour.BLACK);
        Rook whiteRook = new Rook(new Position(6,2),Colour.WHITE);

        board.setPiece(whiteRook);
        board.setPiece(blackRook);

        Set<Position> positionsForWhiteRook = whiteRook.getPotentialMoves(board);

        // numero di mosse
        assertEquals(13, positionsForWhiteRook.size(), "Problema: numero di mosse bloccate da pezzo avversario");

        // posizioni escluse
        assertFalse(positionsForWhiteRook.contains(whiteRook.getPosition()), "Problemea: posizione da escludere");
        assertFalse(positionsForWhiteRook.contains(new Position(1, 6)), "problema: posizione da escludere");
    }

    @Test
    void blockByOwnPiece(){
        ChessBoard board = new ChessBoard();
        
        Rook whiteRook1 = new Rook(new Position(1,2),Colour.WHITE);
        Rook whiteRook = new Rook(new Position(6,2),Colour.WHITE);

        board.setPiece(whiteRook);
        board.setPiece(whiteRook1);

        Set<Position> positionsForWhiteRook = whiteRook.getPotentialMoves(board);

        // numero di mosse
        assertEquals(13, positionsForWhiteRook.size(), "Problema: numero di mosse bloccate da pezzo avversario");

        // posizioni escluse
        assertFalse(positionsForWhiteRook.contains(whiteRook.getPosition()), "Problemea: posizione da escludere");
        assertFalse(positionsForWhiteRook.contains(new Position(1, 6)), "problema: posizione da escludere");
    }

    @Test
    void surroundedByAdversarialPieces(){
        ChessBoard board = new ChessBoard();
        
        Rook whiteRook = new Rook(new Position(2,3),Colour.WHITE);
        board.setPiece(whiteRook);
        
        board.setPiece(new Rook(new Position(3, 3), Colour.BLACK)); 
        board.setPiece(new Rook(new Position(1, 3), Colour.BLACK)); 
        board.setPiece(new Rook(new Position(2, 4), Colour.BLACK)); 
        board.setPiece(new Rook(new Position(2, 2), Colour.BLACK)); 
        
        Set<Position> positionsForWhiteRook = whiteRook.getPotentialMoves(board);

        // numero di mosse
        assertEquals(4, positionsForWhiteRook.size(), "Problema: numero di mosse bloccate da pezzo avversario");
    }

    @Test
    void surroundedByOwnPieces(){
        ChessBoard board = new ChessBoard();
    
        // Torre bianca al centro
        Rook whiteRook = new Rook(new Position(2, 3), Colour.WHITE);
        board.setPiece(whiteRook);
        
        // Circondata da AMICI (per avere 0 mosse)
        board.setPiece(new Rook(new Position(3, 3), Colour.WHITE)); 
        board.setPiece(new Rook(new Position(1, 3), Colour.WHITE)); 
        board.setPiece(new Rook(new Position(2, 4), Colour.WHITE)); 
        board.setPiece(new Rook(new Position(2, 2), Colour.WHITE)); 

    Set<Position> moves = whiteRook.getPotentialMoves(board);

    assertEquals(4, moves.size(), "Problema: numero di mosse bloccate da pezzo avversario");
    }
}