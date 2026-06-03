package org.chess.dataTypes;

import org.chess.pieces.Piece;

public record UndoInfo(Piece movedPiece, Position from, Position to, Piece eatenPiece, SpecialMoves special) {
}
