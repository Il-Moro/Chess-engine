package org.chess.organization;
import org.chess.pieces.Piece;

import java.util.List;

import org.chess.dataTypes.Position;

    public abstract class Player {

        public final String playerName;
        public ChessBoard board;
        public Piece[][] tiles;
        public boolean turn=false;

        public Player(String playerName,String color){
            if (playerName == null || playerName.trim().isEmpty()) {
                throw new IllegalArgumentException("The name can' t be null or empty");
            }
            this.playerName=playerName;
            this.tiles=board.getBoard();
        }

        public String getName(){
            return playerName;
        }

        public void moveSelectedPiece(Piece selectedPiece, Position to){//TODO: return bool not void
            /*
            if(board.movePiece(selectedPiece,to)){
                turn=false;
            }
            */
        }

        public abstract List<Piece> getActivePieces(Piece[][] tiles);
        public abstract List<Piece> getEatenPieces(Piece[][] tiles);
    }
