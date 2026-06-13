package org.chess.dataTypes;
import org.chess.pieces.Piece;

public record Move(Piece selectedPiece, Position to){
    public static final Move INVALID = new Move(null, null);
} 
