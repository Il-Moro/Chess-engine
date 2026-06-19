package org.chess.graphics;

import org.chess.pieces.*;
import org.chess.organization.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SwingChessView  implements ChessView {
        
    private JFrame frame;
    private JPanel boardPanel;
    private JLabel statusLabel;
    private JLabel topLabel;
    private JLabel bottomLabel;


    private JPanel[][] squares = new JPanel[8][8];
    private ChessController controller;

    public SwingChessView (){
        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);

        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

        // Pannello superiore (es. "Pezzi avversario")
        JPanel topPanel = new JPanel();
        topLabel = new JLabel("Pezzi avversario");
        topPanel.add(topLabel);
        container.add(topPanel, BorderLayout.NORTH);

        // Pannello centrale: la scacchiera (vuota all'inizio)
        boardPanel = createChessboardPanel(); // crea la griglia 8x8, ma senza icone
        container.add(boardPanel, BorderLayout.CENTER);

        // Pannello inferiore con label di stato
        JPanel bottomPanel = new JPanel();
        statusLabel = new JLabel("In attesa di iniziare...");
        bottomPanel.add(statusLabel);
        container.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(container);
    }

    private JPanel createChessboardPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 8));
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JPanel square = new JPanel(new BorderLayout());
                boolean isLight = (row + col) % 2 == 0;
                square.setBackground(isLight ? new Color(240, 217, 181) : new Color(181, 136, 99));
                square.setOpaque(true);
                /*
                final int r = row, c = col;
                square.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        if (controller != null) {
                            // TODO:controller.onSquareClicked(r, c);
                        }
                    }
                }); */
                squares[row][col] = square;
                panel.add(square);
            }
        }
        return panel;
    }

    @Override
    public void displayBoard(Piece[][] board) {}

    @Override
    public void highlightSquare(int row, int col, boolean highlight) {}

    @Override
    public void clearHighlights() {}

    @Override
    public void showGameOver(String result) {}

    @Override
    public void show() {
        frame.setVisible(true);
    }
}