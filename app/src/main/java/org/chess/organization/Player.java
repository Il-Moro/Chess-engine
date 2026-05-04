package org.chess.organization;
import org.chess.dataTypes.Color;
import org.chess.dataTypes.Position;
import org.chess.pieces.Piece;

    public class Player {

        private final String playerName;
        private final Color color;

        public Player(String playerName,Color color){
            if (playerName == null || playerName.trim().isEmpty()) {
                throw new IllegalArgumentException("The name can' t be null or empty");
            }

            if (color == null) {
                throw new IllegalArgumentException("the color can't be null");
            }
            this.playerName=playerName;
            this.color=color;
        }

        public String getName(){
            return playerName;
        }

        public Color getColor(){
            return color;
        }
        
    }
