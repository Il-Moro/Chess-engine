package org.chess.organization;


import java.util.ArrayList;
import java.util.List;

import org.chess.dataTypes.Colour;
import org.chess.dataTypes.Move;
import org.chess.dataTypes.Position;
import org.chess.pieces.Piece;

public abstract class Player {

    protected ChessBoard board;
    protected Colour colour;
    

    public Player(Colour colour, ChessBoard board){
        this.colour=colour;
        this.board=board;
    }

    protected List<Piece> getAlivePieces(){
        List<Piece> pieces = new ArrayList<>();
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                Piece piece=board.getPiece(new Position(i,j));
                if(piece != null && piece.getColour() == colour)
                    pieces.add(piece);
            }
        }
        return pieces;
    }

    public abstract Move decideMove();

}
