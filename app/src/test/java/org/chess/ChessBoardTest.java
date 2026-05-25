package org.chess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.chess.dataTypes.Position;
import org.chess.organization.ChessBoard;
import org.chess.pieces.Bishop;
import org.chess.pieces.King;
import org.chess.pieces.Knight;
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
 *  3. validazione cella non vuota
 */


public class ChessBoardTest {

    ChessBoard settings(){
        ChessBoard board = new ChessBoard();
        // posizione bianco
        board.setPiece(new Rock(new Position(0,0), "white"));
        board.setPiece(new Pawn(new Position(1,0), "white"));
        board.setPiece(new Queen(new Position(2,2), "white"));
        board.setPiece(new King(new Position(0,4), "white", false));
        board.setPiece(new Rock(new Position(0,7), "white"));
        board.setPiece(new Bishop(new Position(2,6), "white"));

        // posizione nero
        board.setPiece(new Rock(new Position(7,0), "black"));
        board.setPiece(new Pawn(new Position(6,4), "black"));
        board.setPiece(new Queen(new Position(2,7), "black"));
        board.setPiece(new King(new Position(7,4), "black", false));
        board.setPiece(new Rock(new Position(7,7), "black"));
        board.setPiece(new Knight(new Position(5,1), "black"));
        board.setPiece(new Bishop(new Position(3,7), "black"));
        
        board.updateControl();

        return board;
    }

    ChessBoard setOnlyKing(){
        ChessBoard board = new ChessBoard();
        // posizione bianco
        board.setPiece(new King(new Position(0,4), "white", false));
        // posizione nero
        board.setPiece(new King(new Position(7,4), "black", false));
        
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
    void pawnForwardMoveValidation(){
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
    void enPassant(){
        ChessBoard board = new ChessBoard();
        Pawn blackPawn = new Pawn(new Position(6,4), "black");
        Pawn whitePawn = new Pawn(new Position(4,3), "white");
        
        board.setPiece(new King(new Position(7,4), "black"));
        board.setPiece(new King(new Position(0,4), "white"));
        board.setPiece(whitePawn);
        board.setPiece(blackPawn);
        // EN PASSANT VALIDO
        // Sposto il bianco di 2
        assertTrue(board.isMoveLegal(blackPawn.getPosition(), new Position(4,4)));
        board.physicalMovement(blackPawn.getPosition(),new Position(4,4));
        // Posso eseguire l' en passant
        assertTrue(board.isMoveLegal(whitePawn.getPosition(),new Position(5,4)));
        
        board = new ChessBoard();
        whitePawn = new Pawn(new Position(3,4), "black");
        blackPawn = new Pawn(new Position(1,3), "white");
        
        board.setPiece(whitePawn);
        board.setPiece(blackPawn);
        board.setPiece(new King(new Position(7,4), "black"));
        board.setPiece(new King(new Position(0,4), "white"));

        assertTrue(board.isMoveLegal(blackPawn.getPosition(),new Position(3,3)));
        board.physicalMovement(blackPawn.getPosition(), new Position(3,3));
        assertTrue(board.isMoveLegal(whitePawn.getPosition(),new Position(2,3)));

        // EN PASSANT NON VALIDO

        board = new ChessBoard();
        whitePawn = new Pawn(new Position(6,4), "black");
        blackPawn = new Pawn(new Position(4,3), "white");
        Rock whiteRock = new Rock(new Position(7,0),"white");
        Rock blackRock = new Rock(new Position(0,0) , "white");
        board.setPiece(new King(new Position(7,4), "black"));
        board.setPiece(new King(new Position(0,4), "white"));

        board.setPiece(whitePawn);
        board.setPiece(blackPawn);
        board.setPiece(whiteRock);
        board.setPiece(blackRock);

        assertTrue(board.isMoveLegal(whitePawn.getPosition(),new Position(4,4)));
        board.physicalMovement(whitePawn.getPosition(), new Position(4,4));
        assertTrue(board.isMoveLegal(blackRock.getPosition(),new Position(1,0)));
        board.physicalMovement(blackRock.getPosition(), new Position(1,0));
        assertTrue(board.isMoveLegal(whiteRock.getPosition(),new Position(6,0)));
        board.physicalMovement(whiteRock.getPosition(), new Position(6,0));

        assertFalse(board.isMoveLegal(blackPawn.getPosition(),new Position(5,4)));

        board = new ChessBoard();
        whitePawn = new Pawn(new Position(5,4), "black");
        blackPawn = new Pawn(new Position(4,3), "white");
        
        board.setPiece(new King(new Position(7,4), "white"));
        board.setPiece(new King(new Position(0,4), "black"));
        board.setPiece(whitePawn);
        board.setPiece(blackPawn);

        assertTrue(board.isMoveLegal(whitePawn.getPosition(),new Position(4,4)));
        board.physicalMovement(whitePawn.getPosition(), new Position(4,4));
        assertFalse(board.isMoveLegal(blackPawn.getPosition(),new Position(5,4)));


        board = new ChessBoard();
        board.setPiece(new King(new Position(7,4), "black"));
        board.setPiece(new King(new Position(0,4), "white"));
        whitePawn = new Pawn(new Position(3,4), "black");
        blackPawn = new Pawn(new Position(2,3), "white");
        
        board.setPiece(whitePawn);
        board.setPiece(blackPawn);

        assertTrue(board.isMoveLegal(blackPawn.getPosition(),new Position(3,3)));
        board.physicalMovement(blackPawn.getPosition(), new Position(3,3));
        assertFalse(board.isMoveLegal(whitePawn.getPosition(),new Position(2,3)));
    }

    @Test 
    void kingToSquareControlledByAdversary(){
        ChessBoard board = settings();

        assertFalse(board.isMoveLegal(new Position(0,4), new Position(0,5)), "Il re non può spostarsi su case controllate da avversari");
        assertFalse(board.isMoveLegal(new Position(0,4), new Position(3,4)), "mossa fuori da getPotentialMoves()");
    }

    @Test 
    void pinnedBishopDiagonal(){
        ChessBoard board = setOnlyKing();
        // Alfiere bianco in (2,6), inchiodato dall'Alfiere nero in (3,7) sulla diagonale del Re (0,4)
        board.setPiece(new Bishop(new Position(2, 6), "white"));
        board.setPiece(new Bishop(new Position(3, 7), "black"));
        
        board.updateControl();
        board.updateKingPin("white");

        // Mossa ILLEGALE: uscire dalla diagonale andando in (4,4)
        assertFalse(board.isMoveLegal(new Position(2, 6), new Position(4,4)), "diagonale non consentita");
        // Mossa LEGALE: scorrere sulla diagonale di pin verso il Re in (1,5)
        assertTrue(board.isMoveLegal(new Position(2, 6), new Position(1,5)), "diagonale consentita");
    }

    @Test 
    void pinnedGenericDiagonal(){
        ChessBoard board = setOnlyKing();
        // Mettiamo un Pedone bianco in (1,5) inchiodato lungo la stessa diagonale da una Regina nera in (4,8)
        board.setPiece(new Pawn(new Position(1, 5), "white"));
        board.setPiece(new Queen(new Position(3, 7), "black"));
        
        board.updateControl();
        board.updateKingPin("white");

        // Un pedone pinnato in diagonale non può MAI muoversi in avanti (riga 2, colonna 5), perché uscirebbe dalla linea
        assertFalse(board.isMoveLegal(new Position(1, 5), new Position(2, 5)), "Il pedone non può avanzare se il pin è diagonale");
    }

    @Test
    void pinnedRookOrtogonal(){
        ChessBoard board = setOnlyKing();
        // Torre bianca in (2,4) sulla stessa colonna del Re (0,4), inchiodata da una Torre nera in (5,4)
        board.setPiece(new Rock(new Position(2, 4), "white"));
        board.setPiece(new Rock(new Position(5, 4), "black"));
        
        board.updateControl();
        board.updateKingPin("white");

        // Mossa ILLEGALE: muoversi lateralmente sulla riga in (2,5) rompe il pin
        assertFalse(board.isMoveLegal(new Position(2, 4), new Position(2, 5)), "Spostamento laterale non consentito per pin ortogonale");
        // Mossa LEGALE: scorrere sulla colonna di pin avvicinandosi al Re in (1,4)
        assertTrue(board.isMoveLegal(new Position(2, 4), new Position(1, 4)), "Spostamento lungo la colonna di pin consentito");
    }

    @Test
    void pinnedGenericOrtogonal(){
        ChessBoard board = setOnlyKing();
        // Regina bianca in (0,5) sulla stessa riga del Re (0,4), inchiodata da una Torre nera in (0,7)
        board.setPiece(new Queen(new Position(0, 5), "white"));
        board.setPiece(new Rock(new Position(0, 7), "black"));
        
        board.updateControl();
        board.updateKingPin("white");

        // Mossa ILLEGALE: muoversi in diagonale fuori dalla riga in (1,6)
        assertFalse(board.isMoveLegal(new Position(0, 5), new Position(1, 6)), "La regina non può uscire dalla riga di pin");
        // Mossa LEGALE: scorrere sulla riga verso l'attaccante per mangiarlo in (0,7)
        assertTrue(board.isMoveLegal(new Position(0, 5), new Position(0, 7)), "La regina può mangiare il pezzo che la inchioda");
    }

    @Test
    void pinnedKingUnderCheck(){
        ChessBoard board = setOnlyKing();
        // Il Re è sotto scacco lineare da una Torre nera in (4,4)
        board.setPiece(new Rock(new Position(4, 4), "black"));
        // C'è un Alfiere bianco libero in (2,2) che può intercettare lo scacco in (2,4)
        board.setPiece(new Bishop(new Position(2, 2), "white"));
        
        board.updateControl();
        board.updateKingPin("white");

        // Mossa ILLEGALE: l'alfiere si muove in una casa a caso (3,3) ignorando lo scacco al Re
        assertFalse(board.isMoveLegal(new Position(2, 2), new Position(3, 3)), "Mossa non consentita, il Re rimane sotto scacco");
        // Mossa LEGALE: l'alfiere si mette in mezzo in (2,4) per coprire il Re (CHECK_PATH)
        assertTrue(board.isMoveLegal(new Position(2, 2), new Position(4, 4)), "Mossa consentita, intercetta la linea di scacco");
    }

    @Test
    void kingCheckByKnight(){
        ChessBoard board = setOnlyKing();
        // Il Re è sotto scacco da un Cavallo nero in (2,5)
        board.setPiece(new Knight(new Position(2, 5), "black"));
        // C'è una Regina bianca in (2,2) che può mangiare il cavallo
        board.setPiece(new Queen(new Position(2, 2), "white"));
        
        board.updateControl();
        board.updateKingPin("white");

        // Mossa ILLEGALE: la regina prova a mettersi in mezzo in (1,4). Contro un cavallo non serve a nulla parare la linea
        assertFalse(board.isMoveLegal(new Position(2, 2), new Position(1, 4)), "Non si può intercettare la traiettoria di un cavallo");
        // Mossa LEGALE: la regina si sposta in (2,5) e cattura direttamente il cavallo (KING_ATTACKER)
        assertTrue(board.isMoveLegal(new Position(2, 2), new Position(2, 5)), "È legale catturare il cavallo che dà scacco");
    }

    @Test
    void doubleCheck(){
        ChessBoard board = setOnlyKing();
        // Re sotto DOPPIO scacco simultaneo: Torre nera in (4,4) e Cavallo nero in (2,5)
        board.setPiece(new Rock(new Position(4, 4), "black"));
        board.setPiece(new Knight(new Position(2, 5), "black"));
        // C'è una Regina bianca in (2,2) che teoricamente potrebbe mangiare il cavallo
        board.setPiece(new Queen(new Position(2, 2), "white"));
        
        board.updateControl();
        board.updateKingPin("white");

        // Mossa ILLEGALE: in doppio scacco, qualsiasi mossa di pezzi che non siano il Re deve essere bloccata (ritorna false)
        assertFalse(board.isMoveLegal(new Position(2, 2), new Position(2, 5)), "Sotto doppio scacco le mosse degli altri pezzi sono vietate");
        // Mossa LEGALE: il Re si sposta di un passo in una casa non controllata (0,3)
        assertTrue(board.isMoveLegal(new Position(0, 4), new Position(0, 3)), "Sotto doppio scacco solo il Re può muoversi");
    }
}
