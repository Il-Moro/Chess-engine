package org.chess.organization;

    public class Player {

        private String playerName;
        private String color;

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
