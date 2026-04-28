package org.chess.pieces;

import java.util.HashSet;
import java.util.Set;

import org.chess.dataTypes.*;
import org.chess.organization.*; 

public class Pawn extends Piece{

    public Pawn(Position position, String colour){
        super(position, colour);
    }

    @Override
    public Set<Position> getLegalMoves(ChessBoard chessBoard) {
        
        Set<Position> moves = new HashSet<>();

        int pieceRow = this.position.row();
        int pieceColumn = this.position.column();

        int[][] push = {{1, 0}, {2, 0}};
        int[][] eat = {{1,-1}, {1, 1}};
        
        if(1 <= pieceRow && pieceRow < 7 && 0 <= pieceColumn && pieceColumn < 8){
            if(pieceRow == 1){
                for(int[] d : push){
                    int row = pieceRow + d[0];
                    int col = pieceColumn + d[1];

                    moves.add(new Position(row, col));
                }
            } else {
                int row = pieceRow + push[0][0];
                int col = pieceColumn + push[0][1];

                moves.add(new Position(row, col));
            }
        }
        return moves;
    }
}