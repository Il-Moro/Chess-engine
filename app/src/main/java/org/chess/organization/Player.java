package org.chess.organization;
import org.chess.dataTypes.Position;
import org.chess.pieces.Piece;

    public class Player {

        public final String playerName;
        public final String color;

        public Player(String playerName,String color){
            if (playerName == null || playerName.trim().isEmpty()) {
                throw new IllegalArgumentException("The name can' t be null or empty");
            }

            if (color == null || color.trim().isEmpty()) {
                throw new IllegalArgumentException("the color can't be null");
            }
            this.playerName=playerName;
            this.color=color;
        }

        public String getName(){
            return playerName;
        }

        public String getColor(){
            return color;
        }
        
    }
