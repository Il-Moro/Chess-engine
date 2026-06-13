package org.chess.organization;

import org.chess.dataTypes.Position;
import org.chess.dataTypes.Colour;
import org.chess.dataTypes.Move;
import org.chess.pieces.Piece;

public class PlayerHuman extends Player{
    

    public PlayerHuman(Colour colour, ChessBoard board){
        super(colour, board);
    }

    @Override
    public Move submitMove(Piece selectedPiece, Position to){
        if(super.board.isMoveLegal(selectedPiece.getPosition(), to))
            return  new Move(selectedPiece,to);
        
        return Move.INVALID;
    }
}
