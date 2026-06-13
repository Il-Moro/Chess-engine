package org.chess.organization;

import org.chess.dataTypes.Position;
import org.chess.dataTypes.Colour;
import org.chess.dataTypes.Move;
import org.chess.pieces.Piece;

public class PlayerHuman extends Player{
    private Piece selectedPiece;
    private Position to;

    public PlayerHuman(Colour colour, ChessBoard board){
        super(colour, board);
    }

    @Override
    public Move decideMove() {
        return determinMove();
    }


    public Move determinMove(){
        if(super.board.isMoveLegal(selectedPiece.getPosition(), to)){
            super.board.physicalMovement(selectedPiece.getPosition(), to);
            return  new Move(selectedPiece,to);
        }
        return Move.INVALID;
    }

    public void setMove(Piece selectedPiece,Position to){
        this.selectedPiece=selectedPiece;
        this.to=to;
    }
}
