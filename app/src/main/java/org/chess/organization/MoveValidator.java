package org.chess.organization;

import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.pieces.*;

public class MoveValidator {
    // movement of pieces
    public static boolean isMoveLegal(ChessBoard board, Position from, Position to) {
        
        // Se non c'è un pezzo nella posizione di partenza
        if (board.getPiece(from) == null) { return false; }

        Piece piece = board.getPiece(from);
        Position piecePosition = from;
        Colour pieceColour = piece.getColour();
        Piece targetPiece = board.getPiece(to);
        

        // GESTIONE CASI GENERICI
        boolean legal = generalCasesForLegalMoves(board, piece, targetPiece, pieceColour, from, to);
        if(!legal) { return false; }

        // GESTIONE CASI PARTICOLARI

        King myKing = (pieceColour == (Colour.WHITE)) ? board.getKing(Colour.WHITE) : board.getKing(Colour.BLACK);
        Position myKingPosition = myKing.getPosition();
        Pin[][] kingPin = (myKing.getColour() == (Colour.WHITE)) ? board.getKingPin(Colour.WHITE) : board.getKingPin(Colour.BLACK);

        // double check on king
        legal = doubleCheckCaseForLegalMoves(piece, kingPin, myKingPosition);
        if(!legal){ return false; }

        // piece non è il re, ma il re è sotto scacco
        legal = pinCasesForLegalMoves(piece, piecePosition, to, kingPin, myKingPosition);
        if(!legal){ return false; }

        // piece è re
        legal = pieceIsKingCaseForLegalMoves(board, piece, piecePosition, to, from, kingPin);
        if(!legal) { return false; }

        // gestione pedone (enpassant)
        legal = pieceIsPawnEnpassantCaseForLegalMoves(board, legal, piece, from, to);
        if(!legal) {return false;}
        return true;
    }


    private static boolean generalCasesForLegalMoves(ChessBoard board, Piece piece, Piece targetPiece, Colour colourPiece, Position from, Position to){
        // eseguo controllo sul primo filtro di getPotentialMoves
        Set<Position> potentialMoves = piece.getPotentialMoves(board);
        if (!potentialMoves.contains(to)) {
            return false;
        }

        if (
            (targetPiece != null) && 
            (colourPiece == (targetPiece.getColour()) || 
            (targetPiece instanceof King))
            ){
            return false;
        }
        return true;
    }
    
    private static boolean doubleCheckCaseForLegalMoves(Piece piece, Pin[][] kingPin, Position myKingPosition){
        if (
            (kingPin[myKingPosition.row()][myKingPosition.column()] == Pin.DOUBLE_CHECK) && 
            (!(piece instanceof King))
            ){
            return false;
        }
        return true;
    }


    private static boolean pinCasesForLegalMoves(Piece piece, Position piecePosition, Position to, Pin[][] kingPin, Position myKingPosition){
        // caso in cui il re è sotto scacco lineare
        boolean legal = pinCasesCheckLinesCaseForLegalMoves(piece, piecePosition, to, kingPin, myKingPosition);
        if(!legal) { return false; }

        // caso cavalli
        // sotto scacco da cavallo
        legal = checkFromKnightCaseForLegalMoves(piece, piecePosition, to, kingPin, myKingPosition);
        if(!legal) { return false; }
        return true;
    }

    private static boolean pinCasesCheckLinesCaseForLegalMoves(Piece piece, Position piecePosition, Position to, Pin[][] kingPin, Position myKingPosition){
        if (
            (kingPin[myKingPosition.row()][myKingPosition.column()] == Pin.UNDER_CHECK_LINE) && 
            !(piece instanceof King)
            ){
            // considero prima se: il pezzo è in Pin NON può muoversi
            if (kingPin[piecePosition.row()][piecePosition.column()] == Pin.PINNED) {
                return false;
            } // se il pezzo non è in PIN può solo coprire le case denotate come CHECK_PATH o
              // come King_attacker
            else if (
                (kingPin[to.row()][to.column()] != Pin.CHECK_PATH) && 
                (kingPin[to.row()][to.column()] != Pin.KING_ATTACKER)
                ){
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
        return true;
    }

    private static boolean checkFromKnightCaseForLegalMoves(Piece piece, Position piecePosition, Position to, Pin[][] kingPin, Position myKingPosition){
        if (
            (kingPin[myKingPosition.row()][myKingPosition.column()] == Pin.UNDER_CHECK_KNIGHT) && 
            (!(piece instanceof King))
            ){
            // Se il pezzo è in PIN, non può muoversi comunque per difendere dal cavallo
            if (kingPin[piecePosition.row()][piecePosition.column()] == Pin.PINNED) {
                return false;
            }
            // Può muoversi solo se la casa d'arrivo coincide con l'attaccante (lo mangia)
            if (kingPin[to.row()][to.column()] != Pin.KING_ATTACKER) {
                return false;
            }
        }
        return true;
    }

    private static boolean pieceIsKingCaseForLegalMoves(ChessBoard board, Piece piece, Position piecePosition, Position to, Position from, Pin[][] kingPin){
        if (piece instanceof King k) {
            // 1. non può muoversi su case controllate da avversario
            if (
                (piece.getColour() == (Colour.WHITE) && board.getSquaresControlledBy(Colour.BLACK)[to.row()][to.column()] != 0) ||
                (piece.getColour() == (Colour.BLACK) && board.getSquaresControlledBy(Colour.WHITE)[to.row()][to.column()] != 0)
                ){
                return false;
            }

            // 3. controllo sull'arrocco
            int direction = to.column() - from.column(); // Destra (+) o Sinistra (-)

            if (Math.abs(direction) == 2) {
                int row = piecePosition.row();
                Piece rook;

                // Trova la torre corretta in base alla direzione
                if (direction > 0) { // Verso Destra -> Arrocco Corto
                    rook = board.getBoard()[row][7];
                } else { // Verso Sinistra -> Arrocco Lungo
                    rook = board.getBoard()[row][0];
                }

                // Validazione base dei pezzi coinvolti e dello scacco attuale
                if (
                    (rook == null) || 
                    !(rook instanceof Rook) || 
                    ((Rook) rook).getHasMoved() ||
                    k.getHasMoved() || 
                    kingPin[row][piecePosition.column()] != null
                    ){
                    return false;
                }

                // Recuperiamo la matrice di controllo dell'avversario
                int[][] adversaryControl = k.getColour() == (Colour.WHITE) ? board.getSquaresControlledBy(Colour.BLACK) : board.getSquaresControlledBy(Colour.WHITE);

                // ARROCCO CORTO (Verso destra)
                if (direction > 0) {
                    // Le case di passaggio (colonne 5 e 6) devono essere VUOTE e NON CONTROLLATE
                    if (
                        board.getBoard()[row][5] != null || 
                        board.getBoard()[row][6] != null ||
                        adversaryControl[row][5] != 0 || 
                        adversaryControl[row][6] != 0
                        ){
                        return false;
                    }
                }
                // ARROCCO LUNGO (Verso sinistra)
                else {
                    // Le case di passaggio (colonne 1, 2, 3) devono essere VUOTE
                    if (
                        board.getBoard()[row][1] != null || 
                        board.getBoard()[row][2] != null || 
                        board.getBoard()[row][3] != null ||
                        adversaryControl[row][2] != 0 || adversaryControl[row][3] != 0
                        ){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean pieceIsPawnEnpassantCaseForLegalMoves(ChessBoard board, boolean legal, Piece piece, Position from, Position to){
        boolean rowDiff = false;
        Pawn lastPawnMoved = board.getLastPawnMoved();
        Position lastPawnFromPosition = board.getLastPawnFromPosition();
        if (board.getLastPawnMoved() != null) {
            rowDiff = Math.abs(lastPawnFromPosition.row() - lastPawnMoved.getPosition().row()) != 2;
        }

        // pedone: sasa
        if (piece instanceof Pawn) {
            // 1. non può spostarsi in avanti se è presente un'altro pezzo
            if (
                from.row() != to.row() && 
                from.column() == to.column() && 
                !board.isNull(to)
                ){
                legal = false;
            }
            // 2. enpassant
            if (
                from.row() != to.row() && 
                from.column() != to.column() && 
                board.isNull(to)
                ){
                if (lastPawnMoved != null) {
                    if (
                        to.column() == lastPawnMoved.getPosition().column() && 
                        from.row() == lastPawnMoved.getPosition().row()
                        ){
                        if (rowDiff){ 
                            legal = false;
                        }
                    } else{
                        legal = false;
                    }
                } else{
                    legal = false;
                }
            }
        }
        return legal;
    }
}
