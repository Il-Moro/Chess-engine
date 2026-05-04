package org.chess.organization;

    public class Player {

        private final String playerName;
        private final String color;

        public Player(String playerName,String color){
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
