package org.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*;
import org.chess.pieces.*;

public class BishopTest {
    
    // colonne e righe
    @Test
    void openBoardBishop() {

        ChessBoard board = new ChessBoard();
        
        // posizioni da calcolare
        Bishop bishop1 = new Bishop(new Position(0,0), "white");
        Bishop bishop2 = new Bishop(new Position(3,3), "white");
        
        Set<Position> positionCalculated1 = bishop1.getLegalMoves(board);
        Set<Position> positionCalculated2 = bishop2.getLegalMoves(board);
        
        // controlli minimal:
        
        // cella corrrente 
        assertFalse(positionCalculated1.contains(bishop1.getPosition()), "Problem: home-square 1");
        assertFalse(positionCalculated2.contains(bishop2.getPosition()), "Problem: home-square 2");
                
        // numero di posizioni:
        assertEquals(7, positionCalculated1.size(), "Problem: number of legal movements 1");
        assertEquals(13, positionCalculated2.size(), "Problem: number of legal movements 2");

        // estremi:
        assertTrue(positionCalculated1.contains(new Position(7, 7)), "Extreme diagonal 1");  

        assertTrue(positionCalculated2.contains(new Position(0, 0)), "Extreme column 1");
        assertTrue(positionCalculated2.contains(new Position(0, 7)), "Extreme row 1");
        assertTrue(positionCalculated2.contains(new Position(7, 7)), "Extreme column 1");
        assertTrue(positionCalculated2.contains(new Position(6, 0)), "Extreme row 1");        
    }

    @Test
    void blockByAdversaryPieceBishop() {
        
        ChessBoard board = new ChessBoard();
        
        Bishop blackBishop = new Bishop(new Position(3,2),"black");
        Bishop whiteBishop = new Bishop(new Position(6,5),"white");

        board.setPiece(blackBishop);
        board.setPiece(whiteBishop);

        Set<Position> positionsForWhiteBishop = whiteBishop.getLegalMoves(board);

        // numero di mosse
        assertEquals(11, positionsForWhiteBishop.size(), "Problema: numero di mosse bloccate da pezzo avversario");

        // posizioni escluse
        assertFalse(positionsForWhiteBishop.contains(whiteBishop.getPosition()), "Problemea: posizione da escludere");
        assertFalse(positionsForWhiteBishop.contains(new Position(7, 6)), "problema: posizione da escludere");
    }

    @Test
    void blockByOwnPieceBishop(){
        ChessBoard board = new ChessBoard();
        
        Bishop bishop1 = new Bishop(new Position(3,2),"white");
        Bishop bishop2 = new Bishop(new Position(6,5),"white");

        board.setPiece(bishop1);
        board.setPiece(bishop2);

        Set<Position> positionsForBishop1 = bishop1.getLegalMoves(board);

        // numero di mosse
        assertEquals(10, positionsForBishop1.size(), "Problema: numero di mosse bloccate da pezzo avversario");

        // posizioni escluse
        assertFalse(positionsForBishop1.contains(bishop1.getPosition()), "Problemea: posizione da escludere");
        assertFalse(positionsForBishop1.contains(new Position(6, 5)), "problema: posizione da escludere");
        assertFalse(positionsForBishop1.contains(new Position(7, 6)), "problema: posizione da escludere");
    }

    
}