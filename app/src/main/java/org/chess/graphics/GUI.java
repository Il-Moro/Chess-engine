package org.chess.graphics;

import org.chess.organization.*;
import org.w3c.dom.events.MouseEvent;
import org.chess.dataTypes.*;
import javax.swing.*;
import java.awt.*;

public class GUI {

    public GUI() {
  

        ChessBoard chessBoard = new ChessBoard(true);

        JFrame frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);


        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));


        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Pezzi avversario"));


        JPanel centerPanel = createChessboardPanel();


        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Pezzi viventi"));


        container.add(topPanel, BorderLayout.NORTH);
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);


        frame.add(container);
        frame.setVisible(true);
    }

    private JPanel createChessboardPanel() {
        
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JPanel square = new JPanel(new BorderLayout());
                boolean isLight = (row + col) % 2 == 0;
                square.setBackground(isLight ? new Color(240, 217, 181) : new Color(181, 136, 99));
                square.setOpaque(true);
                boardPanel.add(square);
            }
        }
        return boardPanel;
    }
}