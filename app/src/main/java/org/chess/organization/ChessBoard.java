package org.chess.organization;

import java.util.*;

import org.chess.dataTypes.*;
import org.chess.pieces.*;

/**
 * Gestione della chessboard di una partita di scacchi.
 * <p>
 * Questa classe offre funzionalità per:
 * <ul>
 * <li>Il controllo sui singoli pezzi e la loro posizione.</li>
 * <li>Una panoramica delle matrici di controllo delle case per entrambi i
 * giocatori.</li>
 * <li>La gestione delle matrici di pin (pezzi inchiodati, attaccanti, stato di
 * scacco).</li>
 * <li>La verifica della legalità di una mossa, dello scacco matto e dello
 * stallo.</li>
 * </ul>
 * </p>
 */
public class ChessBoard {

    public static final int BOARD_SIZE = 8;

    private Piece[][] chessboard;
    // matrici di controllo delle celle
    private int[][] squaresControlledByWhite;
    private int[][] squaresControlledByBlack;
    // matrici di pin e stato del re
    private Pin[][] whiteKingPin; // matrice che controlla a partire dal re: Pieces in pin, Pieces attacker, marca il re se è sotto scacco da un pezzo lineare oppure da un cavallo o se è in doppio scacco
    private Pin[][] blackKingPin;
    // re per semplificare i calcoli
    private King whiteKing;
    private King blackKing;
    // varibili per l'enpassants
    private Pawn lastPawnMoved = null;
    private Position lastPawnFromPosition = null;

    /**
     * costruttore base: inizializza una chessboard vuota, nessun re, da riempire
     * mediante le funzioni offerte
     */
    public ChessBoard() {
        this.chessboard = new Piece[BOARD_SIZE][BOARD_SIZE];
        this.squaresControlledByBlack = new int[BOARD_SIZE][BOARD_SIZE];
        this.squaresControlledByWhite = new int[BOARD_SIZE][BOARD_SIZE];
        this.whiteKingPin = new Pin[BOARD_SIZE][BOARD_SIZE];
        this.blackKingPin = new Pin[BOARD_SIZE][BOARD_SIZE];
        // per evitare errori se in Chessboard non sono stati inseriti re
        this.whiteKing = null;
        this.blackKing = null;
    }

    /**
     * costruttore completo per la posizione iniziale di una partita di scacchi
     * 
     * @param initialPosition variabile booleana, per convenzione true, ma in realtà
     *                        basta che sia presente un valore booleano
     */
    public ChessBoard(boolean initialPosition) {
        this.chessboard = new Piece[BOARD_SIZE][BOARD_SIZE];
        this.squaresControlledByBlack = new int[BOARD_SIZE][BOARD_SIZE];
        this.squaresControlledByWhite = new int[BOARD_SIZE][BOARD_SIZE];
        this.whiteKingPin = new Pin[BOARD_SIZE][BOARD_SIZE];
        this.blackKingPin = new Pin[BOARD_SIZE][BOARD_SIZE];

        // Bianchi
        for (int i = 0; i < BOARD_SIZE; i++) {
            this.setPiece(new Pawn(new Position(1, i), Colour.WHITE));
        }
        this.setPiece(new Rook(new Position(0, 0), Colour.WHITE, false));
        this.setPiece(new Knight(new Position(0, 1), Colour.WHITE));
        this.setPiece(new Bishop(new Position(0, 2), Colour.WHITE));
        this.setPiece(new Queen(new Position(0, 3), Colour.WHITE));
        this.setPiece(new King(new Position(0, 4), Colour.WHITE, false));
        this.setPiece(new Bishop(new Position(0, 5), Colour.WHITE));
        this.setPiece(new Knight(new Position(0, 6), Colour.WHITE));
        this.setPiece(new Rook(new Position(0, 7), Colour.WHITE, false));

        // Neri
        for (int i = 0; i < BOARD_SIZE; i++) {
            this.setPiece(new Pawn(new Position(6, i), Colour.BLACK));
        }
        this.setPiece(new Rook(new Position(7, 0), Colour.BLACK, false));
        this.setPiece(new Knight(new Position(7, 1), Colour.BLACK));
        this.setPiece(new Bishop(new Position(7, 2), Colour.BLACK));
        this.setPiece(new Queen(new Position(7, 3), Colour.BLACK));
        this.setPiece(new King(new Position(7, 4), Colour.BLACK, false));
        this.setPiece(new Bishop(new Position(7, 5), Colour.BLACK));
        this.setPiece(new Knight(new Position(7, 6), Colour.BLACK));
        this.setPiece(new Rook(new Position(7, 7), Colour.BLACK, false));

        // Inizializzazione automatica dello stato
        updateControlMap();
        updateKingPin();
    }

    // setter
    /**
     * Inserisce un nuovo Piece nella matrice dei pezzi della chessboard
     * 
     * @param piece istanza dell'oggetto {@link Piece}
     */
    public void setPiece(Piece piece) {
        this.chessboard[piece.getPosition().row()][piece.getPosition().column()] = piece;
        if (piece instanceof King k) {
            if (piece.getColour() == (Colour.WHITE))
                whiteKing = k;
            else {
                blackKing = k;
            }
        }
    }

    /**
     * Serve a svuotare la casa della chessboard dal pezzo su di essa
     * 
     * @param position di tipo {@link Position}
     */
    public void setNull(Position position) {
        this.chessboard[position.row()][position.column()] = null;
    }

    // getter
    /**
     * ritorna il pezzo che c'è nella posizione specificata della chessboard
     * 
     * @param position di tipo {@link Position}
     * @return l'istanza dell'oggetto {@link Piece} se presente, altrimenti
     *         {@code null}
     */
    public Piece getPiece(Position position) {
        return chessboard[position.row()][position.column()];
    }

    /**
     * ritorna matrice dei pezzi
     * 
     * @return matrice 8x8 con tutti i pezzi
     */
    public Piece[][] getBoard() {
        return this.chessboard;
    }

    /**
     * ritorna la matrice di controllo di un certo colore: ogni cella della matrice
     * è contrassegnata da un numero naturale > 0, indica il numero di pezzo che
     * controllano la cella nello specifico
     * 
     * @param colour il colore della fazione di cui si richiede la mappa di
     *               controllo
     * @return matrice di interi 8x8
     */
    public int[][] getSquaresControlledBy(Colour colour) {
        if (colour == Colour.WHITE) {
            return this.squaresControlledByWhite;
        } else {
            return this.squaresControlledByBlack;
        }
    }

    public King getKing(Colour colour){
        if (colour == Colour.WHITE) {
            return whiteKing;
        } else {
            return blackKing;
        }
    }

    public Pawn getLastPawnMoved(){
        return lastPawnMoved;
    }

    public Position getLastPawnFromPosition(){
        return lastPawnFromPosition;
    }

    public void setLastPawnMoved(Pawn pawn) { this.lastPawnMoved = pawn; }
    public void setLastPawnFromPosition(Position pos) { this.lastPawnFromPosition = pos; }

    /**
     * Verifica se una determinata casa della chessboard è priva di pezzi
     * 
     * @param position la coordinata da controllare
     * @return true se la casa è vuota, altrimenti false
     */
    public boolean isNull(Position position) {
        return chessboard[position.row()][position.column()] == null;
    }

    /**
     * Ritorna la matrice di Pin e delle sottomatrici di scacco del Re per il colore
     * richiesto
     * 
     * @param colour il colore del Re di riferimento
     * @return matrice 8x8 di oggetti {@link Pin}
     */
    public Pin[][] getKingPin(Colour colour) {
        return (colour == Colour.WHITE) ? whiteKingPin : blackKingPin;
    }


    // movement of pieces
    public boolean isMoveLegal(Position from, Position to) {
        return MoveValidator.isMoveLegal(this, from, to);
    }


    /**
     * Esegue lo spostamento fisico dei pezzi sulla scacchiera. Gestisce nativamente
     * l'Arrocco
     * e intercetta le promozioni dei pedoni interrogando l'utente.
     * 
     * @param from posizione geometrica di partenza
     * @param to   posizione geometrica di arrivo
     */
    
    public UndoInfo physicalMovement(Position from, Position to) {
        return MoveApplier.physicalMovement(this, from, to, "Q"); 
    }

    public UndoInfo physicalMovement(Position from, Position to, String stringPiece) {
        return MoveApplier.physicalMovement(this, from, to, stringPiece);
    }

    public void undoMove(UndoInfo undo) {
        MoveApplier.undoMove(this, undo);
    }

    /**
     * Valuta se la posizione corrente per il colore indicato determina la
     * conclusione della partita.
     * 
     * @param colour colore del giocatore attivo sotto analisi
     * @return un elemento dell'Enum {@link End} che denota lo stato conclusivo o
     *         attivo
     */
    public End isCheckmateOrStalemate(Colour colour) {
        if (hasInsufficientMaterial()) {
            return End.STALEMATE;
        }

        Position kingPosition = colour == (Colour.WHITE) ? whiteKing.getPosition() : blackKing.getPosition();
        Pin[][] kingPin = (colour == (Colour.WHITE)) ? whiteKingPin : blackKingPin;

        boolean anyLegalMoves = hasAnyLegalMoves(colour);

        // 1. Per essere matto, il Re DEVE essere sotto scacco
        if (kingPin[kingPosition.row()][kingPosition.column()] == null && !anyLegalMoves) {
            return End.STALEMATE;
        } else if (kingPin[kingPosition.row()][kingPosition.column()] != null && !anyLegalMoves) {
            return End.CHECKMATE;
        } else {
            return End.IN_PROGRESS;
        }
    }

    private boolean hasInsufficientMaterial() {
        int kingCount = 0;
        int bishopCount = 0;
        int knightCount = 0;
        int otherCount = 0;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece p = chessboard[row][col];
                if (p != null) {
                    if (p instanceof King) {
                        kingCount++;
                    } else if (p instanceof Bishop) {
                        bishopCount++;
                    } else if (p instanceof Knight) {
                        knightCount++;
                    } else {
                        otherCount++;
                    }
                }
            }
        }

        // If there are pawns, rooks, queens, etc., it's not insufficient material
        if (otherCount > 0) {
            return false;
        }

        // Both players must have a king
        if (kingCount != 2) {
            return false;
        }

        int totalPieces = kingCount + bishopCount + knightCount;

        // King vs King
        if (totalPieces == 2) {
            return true;
        }

        // King + Bishop vs King OR King + Knight vs King
        if (totalPieces == 3 && (bishopCount == 1 || knightCount == 1)) {
            return true;
        }

        return false;
    }

    // scansiona se esistono mosse legali
    private boolean hasAnyLegalMoves(Colour colour) {        
        return Arrays.stream(chessboard)
            .flatMap(Arrays::stream) 
            .filter(piece -> piece != null && piece.getColour().equals(colour))
            .anyMatch(piece -> piece.getPotentialMoves(this).stream()
            .anyMatch(to -> this.isMoveLegal(piece.getPosition(), to)));
    }

    /**
     * Rinfresca e rigenera le matrici KingPin strutturali per entrambi i Re
     * presenti.
     */
    public void updateKingPin() {
        UpdaterMaps.updateKingPin(this);
    }

    /**
     * Azzera ed esegue l'aggiornamento completo delle mappe di controllo geometrico
     * per entrambi i colori.
     */
    public void updateControlMap() {
        UpdaterMaps.updateControlMap(this);
    }

}
