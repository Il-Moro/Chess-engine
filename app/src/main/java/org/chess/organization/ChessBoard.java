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
        
        // Se non c'è un pezzo nella posizione di partenza
        if (this.getPiece(from) == null) { return false; }

        Piece piece = this.getPiece(from);
        Position piecePosition = from;
        Colour pieceColour = piece.getColour();

        Piece targetPiece = this.getPiece(to);
        

        // GESTIONE CASI GENERICI
        boolean legal = generalCasesForLegalMoves(piece, targetPiece, pieceColour, from, to);
        if(!legal) { return false; }

        // GESTIONE CASI PARTICOLARI
        King myKing = (pieceColour == (Colour.WHITE)) ? this.whiteKing : this.blackKing;
        Position myKingPosition = myKing.getPosition();
        Pin[][] kingPin = (myKing.getColour() == (Colour.WHITE)) ? whiteKingPin : blackKingPin;

        // double check on king
        legal = doubleCheckCaseForLegalMoves(piece, kingPin, myKingPosition);
        if(!legal){ return false; }

        // re sotto scacco lineare
        if (kingPin[myKingPosition.row()][myKingPosition.column()] == Pin.UNDER_CHECK_LINE
                && !(piece instanceof King)) {
            // considero prima se: il pezzo è in Pin NON può muoversi
            if (kingPin[piecePosition.row()][piecePosition.column()] == Pin.PINNED) {
                return false;
            } // se il pezzo non è in PIN può solo coprire le case denotate come CHECK_PATH o
              // come King_attacker
            else if (kingPin[to.row()][to.column()] != Pin.CHECK_PATH
                    && kingPin[to.row()][to.column()] != Pin.KING_ATTACKER) {
                return false;
            }
        } else { // il re non è sotto scacco: controllo se il pezzo è in pin e la casa di arrivo
                 // se si trova lungo la direzione di Pin
            if (kingPin[piece.getPosition().row()][piece.getPosition().column()] == Pin.PINNED) {
                // calcolo del determinante per verificare se hanno lo stesso verso
                int deltaRowKing = piecePosition.row() - myKingPosition.row();
                int deltaColKing = piecePosition.column() - myKingPosition.column();
                int deltaRowTo = to.row() - myKingPosition.row();
                int deltaColTo = to.column() - myKingPosition.column();

                if ((deltaRowKing * deltaColTo) - (deltaColKing * deltaRowTo) != 0) {
                    return false;
                }
            }
        }

        // caso cavalli
        // sotto scacco da cavallo
        if (kingPin[myKingPosition.row()][myKingPosition.column()] == Pin.UNDER_CHECK_KNIGHT
                && !(piece instanceof King)) {
            // Se il pezzo è in PIN, non può muoversi comunque per difendere dal cavallo
            if (kingPin[piecePosition.row()][piecePosition.column()] == Pin.PINNED) {
                return false;
            }
            // Può muoversi solo se la casa d'arrivo coincide con l'attaccante (lo mangia)
            if (kingPin[to.row()][to.column()] != Pin.KING_ATTACKER) {
                return false;
            }
        }

        if (piece instanceof King k) {
            // 1. non può muoversi su case controllate da avversario
            if ((piece.getColour() == (Colour.WHITE) && squaresControlledByBlack[to.row()][to.column()] != 0) ||
                    (piece.getColour() == (Colour.BLACK) && squaresControlledByWhite[to.row()][to.column()] != 0)) {
                return false;
            }
            // 3. controllo sull'arrocco
            int direction = to.column() - from.column(); // Destra (+) o Sinistra (-)

            if (Math.abs(direction) == 2) {
                int row = piecePosition.row();
                Piece rook;

                // Trova la torre corretta in base alla direzione
                if (direction > 0) { // Verso Destra -> Arrocco Corto
                    rook = this.chessboard[row][7];
                } else { // Verso Sinistra -> Arrocco Lungo
                    rook = this.chessboard[row][0];
                }

                // Validazione base dei pezzi coinvolti e dello scacco attuale
                if (rook == null || !(rook instanceof Rook) || ((Rook) rook).getHasMoved() ||
                        k.getHasMoved() || kingPin[row][piecePosition.column()] != null) {
                    return false;
                }

                // Recuperiamo la matrice di controllo dell'avversario
                int[][] adversaryControl = k.getColour() == (Colour.WHITE) ? squaresControlledByBlack
                        : squaresControlledByWhite;

                // ARROCCO CORTO (Verso destra)
                if (direction > 0) {
                    // Le case di passaggio (colonne 5 e 6) devono essere VUOTE e NON CONTROLLATE
                    if (this.chessboard[row][5] != null || this.chessboard[row][6] != null ||
                            adversaryControl[row][5] != 0 || adversaryControl[row][6] != 0) {
                        return false;
                    }
                }
                // ARROCCO LUNGO (Verso sinistra)
                else {
                    // Le case di passaggio (colonne 1, 2, 3) devono essere VUOTE
                    if (this.chessboard[row][1] != null || this.chessboard[row][2] != null
                            || this.chessboard[row][3] != null ||
                            adversaryControl[row][2] != 0 || adversaryControl[row][3] != 0) {
                        return false;
                    }
                }
            }
        }

        boolean rowDiff = false;
        if (lastPawnMoved != null) {
            rowDiff = Math.abs(lastPawnFromPosition.row() - lastPawnMoved.getPosition().row()) != 2;
        }

        // pedone: sasa
        if (piece instanceof Pawn) {
            // 1. non può spostarsi in avanti se è presente un'altro pezzo
            if (from.row() != to.row() && from.column() == to.column() && !this.isNull(to))
                legal = false;
            // 2. enpassant
            if (from.row() != to.row() && from.column() != to.column() && this.isNull(to)) {
                if (lastPawnMoved != null) {
                    if (to.column() == lastPawnMoved.getPosition().column()
                            && from.row() == lastPawnMoved.getPosition().row()) {
                        if (rowDiff)
                            legal = false;
                    } else
                        legal = false;
                } else
                    legal = false;
            }
        }
        return legal;
    }


    private boolean generalCasesForLegalMoves(Piece piece, Piece targetPiece, Colour colourPiece, Position from, Position to){
        // eseguo controllo sul primo filtro di getPotentialMoves
        Set<Position> potentialMoves = piece.getPotentialMoves(this);
        if (!potentialMoves.contains(to)) {
            return false;
        }

        if (
            (targetPiece != null) && 
            (colourPiece == (targetPiece.getColour()) || 
            (targetPiece instanceof King))) 
        {
            return false;
        }
        return true;
    }
    
    private static boolean doubleCheckCaseForLegalMoves(Piece piece, Pin[][] kingPin, Position myKingPosition){
        if (
            (kingPin[myKingPosition.row()][myKingPosition.column()] == Pin.DOUBLE_CHECK) && 
            (!(piece instanceof King))) 
        {
            return false;
        }
        return true;
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
        return physicalMovement(from, to, "Q"); 
    }

    public UndoInfo physicalMovement(Position from, Position to, String stringPiece) {
        Piece piece = this.getPiece(from);
        Colour pieceColour = piece.getColour();
        boolean rowDiff = false;

        if (lastPawnMoved != null) {
            rowDiff = Math.abs(lastPawnFromPosition.row() - lastPawnMoved.getPosition().row()) == 2;
        }

        int direction = to.column() - from.column();

        if (piece instanceof King k && Math.abs(direction) == 2) {
            // arrocco corto
            if (direction > 0) {
                k.setPosition(to);
                k.setHasMovedTrue();
                this.setPiece(k);
                this.setNull(from);

                // spostamento della torre affianco al re
                Rook rook = (Rook) this.getPiece(new Position(to.row(), 7));
                rook.setPosition(new Position(to.row(), 5));
                rook.setHasMovedTrue();
                this.setPiece(rook);
                this.setNull(new Position(to.row(), 7));
                updateAfterMove(piece, from);
                return new UndoInfo(piece, from, to, null, SpecialMoves.SHORT_CASTLING);
                // arrocco lungo
            } else if (direction < 0) {
                k.setPosition(to);
                k.setHasMovedTrue();
                this.setPiece(k);
                this.setNull(from);

                // spostamento della torre affianco al re
                Rook rook = (Rook) this.getPiece(new Position(to.row(), 0));
                rook.setPosition(new Position(to.row(), 3));
                rook.setHasMovedTrue();
                this.setPiece(rook);
                this.setNull(new Position(to.row(), 0));
                updateAfterMove(piece, from);
                return new UndoInfo(piece, from, to, null, SpecialMoves.LONG_CASTLING);
            }
        } else if (piece instanceof Pawn && ((piece.getColour() == Colour.WHITE && to.row() == 7) || piece.getColour() == Colour.BLACK && to.row() == 0)) {
            
            Piece promotionPiece = switch (stringPiece) {
            case "R" -> new Rook(to, pieceColour, true);
            case "B" -> new Bishop(to, pieceColour);
            case "K" -> new Knight(to, pieceColour);
            default -> new Queen(to, pieceColour);
        };

            Piece eatenPiece = this.getPiece(to);
            this.setPiece(promotionPiece);
            this.setNull(from);
            updateAfterMove(piece, from);
            return new UndoInfo(piece, from, to, eatenPiece, SpecialMoves.PROMOTION);
        } else if (piece instanceof Pawn
                && (from.row() != to.row() && from.column() != to.column() && this.isNull(to))) {
            if (lastPawnMoved != null) {
                if (to.column() == lastPawnMoved.getPosition().column()
                        && from.row() == lastPawnMoved.getPosition().row()) {
                    if (rowDiff) {
                        piece.setPosition(to);
                        this.setPiece(piece);
                        this.setNull(from);
                        this.setNull(lastPawnMoved.getPosition());
                        updateAfterMove(piece, from);
                        return new UndoInfo(piece, from, to, lastPawnMoved, SpecialMoves.ENPASSANT);
                    }

                }
            }
        }
        if (piece instanceof Pawn) {
            lastPawnMoved = (Pawn) piece;
            lastPawnFromPosition = from;
        } else {
            lastPawnMoved = null;
            lastPawnFromPosition = null;
        }

        // caso generale
        Piece eatenPiece = this.getPiece(to);
        piece.setPosition(to);
        this.setNull(from);
        this.setPiece(piece);
        updateAfterMove(piece, from);
        return new UndoInfo(piece, from, to, eatenPiece, SpecialMoves.NONE);
    }

    private void updateAfterMove(Piece piece, Position from) {

        this.updateControlMap();

        if (this.whiteKing != null && this.blackKing != null) {
            updateKingPin();
        }

        if (piece instanceof King k) {
            k.setHasMovedTrue();
        } else if (piece instanceof Rook r) {
            r.setHasMovedTrue();
        }

    }

    public void undoMove(UndoInfo undo) {
        switch (undo.special()) {
            case SpecialMoves.SHORT_CASTLING:
                King k = (King) undo.movedPiece();
                k.setPosition(undo.from());
                k.setHasMovedFalse();
                this.setPiece(k);
                this.setNull(undo.to());

                // spostamento della torre affianco al re
                Rook rook = (Rook) this.getPiece(new Position(undo.from().row(), 5));
                rook.setPosition(new Position(undo.from().row(), 7));
                rook.setHasMovedFalse();
                this.setPiece(rook);
                this.setNull(new Position(undo.from().row(), 5));

                break;

            case SpecialMoves.LONG_CASTLING:
                k = (King) undo.movedPiece();
                k.setPosition(undo.from());
                k.setHasMovedFalse();
                this.setPiece(k);
                this.setNull(undo.to());

                rook = (Rook) this.getPiece(new Position(undo.from().row(), 3));
                rook.setPosition(new Position(undo.from().row(), 0));
                rook.setHasMovedFalse();
                this.setPiece(rook);
                this.setNull(new Position(undo.from().row(), 3));

                break;

            case SpecialMoves.ENPASSANT:
                Pawn pawn = (Pawn) undo.movedPiece();
                pawn.setPosition(undo.from());
                this.setPiece(pawn);
                this.setNull(undo.to());

                // Ripristino pezzo mangiato
                Piece enemyPawn = undo.eatenPiece();
                Position enemyOriginalPosition = new Position(undo.from().row(), undo.to().column());
                enemyPawn.setPosition(enemyOriginalPosition);
                this.setPiece(enemyPawn);
                break;
            case SpecialMoves.PROMOTION:
                // Ripristino pedone originale
                Piece originalPawn = undo.movedPiece();
                originalPawn.setPosition(undo.from());
                this.setPiece(originalPawn);

                if (undo.eatenPiece() != null) {
                    this.setPiece(undo.eatenPiece());
                } else {
                    this.setNull(undo.to());
                }
                break;

            default:
                Piece piece = undo.movedPiece();
                piece.setPosition(undo.from());
                this.setPiece(piece);

                if (undo.eatenPiece() != null)
                    this.setPiece(undo.eatenPiece());
                else {
                    this.setNull(undo.to());
                }
                break;
        }
        // Ricalcolo Pin e del control Maps
        this.updateControlMap();
        if (this.whiteKing != null && this.blackKing != null) {
            updateKingPin();
        }
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

    // scansiona se esistono mosse legali
    private boolean hasAnyLegalMoves(Colour colour) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                Piece p = chessboard[row][column];

                if (p != null && p.getColour() == (colour)) {
                    for (Position to : p.getPotentialMoves(this)) {
                        if (this.isMoveLegal(p.getPosition(), to)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Rinfresca e rigenera le matrici KingPin strutturali per entrambi i Re
     * presenti.
     */
    public void updateKingPin() {
        generateKingPinData(blackKingPin, blackKing);
        generateKingPinData(whiteKingPin, whiteKing);
    }

    private void generateKingPinData(Pin[][] kingPin, King king) {
        if (king == null) {
            return;
        }
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                kingPin[row][column] = null;
            }
        }

        int[][] linearDirections = { { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 }, { 1, 0 }, { -1, 0 }, { 0, 1 },
                { 0, -1 } };
        int[][] knightDirections = { { 2, -1 }, { 2, 1 }, { 1, 2 }, { -1, 2 }, { -2, 1 }, { -2, -1 }, { -1, -2 },
                { 1, -2 } };

        int kingRow = king.getPosition().row();
        int kingColumn = king.getPosition().column();

        int checks = 0;

        checks += calculateLinearDirectionsForKingPin(king.getColour(), linearDirections, kingRow, kingColumn, checks, kingPin);
        checks += calculatePawnDirectionsForKingPin(king.getColour(), kingRow, kingColumn, checks, kingPin);
        calculateKnightDirectionsForKingPin(king.getColour(), knightDirections, kingRow, kingColumn, checks, kingPin);        
    }

    private int calculateLinearDirectionsForKingPin(Colour colour, int[][] directions, int kingRow, int kingColumn, int checks,
            Pin[][] kingPin) {

        for (int[] d : directions) {
            int targetRow = kingRow + d[0];
            int targetColumn = kingColumn + d[1];
            Piece ownPiece = null;
            while (Position.isInsideBounds(targetRow, targetColumn)) {
                Piece targetPiece = chessboard[targetRow][targetColumn];
                if (targetPiece != null) {
                    if (targetPiece.getColour() == (colour)) {
                        if (ownPiece == null) {
                            ownPiece = targetPiece;
                        } else {
                            break;
                        }
                    } else {
                        boolean isDiagonal = (d[0] * d[1] != 0);
                        if (ownPiece != null) { //
                            if ((isDiagonal && (targetPiece instanceof Bishop || targetPiece instanceof Queen)) ||
                                    (!isDiagonal && (targetPiece instanceof Rook || targetPiece instanceof Queen))) {
                                kingPin[ownPiece.getPosition().row()][ownPiece.getPosition().column()] = Pin.PINNED;
                            }
                        } else if ((isDiagonal && (targetPiece instanceof Bishop || targetPiece instanceof Queen)) ||
                                (!isDiagonal && (targetPiece instanceof Rook || targetPiece instanceof Queen))) {
                            checks += 1;
                            if (checks == 1) {
                                kingPin[kingRow][kingColumn] = Pin.UNDER_CHECK_LINE;
                            } else if (checks == 2) {
                                kingPin[kingRow][kingColumn] = Pin.DOUBLE_CHECK;
                            }

                            kingPin[targetRow][targetColumn] = Pin.KING_ATTACKER;
                            markCheckPathForKingPin(kingRow, kingColumn, targetRow, targetColumn, d, kingPin);
                        }
                        break;
                    }
                }
                targetRow += d[0];
                targetColumn += d[1];
            }
        }
        return checks;
    }

    private int calculatePawnDirectionsForKingPin(Colour colour, int kingRow, int kingColumn, int checks, Pin[][] kingPin){
        int[][] directions = (colour == Colour.WHITE) ? new int[][] {{1,-1}, {1,1}} : new int[][] {{-1,-1}, {-1,1}};
        
        for (int[] d : directions){
            int targetRow = kingRow + d[0];
            int targetColumn = kingColumn + d[1];
            if(Position.isInsideBounds(targetRow, targetColumn)){
                Piece targetPiece = chessboard[targetRow][targetColumn];
                if(targetPiece != null && targetPiece instanceof Pawn && targetPiece.getColour() != colour){
                    checks += 1;
                    if (checks == 1) {
                        kingPin[kingRow][kingColumn] = Pin.UNDER_CHECK_LINE;
                    } else {
                        kingPin[kingRow][kingColumn] = Pin.DOUBLE_CHECK;
                    }
                    kingPin[targetRow][targetColumn] = Pin.KING_ATTACKER;
                }
            }
        }
        return checks;
    }

    private void calculateKnightDirectionsForKingPin(Colour colour, int[][] directions, int kingRow, int kingColumn, int checks, Pin[][] kingPin) {
        for (int[] d : directions) {           
            int targetRow = kingRow + d[0];
            int targetColumn = kingColumn + d[1];

            if (Position.isInsideBounds(targetRow, targetColumn)) {
                Piece targetPiece = chessboard[targetRow][targetColumn];
                if (targetPiece != null) {
                    if (!(targetPiece.getColour() == (colour)) && targetPiece instanceof Knight) {
                        checks += 1;
                        if (checks == 1) {
                            kingPin[kingRow][kingColumn] = Pin.UNDER_CHECK_KNIGHT;
                        } else {
                            kingPin[kingRow][kingColumn] = Pin.DOUBLE_CHECK;
                        }
                        kingPin[targetRow][targetColumn] = Pin.KING_ATTACKER;
                    }
                }
            }
        }
    }

    private static void markCheckPathForKingPin(int startRow, int startCol, int endRow, int endCol, int[] d, Pin[][] kingPin) {
        int row = startRow + d[0];
        int column = startCol + d[1];
        while (row != endRow || column != endCol) {
            kingPin[row][column] = Pin.CHECK_PATH;
            row += d[0];
            column += d[1];
        }
    }

    /**
     * Azzera ed esegue l'aggiornamento completo delle mappe di controllo geometrico
     * per entrambi i colori.
     */
    public void updateControlMap() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.squaresControlledByBlack[i][j] = 0;
                this.squaresControlledByWhite[i][j] = 0;
            }
        }

        for (Piece[] positions : this.chessboard) {
            for (Piece p : positions) {
                if (p != null) {
                    this.fillControlMap(p);
                }
            }
        }
    }

    private void fillControlMap(Piece piece) {
        Set<Position> set = piece.getPotentialMoves(this);

        if (piece.getColour() == (Colour.WHITE)) {
            for (Position s : set) {
                this.squaresControlledByWhite[s.row()][s.column()] += 1;
            }
        } else {
            for (Position s : set) {
                this.squaresControlledByBlack[s.row()][s.column()] += 1;
            }
        }
    }
}
