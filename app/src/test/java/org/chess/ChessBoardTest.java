package org.chess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.chess.dataTypes.Colour;
import org.chess.dataTypes.End;
import org.chess.dataTypes.Position;
import org.chess.dataTypes.SpecialMoves;
import org.chess.dataTypes.UndoInfo;
import org.chess.organization.ChessBoard;
import org.chess.pieces.Bishop;
import org.chess.pieces.King;
import org.chess.pieces.Knight;
import org.chess.pieces.Pawn;
import org.chess.pieces.Piece;
import org.chess.pieces.Queen;
import org.chess.pieces.Rock;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
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
        board.setPiece(new Rock(new Position(0,0), Colour.WHITE));
        board.setPiece(new Pawn(new Position(1,0), Colour.WHITE));
        board.setPiece(new Queen(new Position(2,2), Colour.WHITE));
        board.setPiece(new King(new Position(0,4), Colour.WHITE, false));
        board.setPiece(new Rock(new Position(0,7), Colour.WHITE));
        board.setPiece(new Bishop(new Position(2,6), Colour.WHITE));

        // posizione nero
        board.setPiece(new Rock(new Position(7,0), Colour.BLACK));
        board.setPiece(new Pawn(new Position(6,4), Colour.BLACK));
        board.setPiece(new Queen(new Position(2,7), Colour.BLACK));
        board.setPiece(new King(new Position(7,4), Colour.BLACK, false));
        board.setPiece(new Rock(new Position(7,7), Colour.BLACK));
        board.setPiece(new Knight(new Position(5,1), Colour.BLACK));
        board.setPiece(new Bishop(new Position(3,7), Colour.BLACK));
        
        board.updateControl();

        return board;
    }

    ChessBoard setOnlyKing(){
        ChessBoard board = new ChessBoard();
        // posizione bianco
        board.setPiece(new King(new Position(0,4), Colour.WHITE, false));
        // posizione nero
        board.setPiece(new King(new Position(7,4), Colour.BLACK, false));
        
        board.updateControl();

        return board;
    }
    
    // a. matrici di controllo
    
    @Test // a.1. copertura totale: dato un pezzo libero nella scacchiera, per ogni casa che controlla, la matrice di controllo deve essere settata a 1 nelle celle corrispondenti 
    void controlMapOpenBoard(){
        ChessBoard board = new ChessBoard();
        Rock rock = new Rock(new Position(3,3), Colour.WHITE);
        board.setPiece(rock);
        board.updateControl();
        int [][] squareControlled = board.getSquareControlledBy(Colour.WHITE);

        int sumSquareControlledByRock = 0;
        
        for(int[] riga : squareControlled ) {
            for(int valore : riga) sumSquareControlledByRock += valore;
        }

        assertEquals(14, sumSquareControlledByRock, "La somma dei controlli non coincide!");
    }

    @Test // a.2. covrapposizione di forze: dati due pezzi nella scacchiera, per ogni casa che controlla, la matrice di controllo deve essere settata a come la somma dei due pezzi nelle celle corrispondenti 
    void controlMapTwoPieces(){
        ChessBoard board = new ChessBoard();
        Rock rock = new Rock(new Position(3,3), Colour.WHITE);
        Queen queen = new Queen(new Position(5,7), Colour.WHITE);
        board.setPiece(rock);
        board.setPiece(queen);
        board.updateControl();

        int[][] squareControlled = board.getSquareControlledBy(Colour.WHITE);
        
        assertEquals(2, squareControlled[5][3], "matrice di controllo: control map two pieces");
    }

    @Test // a.3. bloccco da pezzi propri: se un pezzo controlla un altro pezzo dello stesso colore, la matrice di controllo deve essere a 1 nelle posizione del pezzo controllato
    void controlMapOwnPiececs(){
        ChessBoard board = settings();
        int[][] squareControlled = board.getSquareControlledBy(Colour.WHITE);
        assertEquals(1, squareControlled[1][0], "matrice di controllo: control map own pieces");
    }

    // b. Validazione mosse

    @Test // b.1 fuoco amico: se la mossa prevede di mangiare un pezzo proprio, impedirlo
    void cannotCaptureOwnPiece(){
        ChessBoard board = new ChessBoard();
        Bishop bishop = new Bishop(new Position(7,1), Colour.WHITE);
        Pawn pawn = new Pawn(new Position(5,3), Colour.WHITE);
        board.setPiece(pawn);
        board.setPiece(bishop);
        assertFalse(board.isMoveLegal(bishop.getPosition(),new Position(5,3)));
    }

    @Test
    void pawnForwardMoveValidation(){
        ChessBoard board = new ChessBoard();
        Pawn whitePawn = new Pawn(new Position(5,4), Colour.WHITE);
        Pawn blackPawn = new Pawn(new Position(4,4), Colour.BLACK);
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
        Pawn blackPawn = new Pawn(new Position(6,4), Colour.BLACK);
        Pawn whitePawn = new Pawn(new Position(4,3), Colour.WHITE);
        
        board.setPiece(new King(new Position(7,4), Colour.BLACK));
        board.setPiece(new King(new Position(0,4), Colour.WHITE));
        board.setPiece(whitePawn);
        board.setPiece(blackPawn);
        // EN PASSANT VALIDO
        // Sposto il bianco di 2
        assertTrue(board.isMoveLegal(blackPawn.getPosition(), new Position(4,4)));
        board.physicalMovement(blackPawn.getPosition(), new Position(4,4));
        // Posso eseguire l' en passant
        assertTrue(board.isMoveLegal(whitePawn.getPosition(),new Position(5,4)));
        
        board = new ChessBoard();
        whitePawn = new Pawn(new Position(3,4), Colour.BLACK);
        blackPawn = new Pawn(new Position(1,3), Colour.WHITE);
        
        board.setPiece(whitePawn);
        board.setPiece(blackPawn);
        board.setPiece(new King(new Position(7,4), Colour.BLACK));
        board.setPiece(new King(new Position(0,4), Colour.WHITE));

        assertTrue(board.isMoveLegal(blackPawn.getPosition(),new Position(3,3)));
        board.physicalMovement(blackPawn.getPosition(), new Position(3,3));
        assertTrue(board.isMoveLegal(whitePawn.getPosition(),new Position(2,3)));

        // EN PASSANT NON VALIDO

        board = new ChessBoard();
        whitePawn = new Pawn(new Position(6,4), Colour.BLACK);
        blackPawn = new Pawn(new Position(4,3), Colour.WHITE);
        Rock whiteRock = new Rock(new Position(7,0),Colour.WHITE);
        Rock blackRock = new Rock(new Position(0,0) , Colour.WHITE);
        board.setPiece(new King(new Position(7,4), Colour.BLACK));
        board.setPiece(new King(new Position(0,4), Colour.WHITE));

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
        whitePawn = new Pawn(new Position(5,4), Colour.BLACK);
        blackPawn = new Pawn(new Position(4,3), Colour.WHITE);
        
        board.setPiece(new King(new Position(7,4), Colour.WHITE));
        board.setPiece(new King(new Position(0,4), Colour.BLACK));
        board.setPiece(whitePawn);
        board.setPiece(blackPawn);

        assertTrue(board.isMoveLegal(whitePawn.getPosition(),new Position(4,4)));
        board.physicalMovement(whitePawn.getPosition(), new Position(4,4));
        assertFalse(board.isMoveLegal(blackPawn.getPosition(),new Position(5,4)));


        board = new ChessBoard();
        board.setPiece(new King(new Position(7,4), Colour.BLACK));
        board.setPiece(new King(new Position(0,4), Colour.WHITE));
        whitePawn = new Pawn(new Position(3,4), Colour.BLACK);
        blackPawn = new Pawn(new Position(2,3), Colour.WHITE);
        
        board.setPiece(whitePawn);
        board.setPiece(blackPawn);

        assertTrue(board.isMoveLegal(blackPawn.getPosition(),new Position(3,3)));
        board.physicalMovement(blackPawn.getPosition(), new Position(3,3));
        assertFalse(board.isMoveLegal(whitePawn.getPosition(),new Position(2,3)));
    }

    @Test
    void enpassantPhysicalMovement(){
        ChessBoard board= new ChessBoard();
        Pawn blackPawn = new Pawn(new Position(6,4), Colour.BLACK);
        Pawn whitePawn = new Pawn(new Position(4,3), Colour.WHITE);
        board.setPiece(new King(new Position(7,4), Colour.BLACK));
        board.setPiece(new King(new Position(0,4), Colour.WHITE));
        board.setPiece(whitePawn);
        board.setPiece(blackPawn);

        assertTrue(board.isMoveLegal(blackPawn.getPosition(), new Position(4,4)));
        board.physicalMovement(blackPawn.getPosition(), new Position(4,4));
        assertFalse(board.isNull(new Position(4,4)));
        
        assertTrue(board.isMoveLegal(whitePawn.getPosition(),new Position(5,4)));
        board.physicalMovement(whitePawn.getPosition(), new Position(5,4));
        assertFalse(board.isNull(new Position(5,4)));
        
        // Verify if enpassant movement was done well
        assertTrue(board.isNull(new Position(4,4)));
        assertTrue(board.isNull(new Position(6,4)));
        assertTrue(board.isNull(new Position(4,3)));
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
        board.setPiece(new Bishop(new Position(2, 6), Colour.WHITE));
        board.setPiece(new Bishop(new Position(3, 7), Colour.BLACK));
        
        board.updateControl();
        board.updateKingPin();

        // Mossa ILLEGALE: uscire dalla diagonale andando in (4,4)
        assertFalse(board.isMoveLegal(new Position(2, 6), new Position(4,4)), "diagonale non consentita");
        // Mossa LEGALE: scorrere sulla diagonale di pin verso il Re in (1,5)
        assertTrue(board.isMoveLegal(new Position(2, 6), new Position(1,5)), "diagonale consentita");
    }

    @Test 
    void pinnedGenericDiagonal(){
        ChessBoard board = setOnlyKing();
        // Mettiamo un Pedone bianco in (1,5) inchiodato lungo la stessa diagonale da una Regina nera in (4,8)
        board.setPiece(new Pawn(new Position(1, 5), Colour.WHITE));
        board.setPiece(new Queen(new Position(3, 7), Colour.BLACK));
        
        board.updateControl();
        board.updateKingPin();

        // Un pedone pinnato in diagonale non può MAI muoversi in avanti (riga 2, colonna 5), perché uscirebbe dalla linea
        assertFalse(board.isMoveLegal(new Position(1, 5), new Position(2, 5)), "Il pedone non può avanzare se il pin è diagonale");
    }

    @Test
    void pinnedRookOrtogonal(){
        ChessBoard board = setOnlyKing();
        // Torre bianca in (2,4) sulla stessa colonna del Re (0,4), inchiodata da una Torre nera in (5,4)
        board.setPiece(new Rock(new Position(2, 4), Colour.WHITE));
        board.setPiece(new Rock(new Position(5, 4), Colour.BLACK));
        
        board.updateControl();
        board.updateKingPin();

        // Mossa ILLEGALE: muoversi lateralmente sulla riga in (2,5) rompe il pin
        assertFalse(board.isMoveLegal(new Position(2, 4), new Position(2, 5)), "Spostamento laterale non consentito per pin ortogonale");
        // Mossa LEGALE: scorrere sulla colonna di pin avvicinandosi al Re in (1,4)
        assertTrue(board.isMoveLegal(new Position(2, 4), new Position(1, 4)), "Spostamento lungo la colonna di pin consentito");
    }

    @Test
    void pinnedGenericOrtogonal(){
        ChessBoard board = setOnlyKing();
        // Regina bianca in (0,5) sulla stessa riga del Re (0,4), inchiodata da una Torre nera in (0,7)
        board.setPiece(new Queen(new Position(0, 5), Colour.WHITE));
        board.setPiece(new Rock(new Position(0, 7), Colour.BLACK));
        
        board.updateControl();
        board.updateKingPin();

        // Mossa ILLEGALE: muoversi in diagonale fuori dalla riga in (1,6)
        assertFalse(board.isMoveLegal(new Position(0, 5), new Position(1, 6)), "La regina non può uscire dalla riga di pin");
        // Mossa LEGALE: scorrere sulla riga verso l'attaccante per mangiarlo in (0,7)
        assertTrue(board.isMoveLegal(new Position(0, 5), new Position(0, 7)), "La regina può mangiare il pezzo che la inchioda");
    }

    @Test
    void pinnedKingUnderCheck(){
        ChessBoard board = setOnlyKing();
        // Il Re è sotto scacco lineare da una Torre nera in (4,4)
        board.setPiece(new Rock(new Position(4, 4), Colour.BLACK));
        // C'è un Alfiere bianco libero in (2,2) che può intercettare lo scacco in (2,4)
        board.setPiece(new Bishop(new Position(2, 2), Colour.WHITE));
        
        board.updateControl();
        board.updateKingPin();

        // Mossa ILLEGALE: l'alfiere si muove in una casa a caso (3,3) ignorando lo scacco al Re
        assertFalse(board.isMoveLegal(new Position(2, 2), new Position(3, 3)), "Mossa non consentita, il Re rimane sotto scacco");
        // Mossa LEGALE: l'alfiere si mette in mezzo in (2,4) per coprire il Re (CHECK_PATH)
        assertTrue(board.isMoveLegal(new Position(2, 2), new Position(4, 4)), "Mossa consentita, intercetta la linea di scacco");
    }

    @Test
    void kingCheckByKnight(){
        ChessBoard board = setOnlyKing();
        // Il Re è sotto scacco da un Cavallo nero in (2,5)
        board.setPiece(new Knight(new Position(2, 5), Colour.BLACK));
        // C'è una Regina bianca in (2,2) che può mangiare il cavallo
        board.setPiece(new Queen(new Position(2, 2), Colour.WHITE));
        
        board.updateControl();
        board.updateKingPin();

        // Mossa ILLEGALE: la regina prova a mettersi in mezzo in (1,4). Contro un cavallo non serve a nulla parare la linea
        assertFalse(board.isMoveLegal(new Position(2, 2), new Position(1, 4)), "Non si può intercettare la traiettoria di un cavallo");
        // Mossa LEGALE: la regina si sposta in (2,5) e cattura direttamente il cavallo (KING_ATTACKER)
        assertTrue(board.isMoveLegal(new Position(2, 2), new Position(2, 5)), "È legale catturare il cavallo che dà scacco");
    }

    @Test
    void doubleCheck(){
        ChessBoard board = setOnlyKing();
        // Re sotto DOPPIO scacco simultaneo: Torre nera in (4,4) e Cavallo nero in (2,5)
        board.setPiece(new Rock(new Position(4, 4), Colour.BLACK));
        board.setPiece(new Knight(new Position(2, 5), Colour.BLACK));
        // C'è una Regina bianca in (2,2) che teoricamente potrebbe mangiare il cavallo
        board.setPiece(new Queen(new Position(2, 2), Colour.WHITE));
        
        board.updateControl();
        board.updateKingPin();

        // Mossa ILLEGALE: in doppio scacco, qualsiasi mossa di pezzi che non siano il Re deve essere bloccata (ritorna false)
        assertFalse(board.isMoveLegal(new Position(2, 2), new Position(2, 5)), "Sotto doppio scacco le mosse degli altri pezzi sono vietate");
        // Mossa LEGALE: il Re si sposta di un passo in una casa non controllata (0,3)
        assertTrue(board.isMoveLegal(new Position(0, 4), new Position(0, 3)), "Sotto doppio scacco solo il Re può muoversi");
    }
    
    @Test 
    void shortCastleWhite(){
        ChessBoard board = settings();
        
        // Spostiamo l'Alfiere nero in (3,1) per dare scacco diretto al Re bianco in (0,4)
        // Nota: rimuoviamo quello vecchio in (3,7) per pulizia
        board.setNull(new Position(3,7));
        board.setPiece(new Bishop(new Position(3, 1), Colour.BLACK));
        
        board.updateControl();
        board.updateKingPin(); // Questo imposterà UNDER_CHECK_LINE su (0,4)

        // Il Re è sotto scacco -> L'arrocco corto deve essere cancellato
        assertFalse(board.isMoveLegal(new Position(0, 4), new Position(0, 6)), "Arrocco corto bianco non consentito: il Re è sotto scacco");
    }

    @Test
    void longCastleWhite(){
        ChessBoard board = settings();
        board.updateKingPin();

        // Il lungo del bianco rimane CONSENTITO (nessuno scacco, case vuote e sicure)
        assertTrue(board.isMoveLegal(new Position(0, 4), new Position(0, 2)), "Arrocco lungo bianco consentito");
    }

    @Test
    void shortCastleBlack(){
        ChessBoard board = settings();
        board.updateKingPin();

        // Lo short del nero rimane CONSENTITO
        assertTrue(board.isMoveLegal(new Position(7, 4), new Position(7, 6)), "Arrocco corto nero consentito");
    }

    @Test
    void longCastleBlack(){
        ChessBoard board = settings();
        board.updateKingPin();

        // Il lungo del nero rimane NON CONSENTITO perché la Regina bianca in (2,2) 
        // controlla la casa di arrivo (7,2) sulla colonna 2
        assertFalse(board.isMoveLegal(new Position(7, 4), new Position(7, 2)), "Arrocco lungo nero non consentito: la casa di arrivo (7,2) è controllata");
    }
    @Test
    void castleFailedBecauseKingMoved() {
        ChessBoard board = new ChessBoard();
        
        // Prendiamo il Re bianco e simuliamo che si sia GIÀ mosso in precedenza
        board.setPiece(new King(new Position(0,4), Colour.WHITE, true));  
        board.setPiece(new King(new Position(0, 4), Colour.BLACK));      
        board.setPiece(new Rock(new Position(0, 7), Colour.WHITE, false));
        
        board.updateControl();
        board.updateKingPin();

        // Il Re ha mosso -> Arrocco nullo
        assertFalse(board.isMoveLegal(new Position(0, 4), new Position(0, 6)), "Arrocco illegale: il Re si è già mosso precedentemente nella partita");
    }

    @Test
    void castleFailedBecauseRookMoved() {
        ChessBoard board = new ChessBoard();

        // Il Re bianco non ha mai mosso (flag a false)
        board.setPiece(new King(new Position(0, 4), Colour.WHITE,false));
        board.setPiece(new King(new Position(0, 4), Colour.BLACK));
        
        // La Torre nell'angolo (0,7) si è GIÀ mossa in precedenza (flag a true)
        Rock rookMoved = new Rock(new Position(0, 7), Colour.WHITE, true);
        board.setPiece(rookMoved);
        
        board.updateControl();
        board.updateKingPin();

        // La torre ha mosso -> Arrocco corto nullo
        assertFalse(board.isMoveLegal(new Position(0, 4), new Position(0, 6)), "Arrocco illegale: la Torre dell'arrocco corto ha già mosso");
    }

    @Test
    void castleFailedBecausePieceInBetween() {
        ChessBoard board = setOnlyKing();
        board.setPiece(new Rock(new Position(0, 7), Colour.WHITE));
        
        // Piazziamo un Cavallo bianco in (0,6) che blocca la strada
        board.setPiece(new Knight(new Position(0, 6), Colour.WHITE));
        
        board.updateControl();
        board.updateKingPin();

        assertFalse(board.isMoveLegal(new Position(0, 4), new Position(0, 6)), "Arrocco illegale: c'è un pezzo (Cavallo) di intralcio tra il Re e la Torre");
    }

    @Test
    void castleFailedBecauseRookIsMissing() {
        ChessBoard board = setOnlyKing();
        
        board.setPiece(new Queen(new Position(0, 7), Colour.WHITE));
        
        board.updateControl();
        board.updateKingPin();

        // Non c'è la torre nell'angolo -> l'arrocco non esiste geometricamente
        assertFalse(board.isMoveLegal(new Position(0, 4), new Position(0, 6)), "Arrocco illegale: non si può arroccare con una Regina al posto della Torre");
    }

    @Test
    void castleRookUnderAttackIsLegal() {
        ChessBoard board = setOnlyKing();
        board.setPiece(new Rock(new Position(0, 7), Colour.WHITE));
        
        // Caso da regolamento ufficiale FIDE: la TORRE è sotto attacco, ma il Re no.
        board.setPiece(new Bishop(new Position(3, 4), Colour.BLACK));
        
        board.updateControl();
        board.updateKingPin();

        // Regola FIDE: Se la Torre è sotto attacco (o la casa b1/b8 nell'arrocco lungo), l'arrocco è comunque legale
        // L'importante è che non siano sotto attacco il Re, la casa di passaggio del Re, e la casa di arrivo del Re.
        assertTrue(board.isMoveLegal(new Position(0, 4), new Position(0, 6)), "Arrocco legale: la Torre sotto attacco non impedisce l'arrocco");
    }

    @Test 
    void chechMate(){
        ChessBoard board = new ChessBoard();
        board.setPiece(new King(new Position(0,0), Colour.WHITE, true));
        board.setPiece(new King(new Position(7,0), Colour.BLACK, true));
        board.setPiece(new Rock(new Position(0,7), Colour.BLACK, true));
        board.setPiece(new Queen(new Position(1,7), Colour.BLACK));
        board.updateControl();
        board.updateKingPin();

        assertEquals(End.CHECKMATE, board.isCheckmateOrStalemate(Colour.WHITE));
    }

    @Test 
    void staleMate(){
        ChessBoard board = new ChessBoard();
        board.setPiece(new King(new Position(0,0), Colour.WHITE, true));
        board.setPiece(new King(new Position(7,0), Colour.BLACK, true));
        board.setPiece(new Rock(new Position(7,1), Colour.BLACK, true));
        board.setPiece(new Queen(new Position(1,7), Colour.BLACK));
        board.updateControl();
        board.updateKingPin();

        assertEquals(End.STALEMATE, board.isCheckmateOrStalemate(Colour.WHITE));
    }

    @Test
    void whiteCastles() {
        ChessBoard board = new ChessBoard(true); // Configurazione iniziale standard

        // arrocco corto
        // libero le case f1 (0,5) e g1 (0,6) togliendo Alfiere e Cavallo
        board.setNull(new Position(0, 5));
        board.setNull(new Position(0, 6));
        board.updateControl();
        board.updateKingPin();

        // verifico che la mossa si possa eseguire e la eseguo
        assertTrue(board.isMoveLegal(new Position(0,4), new Position(0,6)), "arrocco non eseguibile");
        board.physicalMovement(new Position(0, 4), new Position(0, 6));

        // Verifiche: il Re è in g1, la Torre è in f1, le vecchie case sono vuote
        assertTrue(board.getPiece(new Position(0, 6)) instanceof King);
        assertTrue(board.getPiece(new Position(0, 5)) instanceof Rock);
        assertNull(board.getPiece(new Position(0, 4)));
        assertNull(board.getPiece(new Position(0, 7)));


        // arrocco lungo
        // Reset della scacchiera per testare il lungo separatamente
        board = new ChessBoard(true);
        // libero b1 (0,1), c1 (0,2), d1 (0,3)
        board.setNull(new Position(0, 1));
        board.setNull(new Position(0, 2));
        board.setNull(new Position(0, 3));
        board.updateControl();
        board.updateKingPin();

        // verifico che la mossa possa essere eseguita e poi la eseguo
        assertTrue(board.isMoveLegal(new Position(0,4), new Position(0,2)), "arrocco non eseguibile");
        board.physicalMovement(new Position(0, 4), new Position(0, 2));

        // Verifiche: il Re è in c1, la Torre è in d1, le vecchie case sono vuote
        assertTrue(board.getPiece(new Position(0, 2)) instanceof King);
        assertTrue(board.getPiece(new Position(0, 3)) instanceof Rock);
        assertNull(board.getPiece(new Position(0, 4)));
        assertNull(board.getPiece(new Position(0, 0)));
    }

    @Test
    void blackCastles() {
        ChessBoard board = new ChessBoard(true);

        // arrocco corto
        // libero f8 (7,5) e g8 (7,6)
        board.setNull(new Position(7, 5));
        board.setNull(new Position(7, 6));
        board.updateControl();
        board.updateKingPin();

        // Il Re nero si sposta da e8(7,4) a g8(7,6)
        assertTrue(board.isMoveLegal(new Position(7,4), new Position(7, 6)));
        board.physicalMovement(new Position(7, 4), new Position(7, 6));

        assertTrue(board.getPiece(new Position(7, 6)) instanceof King);
        assertTrue(board.getPiece(new Position(7, 5)) instanceof Rock);
        assertNull(board.getPiece(new Position(7, 4)));
        assertNull(board.getPiece(new Position(7, 7)));


        // arrocco lungo

        // Reset della scacchiera per testare il lungo separatamente
        board = new ChessBoard(true);

        // Liberiamo b8 (7,1), c8 (7,2), d8 (7,3)
        board.setNull(new Position(7, 1));
        board.setNull(new Position(7, 2));
        board.setNull(new Position(7, 3));
        board.updateControl();
        board.updateKingPin();

        // Il Re nero si sposta da e8(7,4) a c8(7,2)
        assertTrue(board.isMoveLegal(new Position(7, 4), new Position(7, 2)));
        board.physicalMovement(new Position(7, 4), new Position(7, 2));

        assertTrue(board.getPiece(new Position(7, 2)) instanceof King);
        assertTrue(board.getPiece(new Position(7, 3)) instanceof Rock);
        assertNull(board.getPiece(new Position(7, 4)));
        assertNull(board.getPiece(new Position(7, 0)));
    }

    /*
    @Test
    void promotions() {
        ChessBoard board = setOnlyKing(); // Solo i Re presenti sulla scacchiera

        // promozione bianco
        // Piazziamo un pedone bianco in settima traversa (6, 4)
        Pawn whitePawn = new Pawn(new Position(6, 4), Colour.WHITE);
        board.setPiece(whitePawn);
        board.updateControl();
        board.updateKingPin();

        // Spingiamo il pedone in ottava traversa (7, 4) -> Scatta la promozione
        board.physicalMovement(new Position(6, 4), new Position(7, 4));

        // Verifiche: il pedone in (6,4) non c'è più, e in (7,4) c'è il pezzo promosso (es. Queen)
        assertNull(board.getPiece(new Position(6, 4)));
        assertNotNull(board.getPiece(new Position(7, 4)));
        assertFalse(board.getPiece(new Position(7, 4)) instanceof Pawn, "Il pezzo non deve più essere un pedone");


        // promozione nero
        // Piazziamo un pedone nero in seconda traversa (1, 3)
        Pawn blackPawn = new Pawn(new Position(1, 3), Colour.BLACK);
        board.setPiece(blackPawn);
        board.updateControl();
        board.updateKingPin();

        // Spingiamo il pedone in prima traversa (0, 3) -> Scatta la promozione
        board.physicalMovement(new Position(1, 3), new Position(0, 3));

        // Verifiche
        assertNull(board.getPiece(new Position(1, 3)));
        assertNotNull(board.getPiece(new Position(0, 3)));
        assertFalse(board.getPiece(new Position(0, 3)) instanceof Pawn, "Il pedone nero deve essere stato promosso");
    }
    */



@Test
    void testUndoMoveRestoresOriginalState() {
        ChessBoard board = new ChessBoard(true);
        assertTrue(board.isMoveLegal(new Position(1,3),new Position(3,3)));
        board.physicalMovement(new Position(1,3), new Position(3,3));
        assertTrue(board.isNull(new Position(1,3)));
        assertFalse(board.isNull(new Position(3,3)));

        assertTrue(board.isMoveLegal(new Position(6,5),new Position(5,5)));
        board.physicalMovement(new Position(6,5), new Position(5,5));
        assertTrue(board.isNull(new Position(6,5)));
        assertFalse(board.isNull(new Position(5,5)));


        Piece firsPiece=board.getPiece(new Position(3,3));
        Piece secondPiece=board.getPiece(new Position(5,5));

        UndoInfo undoOne=new UndoInfo(firsPiece,new Position(1,3),new Position(3,3),null,SpecialMoves.NONE);
        UndoInfo undoTwo=new UndoInfo(secondPiece,new Position(6,5),new Position(5,5),null,SpecialMoves.NONE);
        
        board.undoMove(undoOne);
        board.undoMove(undoTwo);

        assertTrue(board.isNull(new Position(3,3)));
        assertFalse(board.isNull(new Position(1,3)));

        assertTrue(board.isNull(new Position(5,5)));
        assertFalse(board.isNull(new Position(6,5)));

    }
    
    @Test
    void testUndoMoveWithCapture() {
        ChessBoard board = new ChessBoard();
        Bishop bishop = new Bishop(new Position(0,6),Colour.WHITE);
        Rock rock = new Rock(new Position(5,1),Colour.BLACK);
        board.setPiece(new King(new Position(7,4), Colour.BLACK));
        board.setPiece(new King(new Position(0,4), Colour.WHITE));
        board.setPiece(bishop);
        board.setPiece(rock);

        assertTrue(board.isMoveLegal(new Position(0,6),new Position(5,1)));
        board.physicalMovement(new Position(0,6),new Position(5,1));

        assertTrue(board.isNull(new Position(0,6)));
        assertTrue(board.isNull(new Position(5,1)) == false && board.getPiece(new Position(5,1)) instanceof Bishop);
        
        UndoInfo undo = new UndoInfo(bishop,new Position(0,6),new Position(5,1),rock,SpecialMoves.NONE);

        board.undoMove(undo);

        assertTrue(board.isNull(new Position(0,6))==false && board.getPiece(new Position(0,6)) instanceof Bishop);
        assertTrue(board.isNull(new Position(5,1))==false && board.getPiece(new Position(5,1)) instanceof Rock);
    }
    
    @Test
    void testUndoShortCastling() {
        ChessBoard board = new ChessBoard();
        board.setPiece(new King(new Position(7,4), Colour.BLACK));
        board.setPiece(new King(new Position(0,4), Colour.WHITE, false));
        board.setPiece(new Rock(new Position(0,7),Colour.WHITE,false));
        assertTrue(board.isMoveLegal(new Position(0,4),new Position(0,6)));
        UndoInfo undoShortCastle=board.physicalMovement(new Position(0,4),new Position(0,6));
        
        assertTrue(board.isNull(new Position(0,4)) && board.getPiece(new Position(0,6)) instanceof King);
        assertTrue(board.isNull(new Position(0,7)) && board.getPiece(new Position(0,5)) instanceof Rock);
        
        board.undoMove(undoShortCastle);
        assertTrue(board.isNull(new Position(0,4))==false && board.getPiece(new Position(0,4)) instanceof King);
        assertTrue(board.isNull(new Position(0,7))==false && board.getPiece(new Position(0,7)) instanceof Rock);
        
        assertTrue(board.isMoveLegal(new Position(0,4),new Position(0,6)));
    }
    
    @Test
    void testUndoLongCastling() {
        
    }
    
    @Test
    void testUndoMoveEnpassant() {
        ChessBoard board= new ChessBoard();
        Pawn blackPawn = new Pawn(new Position(6,4), Colour.BLACK);
        Pawn whitePawn = new Pawn(new Position(4,3), Colour.WHITE);
        board.setPiece(new King(new Position(7,4), Colour.BLACK));
        board.setPiece(new King(new Position(0,4), Colour.WHITE));
        board.setPiece(whitePawn);
        board.setPiece(blackPawn);

        assertTrue(board.isMoveLegal(blackPawn.getPosition(), new Position(4,4)));
        board.physicalMovement(blackPawn.getPosition(), new Position(4,4));
        assertTrue(board.isNull(new Position(4,4))==false && board.getPiece(new Position(4,4)) instanceof Pawn);
        
        assertTrue(board.isMoveLegal(whitePawn.getPosition(),new Position(5,4)));
        UndoInfo undoEnpassant= board.physicalMovement(whitePawn.getPosition(), new Position(5,4));
        assertTrue(board.isNull(new Position(5,4))==false && board.getPiece(new Position(5,4)) instanceof Pawn);    
        assertTrue(board.isNull(new Position(4,4)));
        
        board.undoMove(undoEnpassant);
        assertTrue(board.isNull(new Position(4,4))==false && board.getPiece(new Position(4,4)) instanceof Pawn);
        assertTrue(board.isNull(new Position(4,3))==false && board.getPiece(new Position(4,3)) instanceof Pawn);
    }
    
    @Test
    @StdIo("Q")
    void testUndoMoveQueenPromotion() {
        ChessBoard board = new ChessBoard();

        Pawn whitePawn = new Pawn(new Position(6, 3), Colour.WHITE);
        board.setPiece(new King(new Position(7, 4), Colour.BLACK));
        board.setPiece(new King(new Position(0, 4), Colour.WHITE));
        board.setPiece(whitePawn);
        
        assertTrue(board.isMoveLegal(whitePawn.getPosition(), new Position(7, 3)));
        UndoInfo undoPromotion = board.physicalMovement(whitePawn.getPosition(), new Position(7, 3));
        assertTrue(board.getPiece(new Position(7, 3)) instanceof Queen);
        
        board.undoMove(undoPromotion);
        assertTrue(board.isNull(new Position(7, 3)));
        assertFalse(board.isNull(new Position(6, 3)));
        assertTrue(board.getPiece(new Position(6, 3)) instanceof Pawn);
        
    }

    @Test
    @StdIo("R")
    void testUndoMoveRockPromotion() {

        ChessBoard board = new ChessBoard();
        
        Pawn whitePawn = new Pawn(new Position(6, 3), Colour.WHITE);
        board.setPiece(new King(new Position(7, 4), Colour.BLACK));
        board.setPiece(new King(new Position(0, 4), Colour.WHITE));
        board.setPiece(whitePawn);
        
        assertTrue(board.isMoveLegal(whitePawn.getPosition(), new Position(7, 3)));
        UndoInfo undoPromotion = board.physicalMovement(whitePawn.getPosition(), new Position(7, 3));
        assertTrue(board.getPiece(new Position(7, 3)) instanceof Rock);
        
        board.undoMove(undoPromotion);
        assertTrue(board.isNull(new Position(7, 3)));
        assertFalse(board.isNull(new Position(6, 3)));
        assertTrue(board.getPiece(new Position(6, 3)) instanceof Pawn);
        
    }

    @Test
    @StdIo("K")
    void testUndoMoveKnightPromotion() {
        ChessBoard board = new ChessBoard();
        
        Pawn whitePawn = new Pawn(new Position(6, 3), Colour.WHITE);
        board.setPiece(new King(new Position(7, 4), Colour.BLACK));
        board.setPiece(new King(new Position(0, 4), Colour.WHITE));
        board.setPiece(whitePawn);
        
        assertTrue(board.isMoveLegal(whitePawn.getPosition(), new Position(7, 3)));
        UndoInfo undoPromotion = board.physicalMovement(whitePawn.getPosition(), new Position(7, 3));
        assertTrue(board.getPiece(new Position(7, 3)) instanceof Knight);
        board.undoMove(undoPromotion);
        assertTrue(board.isNull(new Position(7, 3)));
        assertFalse(board.isNull(new Position(6, 3)));
        assertTrue(board.getPiece(new Position(6, 3)) instanceof Pawn);
    }

    @Test
    @StdIo("B")
    void testUndoMoveBishopPromotion() {
        ChessBoard board = new ChessBoard();
    
        Pawn whitePawn = new Pawn(new Position(6, 3), Colour.WHITE);
        board.setPiece(new King(new Position(7, 4), Colour.BLACK));
        board.setPiece(new King(new Position(0, 4), Colour.WHITE));
        board.setPiece(whitePawn);
        
        assertTrue(board.isMoveLegal(whitePawn.getPosition(), new Position(7, 3)));
        UndoInfo undoPromotion = board.physicalMovement(whitePawn.getPosition(), new Position(7, 3));
        assertTrue(board.getPiece(new Position(7, 3)) instanceof Bishop);
        
        board.undoMove(undoPromotion);
        assertTrue(board.isNull(new Position(7, 3)));
        assertFalse(board.isNull(new Position(6, 3)));
        assertTrue(board.getPiece(new Position(6, 3)) instanceof Pawn);
    }
}