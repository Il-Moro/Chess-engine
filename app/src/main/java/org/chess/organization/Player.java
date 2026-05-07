package org.chess.organization;
import org.chess.pieces.Piece;

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

        public abstract Piece[] getActivePieces(Piece[][] tiles);
        public abstract Piece[] getEatenPieces(Piece[][] tiles);
    }
