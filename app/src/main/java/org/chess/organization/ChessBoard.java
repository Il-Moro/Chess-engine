package org.chess.organization;

import java.util.*;

import org.chess.dataTypes.*;
import org.chess.pieces.*;

public class ChessBoard {

    private Piece[][] chessboard;
    private int[][] squaresControlledByWhite;
    private int[][] squaresControlledByBlack;
    private Pin[][] kingPin; // matrice che controlla a partire dal re: Pieces in pin, Pieces attacker, marca il re se è sotto scacco da un pezzo lineare oppure da un cavallo o se è in doppio scacco
    private King whiteKing;
    private King blackKing;
    private Pawn lastPawnMoved = null;
    private Position lastPawnFromPosition = null;

    // costruttore
    public ChessBoard() {
        this.chessboard = new Piece[8][8];
        this.squaresControlledByBlack = new int[8][8];
        this.squaresControlledByWhite = new int[8][8];
        this.kingPin = new Pin[8][8];
        // per evitare errori se in Chessboard non sono stati inseriti re
        this.whiteKing = null;
        this.blackKing = null;
    }

    // setter
    public Piece getPiece(Position position) {
        return chessboard[position.row()][position.column()];
    }

    public void setPiece(Piece piece) {
        this.chessboard[piece.getPosition().row()][piece.getPosition().column()] = piece;
        if(piece instanceof King k){
            if(piece.getColour().equals("white"))
                whiteKing = k;
            else{
                blackKing = k;
            }
        }
    }

    // getter
    public Piece[][] getBoard(){
        return this.chessboard;
    }

    public int[][] getSquareControlledBy(String colour){
        if(colour.equals("white")){
            return this.squaresControlledByWhite;
        } else{
            return this.squaresControlledByBlack;
        }
    }    

    public boolean isNull(Position position) {
        return chessboard[position.row()][position.column()] == null;
    }

    public Pin[][] getKingPin(){
        return this.kingPin;
    }

    // movement of pieces
    public boolean isMoveLegal(Position from, Position to){
        Piece piece = this.getPiece(from);

        // Se non c'è un pezzo nella posizione di partenza
        if(piece == null){
            return false;
        }

        Position piecePosition = piece.getPosition();
        String pieceColour = piece.getColour();
        boolean rowDiff=false;
        boolean colDiff=false;
        
        if (lastPawnMoved != null) {
            rowDiff=Math.abs(lastPawnFromPosition.row()-lastPawnMoved.getPosition().row()) != 2;
            colDiff=Math.abs(lastPawnFromPosition.column()-lastPawnMoved.getPosition().column())!=0;   
        }
        
        boolean legal=true;

        // GESTIONE CASI GENERICI
        // eseguo controllo sul primo filtro di getPotentialMoves
        Set<Position> potentialMoves = piece.getPotentialMoves(this);
        if(!potentialMoves.contains(to)){
            return false;
        }
        
        // mossa su pezzo proprio sasa
        /*
            else if (!piece.getPotentialMoves(this).contains(to)) {
                legal = false;
            }
         */
        if(this.getPiece(to)!=null&& piece.getColour().equals(this.getPiece(to).getColour())){
            return false;
        }

        // GESTIONE CASI PARTICOLARI

        // casistiche re: moro
        King myKing = (pieceColour.equals("white")) ? this.whiteKing:this.blackKing;
        Position myKingPosition = myKing.getPosition();

        // //      2. Pin o mossa obbligata del re: se il re è sotto scacco bisogna coprirlo -> controllare in kinPin o muoverlo; se c'è un doppio scacco -> mossa obbligata SOLO sul re, tutti gli altri pezzi sono esclusi -> controllo su DOUBLE_UNDER_CHECK in kingPin

        // idea: devo verificare se il re dopo aver eseguito la mossa è sotto scacco
        // idea: memorizzo posizione dei re e una matrice di controllo, con RayCasting verifico se ci sono pezzi pinnati
        
        if(kingPin[myKingPosition.row()][myKingPosition.column()] == Pin.DOUBLE_CHECK && !(piece instanceof King)) { 
            return false; 
        }
        // sotto scacco lineare
        if(kingPin[myKingPosition.row()][myKingPosition.column()] == Pin.UNDER_CHECK_LINE){
            // considero prima se: il pezzo è in Pin NON può muoversi
            if(
                kingPin[piecePosition.row()][piecePosition.column()] == Pin.PINNED
            ){ 
                return false; 
            } // se il pezzo non è in PIN può solo coprire le case denotate come CHECK_PATH o come King_attacker
            else if(kingPin[to.row()][to.column()] != Pin.CHECK_PATH && kingPin[to.row()][to.column()] != Pin.KING_ATTACKER) { 
                return false;
            }
        } else{ // il re non è sotto scacco: controllo se il pezzo è in pin e la casa di arrivo se si trova lungo la direzione di Pin
            if(kingPin[piece.getPosition().row()][piece.getPosition().column()] == Pin.PINNED){
                int deltaRowKing = piecePosition.row() - myKingPosition.row();
                int deltaColKing = piecePosition.column() - myKingPosition.column();
                int deltaRowTo = to.row() - myKingPosition.row();
                int deltaColTo = to.column() - myKingPosition.column();

                if((deltaRowKing * deltaColTo) - (deltaColKing * deltaRowTo) != 0){
                    return false;
                }
            }
        }
        
        // caso cavalli
        // sotto scacco da cavallo
        if (kingPin[myKingPosition.row()][myKingPosition.column()] == Pin.UNDER_CHECK_KNIGHT && !(piece instanceof King)) {
            // Se il pezzo è in PIN, non può muoversi comunque per difendere dal cavallo
            if (kingPin[piecePosition.row()][piecePosition.column()] == Pin.PINNED) { 
                return false; 
            }
            // Può muoversi solo se la casa d'arrivo coincide con l'attaccante (lo mangia)
            if (kingPin[to.row()][to.column()] != Pin.KING_ATTACKER) {
                return false;
            }
        }

        if(piece instanceof King){
            //      1. non può muoversi su case controllate da avversario
            //      controllo che Position to sia a 0 in squaresControlledBy<adversary>
            if(
                (piece.getColour().equals("white") && squaresControlledByBlack[to.row()][to.column()] != 0) ||
                (piece.getColour().equals("black") && squaresControlledByWhite[to.row()][to.column()] != 0)
            ){
                return false;
            }
            
            //      3. controllo sull'arrocco
            // NOTA: aggiungere condizione di verifica sul re che non abbia mai mosso, controllare se il re è sotto scacco (sempre su kingPin), sontrolllare su matrici di controllo le case tra re e torre, controllare la torre se non è mai stata mossa
        }


        // pedone: sasa 
        else if(piece instanceof Pawn){
            //      1. non può spostarsi in avanti se è presente un'altro pezzo
            if(from.row()!=to.row() && from.column()==to.column() && !this.isNull(to))
                legal=false;
            //      2. enpassant: considerare traversa iniziale e se si affianca a un pedone opposto
            
            if(from.row()!=to.row() && from.column()!=to.column() && this.isNull(to)){
                if(lastPawnMoved != null){
                    if(to.column() == lastPawnMoved.getPosition().column() && from.row() == lastPawnMoved.getPosition().row()){
                        if(colDiff || rowDiff)
                            legal = false;
                    }
                    else legal = false;
                }
                else
                    legal=false;
            }
        }

        // checkmate: insieme

        if(legal){
            if(piece instanceof Pawn){
                lastPawnMoved = (Pawn) piece;
                lastPawnFromPosition = from;
            }
            else{
                lastPawnMoved = null;
                lastPawnFromPosition = null;
            }
        }
        return legal;
    }

    public void physicalMovement(Position from, Position to){

        // arrocco
        // promozione

        Piece piece = this.getPiece(from);
        this.chessboard[from.row()][from.column()] = null;
        this.chessboard[to.row()][to.column()] = piece;
        piece.setPosition(to);        
        this.updateControl();

        // aggiunte righe di update per kingPin
        if(piece.getColour().equals("white") && this.blackKing != null){
            this.updateKingPin("black");    
        } else if(this.whiteKing != null){
            this.updateKingPin("white");
        }        
    }

    
    public void updateKingPin(String colour) {
        if((blackKing == null && colour.equals("black")) || (whiteKing == null && colour.equals("white"))){
            return;
        }
        // Reset preventivo della matrice prima di calcolare i nuovi pin/check
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                kingPin[r][c] = null;
            }
        }

        int[][] linearDirections = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int[][] knightDirections = {{2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}};

        int kingRow = colour.equals("white") ? whiteKing.getPosition().row() : blackKing.getPosition().row();
        int kingColumn = colour.equals("white") ? whiteKing.getPosition().column() : blackKing.getPosition().column();

        int checks = 0;

        checks += calculateLinearDirections(colour, linearDirections, kingRow, kingColumn, checks);
        calculateKnightDirections(colour, knightDirections, kingRow, kingColumn, checks);        
    }

    private int calculateLinearDirections(String colour, int[][] directions, int kingRow, int kingColumn, int checks) {

        for (int[] d : directions) {           
            int targetRow = kingRow + d[0];
            int targetColumn = kingColumn + d[1];
            Piece ownPiece = null; 
            
            while (0 <= targetRow && targetRow < 8 && 0 <= targetColumn && targetColumn < 8) {
                Piece targetPiece = chessboard[targetRow][targetColumn];
                
                if (targetPiece != null) {
                    if (targetPiece.getColour().equals(colour)) {
                        if (ownPiece == null) {
                            ownPiece = targetPiece; 
                        } else {
                            break; // Secondo pezzo amico, linea bloccata
                        }
                    } 
                    else {
                        boolean isDiagonal = (d[0] * d[1] != 0);
                        
                        if (ownPiece != null) {
                            // gestione pin
                            if (
                                (isDiagonal && (targetPiece instanceof Bishop || targetPiece instanceof Queen)) ||
                                (!isDiagonal && (targetPiece instanceof Rock || targetPiece instanceof Queen))
                            ) {
                                kingPin[ownPiece.getPosition().row()][ownPiece.getPosition().column()] = Pin.PINNED;
                        } else if(
                            (isDiagonal && (targetPiece instanceof Bishop || targetPiece instanceof Queen)) || 
                            (!isDiagonal && (targetPiece instanceof Rock || targetPiece instanceof Queen))
                        ){
                            checks += 1;
                            if (checks == 1) {
                                kingPin[kingRow][kingColumn] = Pin.UNDER_CHECK_LINE;
                            } else {
                                kingPin[kingRow][kingColumn] = Pin.DOUBLE_CHECK;
                            }

                            kingPin[targetRow][targetColumn] = Pin.KING_ATTACKER;
                            markCheckPath(kingRow, kingColumn, targetRow, targetColumn, d);
                            }
                        }               
                        break; 
                    }
                }
                targetRow += d[0];
                targetColumn += d[1];
            }
        }
        return checks;
    }

    private void calculateKnightDirections(String colour, int[][] directions, int kingRow, int kingColumn, int checks) {

        for (int[] d : directions) {           
            int targetRow = kingRow + d[0];
            int targetColumn = kingColumn + d[1];
            
            if (0 <= targetRow && targetRow < 8 && 0 <= targetColumn && targetColumn < 8) {
                Piece targetPiece = chessboard[targetRow][targetColumn];

                if (targetPiece != null) {
                    if (!targetPiece.getColour().equals(colour) && targetPiece instanceof Knight) {
                        checks += 1;
                        
                        if (checks == 1) {
                            kingPin[kingRow][kingColumn] = Pin.UNDER_CHECK_KNIGHT;
                        } else {
                            kingPin[kingRow][kingColumn] = Pin.DOUBLE_CHECK;
                        }
                        kingPin[targetRow][targetColumn] = Pin.KING_ATTACKER;
                    }
                }
            }
        }
    }

    private void markCheckPath(int startRow, int startCol, int endRow, int endCol, int[] d) {
        int r = startRow + d[0];
        int c = startCol + d[1];
        while (r != endRow || c != endCol) {
            kingPin[r][c] = Pin.CHECK_PATH;
            r += d[0];
            c += d[1];
        }
    }

    // Updating control maps
    public void updateControl(){
        // azzero tabelle
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                this.squaresControlledByBlack[i][j] = 0;
                this.squaresControlledByWhite[i][j] = 0;
            }
        }

        for(Piece[] positions : this.chessboard){
            for(Piece p : positions){
                if(p != null){
                    this.fillControlMap(p);
                }
            }
        }        
    }

    private void fillControlMap(Piece piece){
        Set<Position> set = piece.getPotentialMoves(this);

        if(piece.getColour().equals("white")){
            for(Position s : set){
                this.squaresControlledByWhite[s.row()][s.column()] += 1;
            }
        } else{
            for(Position s : set){
                this.squaresControlledByBlack[s.row()][s.column()] += 1;
            }
        }
    }
}