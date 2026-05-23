package org.chess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.chess.dataTypes.Position;
import org.chess.organization.ChessBoard;
import org.chess.pieces.*;
import org.junit.jupiter.api.Test;

/**
 * Test:
 * a. matrici di controllo
 *  1. copertura totale
 *  2. sovrapposizione di forze
 *  3. bloccco da pezzi propri
 * b. validazione mosse:
 *  1. fuoco amico
 *  2. King: 
 *     2.1 inchiodatura
 *     2.2 doppio scacco, mossa obbligata
 *     2.3 mossa obbligata sotto scacco
 * c. mosse speciali:
 *  1. enpassant
 *  2. arrocco corto 
 *  3. arrocco lungo
 *  4. promozione
 * d. test di integrità:
 *  1. validazione arrocco
 *  2. validazione enpassant *  
 *  3. validazione cella non vuota
 */


public class ChessBoardTest {

    ChessBoard settings(){
        ChessBoard board = new ChessBoard();
        // posizione bianco
        board.setPiece(new Rock(new Position(0,0), "white"));
        board.setPiece(new Pawn(new Position(1,0), "white"));
        board.setPiece(new Queen(new Position(2,2), "white"));
        board.setPiece(new King(new Position(0,4), "white"));
        board.setPiece(new Rock(new Position(0,7), "white"));
        board.setPiece(new Knight(new Position(2,6), "white"));

        // posizione nero
        board.setPiece(new Rock(new Position(7,0), "black"));
        board.setPiece(new Pawn(new Position(6,4), "black"));
        board.setPiece(new Queen(new Position(2,7), "black"));
        board.setPiece(new King(new Position(7,4), "black"));
        board.setPiece(new Rock(new Position(7,7), "black"));
        board.setPiece(new Knight(new Position(5,1), "black"));
        
        board.updateControl();

        return board;
    }
    
    // a. matrici di controllo
    
    @Test // a.1. copertura totale: dato un pezzo libero nella scacchiera, per ogni casa che controlla, la matrice di controllo deve essere settata a 1 nelle celle corrispondenti 
    void controlMapOpenBoard(){
        ChessBoard board = new ChessBoard();
        Rock rock = new Rock(new Position(3,3), "white");
        board.setPiece(rock);
        board.updateControl();
        int [][] squareControlled = board.getSquareControlledBy("white");

        int sumSquareControlledByRock = 0;
        
        for(int[] riga : squareControlled ) {
            for(int valore : riga) sumSquareControlledByRock += valore;
        }

        assertEquals(14, sumSquareControlledByRock, "La somma dei controlli non coincide!");
    }

    @Test // a.2. covrapposizione di forze: dati due pezzi nella scacchiera, per ogni casa che controlla, la matrice di controllo deve essere settata a come la somma dei due pezzi nelle celle corrispondenti 
    void controlMapTwoPieces(){
        ChessBoard board = new ChessBoard();
        Rock rock = new Rock(new Position(3,3), "white");
        Queen queen = new Queen(new Position(5,7), "white");
        board.setPiece(rock);
        board.setPiece(queen);
        board.updateControl();

        int[][] squareControlled = board.getSquareControlledBy("white");
        
        assertEquals(2, squareControlled[5][3], "matrice di controllo: control map two pieces");
    }

    @Test // a.3. bloccco da pezzi propri: se un pezzo controlla un altro pezzo dello stesso colore, la matrice di controllo deve essere a 1 nelle posizione del pezzo controllato
    void controlMapOwnPiececs(){
        ChessBoard board = settings();
        int[][] squareControlled = board.getSquareControlledBy("white");
        assertEquals(1, squareControlled[1][0], "matrice di controllo: control map own pieces");
    }

    // b. Validazione mosse

    @Test // b.1 fuoco amico: se la mossa prevede di mangiare un pezzo proprio, impedirlo
    void cannotCaptureOwnPiece(){
        ChessBoard board = new ChessBoard();
        Bishop bishop = new Bishop(new Position(7,1), "white");
        Pawn pawn = new Pawn(new Position(5,3), "white");
        board.setPiece(pawn);
        board.setPiece(bishop);
        assertFalse(board.isMoveLegal(bishop.getPosition(),new Position(5,3)));
    }

    @Test
    void pawnForwardMoveVaidation(){
        ChessBoard board = new ChessBoard();
        Pawn whitePawn = new Pawn(new Position(5,4), "white");
        Pawn blackPawn = new Pawn(new Position(4,4), "black");
        board.setPiece(whitePawn);
        board.setPiece(blackPawn);
        Position whitePawnPosition=whitePawn.getPosition();
        Position blackPawnPosition=blackPawn.getPosition();
        assertFalse(board.isMoveLegal(whitePawnPosition,new Position(4,4)));
        assertFalse(board.isMoveLegal(blackPawnPosition,new Position(5,4)));
    }


    @Test 
    void pinnedKing(){
        
    }


}
