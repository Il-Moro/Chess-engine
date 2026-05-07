package org.chess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.chess.dataTypes.Position;
import org.chess.organization.ChessBoard;
import org.chess.pieces.Pawn;
import org.chess.pieces.Queen;
import org.chess.pieces.Rock;
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
 */


public class ChessBoardTest {
    
    // a. matrici di controllo
    
    @Test // 1. copertura totale: dato un pezzo libero nella scacchiera, per ogni casa che controlla, la matrice di controllo deve essere settata a 1 nelle celle corrispondenti 
    void controlMapOpenBoard(){
        ChessBoard board = new ChessBoard();
        Rock rock = new Rock(new Position(3,3), "white");
        board.setPiece(rock);
        int [][] squareControlled = board.getSquareControlledBy("white");

        int sumSquareControlledByRock = 0;
        
        for(int[] riga : squareControlled ) {
            for(int valore : riga) sumSquareControlledByRock += valore;
        }

        assertEquals(14, sumSquareControlledByRock, "La somma dei controlli non coincide!");
    }

    @Test // 2. covrapposizione di forze: dati due pezzi nella scacchiera, per ogni casa che controlla, la matrice di controllo deve essere settata a come la somma dei due pezzi nelle celle corrispondenti 
    void controlMapTwoPieces(){
        ChessBoard board = new ChessBoard();
        Rock rock = new Rock(new Position(3,3), "white");
        Queen queen = new Queen(new Position(5,7), "white");
        board.setPiece(rock);
        board.setPiece(queen);

        int[][] squareControlled = board.getSquareControlledBy("white");
        
        assertEquals(2, squareControlled[5][3], "matrice di controllo: control map two pieces");
    }

    @Test // 3. bloccco da pezzi propri: se un pezzo controlla un altro pezzo dello stesso colore, la matrice di controllo deve essere a 1 nelle posizione del pezzo controllato
    void controlMapOwnPiececs(){
        ChessBoard board = new ChessBoard();
        Rock rock = new Rock(new Position(0,0), "white");
        Pawn pawn = new Pawn(new Position(1,0), "white");
        board.setPiece(rock);
        board.setPiece(pawn);

        int[][] squareControlled = board.getSquareControlledBy("white");
        
        assertEquals(1, squareControlled[1][0], "matrice di controllo: control map own pieces");
    }


}
