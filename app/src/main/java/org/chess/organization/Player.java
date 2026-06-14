package org.chess.organization;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public List<Move> computeAllLegalMoves(){
        List<Piece> pieces = getAlivePieces();
        List<Move> moves=new ArrayList<>();

        for(Piece p : pieces){
            Set<Position> potentialMoves=p.getPotentialMoves(board);
            for(Position m:potentialMoves){
                if (board.isMoveLegal(p.getPosition(), m)) {
                    moves.add(new Move(p,m));
                }
            }
        }
        return moves;
    }

    public abstract Move decideMove();

}
