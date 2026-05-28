package org.chess.dataTypes;
import org.chess.pieces.Piece;

public record Move(Piece selePiece, Position to){} 
