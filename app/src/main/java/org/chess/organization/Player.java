package org.chess.organization;

import org.chess.pieces.Piece;

import org.chess.dataTypes.Colour;
import org.chess.dataTypes.Move;
import org.chess.dataTypes.Position;

public abstract class Player {

    protected ChessBoard board;
    protected Colour colour;
    

    public Player(Colour colour, ChessBoard board){
        this.colour=colour;
        this.board=board;
    }
    public abstract Move submitMove(Piece selectedPiece, Position to);

}
