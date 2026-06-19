package org.chess.graphics;



public class ChessController {
    private ChessView view;
    private ChessModel model;

    public ChessController(ChessView view, ChessModel model) {
        this.view = view;
        this.model = model;
    }
}
