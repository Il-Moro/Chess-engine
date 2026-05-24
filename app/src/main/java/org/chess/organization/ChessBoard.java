package org.chess.organization;

import java.util.*;

import org.chess.dataTypes.*;
import org.chess.pieces.*;

public class ChessBoard {

    private Piece[][] chessboard;
    private int[][] squaresControlledByWhite;
    private int[][] squaresControlledByBlack;
    private Pin[][] kingPin;
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
        this.updateControl();
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
        if(colour == "white"){
            return this.squaresControlledByWhite;
        } else{
            return this.squaresControlledByBlack;
        }
    }    

    public boolean isNull(Position position) {
        return chessboard[position.row()][position.column()] == null;
    }

    // movement of pieces
    public boolean isMoveLegal(Position from, Position to){
        Piece piece = this.getPiece(from);
        boolean rowDiff=false;
        boolean colDiff=false;
        
        if (lastPawnMoved != null) {
            rowDiff=Math.abs(lastPawnFromPosition.row()-lastPawnMoved.getPosition().row()) != 2;
            colDiff=Math.abs(lastPawnFromPosition.column()-lastPawnMoved.getPosition().column())!=0;   
        }
        
        boolean legal=true;
        

        // GESTIONE CASI GENERICI
        // Se non c'è un pezzo nella posizione di partenza
        if (piece == null) {
            return false;
        }

        // esguo controllo sul primo filtro di getPotentialMoves
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
        // pezzo in pin
        // idea 1: devo verificare se il re dopo aver eseguito la mossa è sotto scacco
        // idea 2: memorizzo posizione dei re e una matrice di controllo, con RayCasting verifico se ci sono pezzi pinnati
        if(this.kingPin[piece.getPosition().row()][piece.getPosition().column()] == Pin.PINNED){
            return false;
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
            //      2. mossa obbligata del re: se il re è sotto scacco bisogna coprirlo -> controllare in kinPin o muoverlo; se c'è un doppio scacco -> mossa obbligata SOLO sul re, tutti gli altri pezzi sono esclusi -> controllo su DOUBLE_UNDER_CHECK in kingPin (NOTA, aggiungere sezione per inserire DOUBLE_UNDER_CHECK su pinKing)
            
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
        /*
        if(piece.getColour().equals("white")){
            this.updateKingPin("black");    
        } else{
            this.updateKingPin("white");
        }
            */
        
    }

    // NOTA aggiungere righe per:
    // 1. verificare scacco di cavallo
    // 2. aggiungere opzione di DOUBLE_UNDER_CHECK sulla casa del re in kingPin se ci sono due pezzi che attaccano il re

    public void updateKingPin(String colour) {
        // Tutte le direzioni per X-ray a partire dalla posizione del re
        int[][] directions = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        
        int kingRow = colour.equals("white") ? whiteKing.getPosition().row() : blackKing.getPosition().row();
        int kingColumn = colour.equals("white") ? whiteKing.getPosition().column() : blackKing.getPosition().column();
        
        // Itero su tutte le direzioni
        for (int[] d : directions) {           
            int targetRow = kingRow + d[0];
            int targetColumn = kingColumn + d[1];
            
            Piece ownPiece = null; // Il pezzo scudo (se esiste)
            boolean doubleShield = false; // Se ci sono due pezzi propri, il pin salta
            
            while (0 <= targetRow && targetRow < 8 && 0 <= targetColumn && targetColumn < 8) {
                Piece targetPiece = chessboard[targetRow][targetColumn];
                
                if (targetPiece != null) {
                    // CASO 1: Incontriamo un pezzo AMICO
                    if (targetPiece.getColour().equals(colour)) {
                        if (ownPiece == null) {
                            ownPiece = targetPiece; // Primo pezzo amico trovato (potenziale pinnato)
                        } else {
                            doubleShield = true; // Secondo pezzo amico: il re è protetto da due pezzi, interrompiamo il raggio
                            break;
                        }
                    } 
                    // CASO 2: Incontriamo un pezzo AVVERSARIO
                    else {
                        boolean isDiagonal = (d[0] * d[1] != 0);
                        boolean validAttacker = false;
                        
                        // Controlliamo se il pezzo avversario minaccia questa linea
                        if (isDiagonal && (targetPiece instanceof Bishop || targetPiece instanceof Queen)) {
                            validAttacker = true;
                        } else if (!isDiagonal && (targetPiece instanceof Rock || targetPiece instanceof Queen)) {
                        }
                        
                        if (validAttacker) {
                            if (ownPiece != null) {
                                // C'è un nostro pezzo in mezzo: lui è PINNED
                                kingPin[ownPiece.getPosition().row()][ownPiece.getPosition().column()] = Pin.PINNED;
                            } else {
                                // SCACCO DIRETTO 
                                kingPin[kingRow][kingColumn] = Pin.UNDER_CHECK;
                                kingPin[targetRow][targetColumn] = Pin.KING_ATTACKER;
                                
                                // Qui puoi riempire la linea tra il Re e l'attaccante con CHECK_PATH
                                markCheckPath(kingRow, kingColumn, targetRow, targetColumn, d);
                            }
                        }
                        // In ogni caso, appena becchi un pezzo avversario il raggio si ferma
                        break; 
                    }
                }
                
                // FONDAMENTALE: Avanza nella direzione, altrimenti loop infinito!
                targetRow += d[0];
                targetColumn += d[1];
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

        if(piece.getColour() == "white"){
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