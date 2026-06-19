package org.chess.graphics;

import org.chess.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SwingChessView  implements ChessView {
        
    private ChessController controller;
    private JFrame frame;
    private JPanel setupPanel;
    private String selectedMode;
    private String selectedColor;
    private String selectedDifficulty;

    public SwingChessView() {
        initFrame();
        setupScreen();
        show();
    }


    public void setController(ChessController controller) {
        this.controller = controller;
    }

    private void initFrame() {
        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
    }


    @Override
    public void setupScreen() {
        frame.getContentPane().removeAll();

        setupPanel = new JPanel();
        setupPanel.setLayout(new GridLayout(5, 1, 10, 10));
        setupPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Select Game Mode", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        JButton humanVsAi = new JButton("Human vs AI");
        JButton humanVsHuman = new JButton("Human vs Human");
        JComboBox<String> colorComboBox = new JComboBox<>(new String[]{"WHITE", "BLACK"});
        JComboBox<String> difficultyComboBox = new JComboBox<>(new String[]{"EASY", "MEDIUM", "HARD"});
        JButton startButton = new JButton("Start Game");

        humanVsAi.addActionListener(e -> { 
            selectedMode = humanVsAi.getText();
            difficultyComboBox.setVisible(true);
        });
        
        humanVsHuman.addActionListener(e -> selectedMode = humanVsHuman.getText());
        colorComboBox.addActionListener(e -> selectedColor = (String) colorComboBox.getSelectedItem());
        
        difficultyComboBox.addActionListener(e -> selectedDifficulty = (String) difficultyComboBox.getSelectedItem());
        difficultyComboBox.setVisible(false);        

        startButton.addActionListener(e -> {
            if (controller != null) {
                controller.startGame(selectedMode, selectedColor, selectedDifficulty);
            }
        });

        setupPanel.add(title);
        setupPanel.add(humanVsAi);
        setupPanel.add(humanVsHuman);
        setupPanel.add(colorComboBox);
        setupPanel.add(difficultyComboBox);
        setupPanel.add(startButton);

        frame.add(setupPanel, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void gameScreen() { 
    }


    @Override
    public void displayBoard(Piece[][] board) {
        
    }

    @Override
    public void highlightSquare(int row, int col, boolean highlight) {}

    @Override
    public void clearHighlights() {}

    @Override
    public void setStatus(String message) {

    }

    @Override
    public void gameover(String result) {}

    @Override
    public void show() {
        frame.setVisible(true);
    }
}