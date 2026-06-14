package org.chess.organization;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.chess.dataTypes.Colour;
import org.chess.dataTypes.UndoInfo;
import org.chess.dataTypes.Move;
import org.chess.dataTypes.Position;
import org.chess.pieces.Piece;


public class PlayerAgent extends Player{

    private final int SEARCH_DEPTH;
    private Move bestMove;

    public PlayerAgent(Colour colour, ChessBoard board,int SEARCH_DEPTH){
        super(colour,board);
        this.SEARCH_DEPTH=SEARCH_DEPTH;
    }


    @Override
    public Move decideMove(){
        maximizer(SEARCH_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return bestMove;
    }

    int maximizer(int depth,int alpha,int beta){
        return 0;
    }

    public List<Move> computeAllLegalMoves(){
        List<Piece> pieces = super.getAlivePieces();
        List<Move> moves=new ArrayList<>();

        for(Piece p : pieces){
            Set<Position> potentialMoves=p.getPotentialMoves(super.board);
            for(Position m:potentialMoves){
                if (super.board.isMoveLegal(p.getPosition(), m)) {
                    moves.add(new Move(p,m));
                }
            }
        }
        return moves;
    }

    public int evaluateBoard(){
        return 0;
    }
}
