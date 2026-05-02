package org.chess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.chess.dataTypes.Position;
import org.chess.organization.ChessBoard;
import org.chess.pieces.King;
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
    void controlMatrix(){
        ChessBoard board = new ChessBoard();
        Rock rock = new Rock(new Position(3,3), "white");
        board.setPiece(rock);
        int [][] SquareControlledByRock = board.getSquareControlledBy("white");

        int sumSquareControlledByRock = 0;
        
        for(int[] riga : SquareControlledByRock ) {
            for(int valore : riga) sumSquareControlledByRock += valore;
        }

        assertEquals(14, sumSquareControlledByRock, "La somma dei controlli non coincide!");
    }
}
