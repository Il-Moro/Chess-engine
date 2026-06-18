package org.chess.organization;

import java.util.List;

import org.chess.dataTypes.Colour;
import org.chess.dataTypes.UndoInfo;
import org.chess.dataTypes.Move;


public class PlayerAgent extends Player{

    private final int SEARCH_DEPTH;
    private Move bestMove = null;
    private StateEvaluation evaluator;
    private Player opponent;

    public PlayerAgent(Colour colour, ChessBoard board, int SEARCH_DEPTH, Player opponent) {
        super(colour,board);
        this.SEARCH_DEPTH=SEARCH_DEPTH;
        this.opponent = opponent;
    }


    @Override
    public Move decideMove(){
        this.evaluator = new StateEvaluation(board,this,this.opponent);
        maximizer(SEARCH_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return bestMove;
    }

    private int maximizer(int depth, int alpha, int beta){
        
        if(depth==0){
            return evaluator.evaluate();
        }

        List<Move> legalMoves = super.computeAllLegalMoves();
        
        if(legalMoves.isEmpty()){
            return evaluator.evaluate();
        }

        for(Move move: legalMoves){
            UndoInfo undoInfo = board.physicalMovement(move.selectedPiece().getPosition(), move.to());
            int rating = minimizer(depth-1, alpha, beta);
            board.undoMove(undoInfo);
            
            if(rating > alpha){
                alpha = rating;
                if(depth == SEARCH_DEPTH){
                    bestMove = move;
                }
            }

            if(alpha >= beta){
                return alpha;
            }

        }

        return alpha;
    }

    private int minimizer(int depth, int alpha, int beta){
        
        if(depth==0){
            return evaluator.evaluate();
        }

        List<Move> legalMoves = opponent.computeAllLegalMoves();
        
        if(legalMoves.isEmpty()){
            return evaluator.evaluate();
        }

        for(Move move: legalMoves){
            UndoInfo undoInfo = board.physicalMovement(move.selectedPiece().getPosition(), move.to());
            int rating = maximizer(depth-1, alpha, beta);
            board.undoMove(undoInfo);
            
            if(rating <= beta){
                beta = rating;

            }

            if (alpha >= beta){
                return beta;
            }

        }

        return beta;
    }
}