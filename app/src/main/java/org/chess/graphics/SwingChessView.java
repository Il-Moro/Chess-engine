package org.chess.graphics;

import org.chess.pieces.*;

import javax.swing.*;
import java.awt.*;

public class SwingChessView  implements ChessView {
        
    private ChessController controller;
    private JFrame frame;
    private JPanel setupPanel;
    private String selectedMode;
    private String selectedColor;
    private String selectedDifficulty;
    private boolean humanVsAi = false;

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
        
        CardLayout cardLayout = new CardLayout();
        setupPanel = new JPanel(cardLayout);
        setupPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // MODE
        JLabel titleMode = new JLabel("Select Game Mode", SwingConstants.CENTER);
        titleMode.setFont(new Font("Arial", Font.BOLD, 50));
        JPanel modePanelContainer = new JPanel();
        modePanelContainer.setLayout(new BorderLayout());
        JPanel modePanel = new JPanel();
        modePanel.setLayout(new GridLayout(1, 2, 10, 10));
        modePanelContainer.add(titleMode,BorderLayout.NORTH);
        modePanelContainer.add(modePanel, BorderLayout.CENTER);

        JButton humanVsAi = new JButton("Human vs AI");
        JButton humanVsHuman = new JButton("Human vs Human");

        // Styling
        humanVsAi.setFont(new Font("Segoe UI", Font.BOLD, 20));
        humanVsAi.setBackground(new Color(52, 73, 94));
        humanVsAi.setForeground(Color.WHITE);
        humanVsAi.setFocusPainted(false);
        humanVsAi.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        humanVsAi.setCursor(new Cursor(Cursor.HAND_CURSOR));

        humanVsHuman.setFont(new Font("Segoe UI", Font.BOLD, 20));
        humanVsHuman.setBackground(new Color(52, 73, 94));
        humanVsHuman.setForeground(Color.WHITE);
        humanVsHuman.setFocusPainted(false);
        humanVsHuman.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        humanVsHuman.setCursor(new Cursor(Cursor.HAND_CURSOR));



        modePanel.add(humanVsAi);
        modePanel.add(humanVsHuman);

        humanVsAi.addActionListener(e-> {
            selectedMode = humanVsAi.getText();
            this.humanVsAi = true;
            cardLayout.show(setupPanel, "Color selection");
            
        });
        humanVsHuman.addActionListener(e -> {
            selectedMode = humanVsHuman.getText();
            cardLayout.show(setupPanel, "Color selection");
            
        });

        // ONLY COLOR

        JLabel titleColor = new JLabel("Select Color", SwingConstants.CENTER);
        titleColor.setFont(new Font("Arial", Font.BOLD, 50));
        JPanel colorPanelContainer = new JPanel();
        colorPanelContainer.setLayout(new BorderLayout());
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(1, 2, 10, 10));
        colorPanelContainer.add(titleColor,BorderLayout.NORTH);
        colorPanelContainer.add(colorPanel,BorderLayout.CENTER);

        JButton white = new JButton("WHITE");
        JButton black = new JButton("BLACK");

        // Styling
        white.setFont(new Font("Segoe UI", Font.BOLD, 20));
        white.setBackground(new Color(52, 73, 94));
        white.setForeground(Color.WHITE);
        white.setFocusPainted(false);
        white.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        white.setCursor(new Cursor(Cursor.HAND_CURSOR));

        black.setFont(new Font("Segoe UI", Font.BOLD, 20));
        black.setBackground(new Color(52, 73, 94));
        black.setForeground(Color.WHITE);
        black.setFocusPainted(false);
        black.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        black.setCursor(new Cursor(Cursor.HAND_CURSOR));

        colorPanel.add(white);
        colorPanel.add(black);

        white.addActionListener(e-> {
            selectedColor = white.getText();
            if (this.humanVsAi) {
                cardLayout.show(setupPanel, "Difficulty selection");
            }
            else if (controller != null) {
                controller.startGame(selectedMode, selectedColor,selectedDifficulty);
            }
            
        });
        black.addActionListener(e -> {
            selectedColor = black.getText();
            if (this.humanVsAi) {
                cardLayout.show(setupPanel, "Difficulty selection");
            }
            else if (controller != null) {
                controller.startGame(selectedMode, selectedColor,selectedDifficulty);
            }
        });

        // DIFFICULTY

        JLabel titleDifficulty = new JLabel("Select Difficulty", SwingConstants.CENTER);
        titleDifficulty.setFont(new Font("Arial", Font.BOLD, 50));
        JPanel difficultyContainer = new JPanel();
        difficultyContainer.setLayout(new BorderLayout());
        JPanel difficuPanel = new JPanel();
        difficuPanel.setLayout(new GridLayout(1, 3, 10, 10));
        difficultyContainer.add(titleDifficulty,BorderLayout.NORTH);
        difficultyContainer.add(difficuPanel,BorderLayout.CENTER);

        JButton easy = new JButton("EASY");
        JButton normal = new JButton("NORMAL");
        JButton hard = new JButton("HARD");

        difficuPanel.add(easy);
        difficuPanel.add(normal);
        difficuPanel.add(hard);

        easy.addActionListener(e-> {
            selectedDifficulty = easy.getText();
            if (controller != null) {
                controller.startGame(selectedMode, selectedColor,selectedDifficulty);
            }
        });

        normal.addActionListener(e -> {
            selectedDifficulty = normal.getText();
            if (controller != null) {
                controller.startGame(selectedMode, selectedColor,selectedDifficulty);
            }
        });

        hard.addActionListener(e -> {
            selectedDifficulty = hard.getText();
            if (controller != null) {
                controller.startGame(selectedMode, selectedColor,selectedDifficulty);
            }      
        });


        // Styling

        easy.setFont(new Font("Segoe UI", Font.BOLD, 20));
        easy.setBackground(new Color(52, 73, 94));
        easy.setForeground(Color.WHITE);
        easy.setFocusPainted(false);
        easy.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        easy.setCursor(new Cursor(Cursor.HAND_CURSOR));

        normal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        normal.setBackground(new Color(52, 73, 94));
        normal.setForeground(Color.WHITE);
        normal.setFocusPainted(false);
        normal.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        normal.setCursor(new Cursor(Cursor.HAND_CURSOR));

        hard.setFont(new Font("Segoe UI", Font.BOLD, 20));
        hard.setBackground(new Color(52, 73, 94));
        hard.setForeground(Color.WHITE);
        hard.setFocusPainted(false);
        hard.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        hard.setCursor(new Cursor(Cursor.HAND_CURSOR));

        setupPanel.add(modePanelContainer, "ModeSelection");
        setupPanel.add(colorPanelContainer, "Color selection");
        setupPanel.add(difficultyContainer, "Difficulty selection");

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