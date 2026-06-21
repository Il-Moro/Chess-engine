package org.chess.organization;

import org.chess.dataTypes.*;
import org.chess.pieces.*;


public class MoveApplier {

    public static UndoInfo physicalMovement(ChessBoard board, Position from, Position to, String stringPiece) {
        
        Piece piece = board.getPiece(from);
        Colour pieceColour = piece.getColour();

        // CASTLING
        UndoInfo undoInfo = physicalMovementCastling(board, piece, from, to);
        if(undoInfo != null){ return undoInfo; }

        // PROMOTION
        undoInfo = physicalMovementPromotion(board, piece, pieceColour, from, to, stringPiece);
        if(undoInfo != null){ return undoInfo; }

        // ENPASSANT
        undoInfo = physicalMovementEnpassant(board, piece, from, to);
        if(undoInfo != null){ return undoInfo; }
            
        // GENERAL CASE
        undoInfo = physicalMovementGeneralCase(board, piece, board.getPiece(to), from, to);
        
        // In MoveApplier.java alla fine di physicalMovement:
        if (piece instanceof Pawn) {
            board.setLastPawnMoved((Pawn) piece);
            board.setLastPawnFromPosition(from);
        } else {
            board.setLastPawnMoved(null);
            board.setLastPawnFromPosition(null);
        }

        return undoInfo;
    }

    public static void undoMove(ChessBoard board, UndoInfo undo) {
        switch (undo.special()) {
            case SpecialMoves.SHORT_CASTLING:
                King k = (King) undo.movedPiece();
                k.setPosition(undo.from());
                k.setHasMovedFalse();
                board.setPiece(k);
                board.setNull(undo.to());

                // spostamento della torre affianco al re
                Rook rook = (Rook) board.getPiece(new Position(undo.from().row(), 5));
                rook.setPosition(new Position(undo.from().row(), 7));
                rook.setHasMovedFalse();
                board.setPiece(rook);
                board.setNull(new Position(undo.from().row(), 5));

                break;

            case SpecialMoves.LONG_CASTLING:
                k = (King) undo.movedPiece();
                k.setPosition(undo.from());
                k.setHasMovedFalse();
                board.setPiece(k);
                board.setNull(undo.to());

                rook = (Rook) board.getPiece(new Position(undo.from().row(), 3));
                rook.setPosition(new Position(undo.from().row(), 0));
                rook.setHasMovedFalse();
                board.setPiece(rook);
                board.setNull(new Position(undo.from().row(), 3));

                break;

            case SpecialMoves.ENPASSANT:
                Pawn pawn = (Pawn) undo.movedPiece();
                pawn.setPosition(undo.from());
                board.setPiece(pawn);
                board.setNull(undo.to());

                // Ripristino pezzo mangiato
                Piece enemyPawn = undo.eatenPiece();
                Position enemyOriginalPosition = new Position(undo.from().row(), undo.to().column());
                enemyPawn.setPosition(enemyOriginalPosition);
                board.setPiece(enemyPawn);
                break;
            case SpecialMoves.PROMOTION:
                // Ripristino pedone originale
                Piece originalPawn = undo.movedPiece();
                originalPawn.setPosition(undo.from());
                board.setPiece(originalPawn);

                if (undo.eatenPiece() != null) {
                    board.setPiece(undo.eatenPiece());
                } else {
                    board.setNull(undo.to());
                }
                break;

            default:
                Piece piece = undo.movedPiece();
                piece.setPosition(undo.from());
                board.setPiece(piece);

                if (undo.eatenPiece() != null)
                    board.setPiece(undo.eatenPiece());
                else {
                    board.setNull(undo.to());
                }
                break;
        }
        // Ricalcolo Pin e del control Maps
        board.updateControlMap();
        if (board.getKing(Colour.WHITE) != null && board.getKing(Colour.BLACK) != null) {
            board.updateKingPin();
        }
    }

















    private static UndoInfo physicalMovementCastling(ChessBoard board, Piece piece, Position from, Position to){
        int direction = to.column() - from.column();
        
        if (piece instanceof King k && Math.abs(direction) == 2) {
            // arrocco corto
            if (direction > 0) {
                return physicalMovementShortCastling(board, k, from, to);
                // arrocco lungo
            } else if (direction < 0) {
                return physicalMovementLongCastling(board, k, from, to);
            }
        }
        return null;
    } 

    private static UndoInfo physicalMovementShortCastling(ChessBoard board, King k, Position from, Position to){
        k.setPosition(to);
        k.setHasMovedTrue();
        board.setPiece(k);
        board.setNull(from);

        // spostamento della torre affianco al re
        Rook rook = (Rook) board.getPiece(new Position(to.row(), 7));
        rook.setPosition(new Position(to.row(), 5));
        rook.setHasMovedTrue();
        board.setPiece(rook);
        board.setNull(new Position(to.row(), 7));
        updateAfterMove(board, k, from);
        return new UndoInfo(k, from, to, null, SpecialMoves.SHORT_CASTLING);
    }

    private static UndoInfo physicalMovementLongCastling(ChessBoard board, King k, Position from, Position to){
        k.setPosition(to);
        k.setHasMovedTrue();
        board.setPiece(k);
        board.setNull(from);

        // spostamento della torre affianco al re
        Rook rook = (Rook) board.getPiece(new Position(to.row(), 0));
        rook.setPosition(new Position(to.row(), 3));
        rook.setHasMovedTrue();
        board.setPiece(rook);
        board.setNull(new Position(to.row(), 0));
        updateAfterMove(board, k, from);
        return new UndoInfo(k, from, to, null, SpecialMoves.LONG_CASTLING);
    }

    private static UndoInfo physicalMovementPromotion(ChessBoard board, Piece piece, Colour pieceColour, Position from, Position to, String stringPiece){
        if (piece instanceof Pawn && (
                (piece.getColour() == Colour.WHITE && to.row() == 7) || 
                piece.getColour() == Colour.BLACK && to.row() == 0)
            ){
            Piece promotionPiece = switch (stringPiece) {
                case "R" -> new Rook(to, pieceColour, true);
                case "B" -> new Bishop(to, pieceColour);
                case "K" -> new Knight(to, pieceColour);
                default -> new Queen(to, pieceColour);
            };

            Piece eatenPiece = board.getPiece(to);
            board.setPiece(promotionPiece);
            board.setNull(from);
            updateAfterMove(board, piece, from);
            return new UndoInfo(piece, from, to, eatenPiece, SpecialMoves.PROMOTION);
        }
        
        return null;
    }

    private static UndoInfo physicalMovementEnpassant(ChessBoard board, Piece piece, Position from, Position to){
        boolean rowDiff = false;
        Pawn lastPawnMoved = board.getLastPawnMoved();
        Position lastPawnFromPosition = board.getLastPawnFromPosition();


        if (lastPawnMoved != null) {
            rowDiff = Math.abs(lastPawnFromPosition.row() - lastPawnMoved.getPosition().row()) == 2;
        }

        if (piece instanceof Pawn && (
                from.row() != to.row() && 
                from.column() != to.column() && 
                board.isNull(to))
            ){
            if (lastPawnMoved != null) {
                if (
                    to.column() == lastPawnMoved.getPosition().column() && 
                    from.row() == lastPawnMoved.getPosition().row()
                    ){
                    if (rowDiff) {
                        piece.setPosition(to);
                        board.setPiece(piece);
                        board.setNull(from);
                        board.setNull(lastPawnMoved.getPosition());
                        updateAfterMove(board, piece, from);
                        return new UndoInfo(piece, from, to, lastPawnMoved, SpecialMoves.ENPASSANT);
                    }

                }
            }
        }
        return null;
    }

    private static UndoInfo physicalMovementGeneralCase(ChessBoard board, Piece piece, Piece eatenPiece, Position from, Position to){
        piece.setPosition(to);
        board.setNull(from);
        board.setPiece(piece);
        updateAfterMove(board, piece, from);
        return new UndoInfo(piece, from, to, eatenPiece, SpecialMoves.NONE);
    }

    private static void updateAfterMove(ChessBoard board, Piece piece, Position from) {

        board.updateControlMap();

        if (board.getKing(Colour.WHITE) != null && board.getKing(Colour.BLACK) != null) {
            UpdaterMaps.updateKingPin(board);
        }

        if (piece instanceof King k) {
            k.setHasMovedTrue();
        } else if (piece instanceof Rook r) {
            r.setHasMovedTrue();
        }

    }

    
}
