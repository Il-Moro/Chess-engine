package org.chess.graphics;

import org.chess.dataTypes.Colour;
import org.chess.pieces.*;

import javax.swing.*;
import java.awt.*;

public class SwingChessView  implements ChessView {
        
    private ChessController controller;
    private JFrame frame;
    private JPanel setupPanel;
    private String selectedMode="";
    private String selectedColor="";
    private String selectedDifficulty ="";
    private boolean humanVsAi = false;
    private CardLayout cardLayout = new CardLayout();
    private JButton[][] squares = new JButton[8][8];

    public SwingChessView() {
        initFrame();
        setupScreen();
        show();
    }



    private void initFrame() {
        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
    }

    
    @Override
    public void setController(ChessController controller) {
        this.controller = controller;
    }


    @Override
    public void setupScreen() {
        
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
        buttonStyling(humanVsAi);
        buttonStyling(humanVsHuman);



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

        buttonStyling(white);
        buttonStyling(black);

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

        buttonStyling(easy);
        buttonStyling(normal);
        buttonStyling(hard);

        setupPanel.add(modePanelContainer, "ModeSelection");
        setupPanel.add(colorPanelContainer, "Color selection");
        setupPanel.add(difficultyContainer, "Difficulty selection");

        frame.add(setupPanel, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }

    private void buttonStyling(JButton button){
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void gameScreen() {
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));

        frame.add(boardPanel);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new JButton();
                squares[row][col].setPreferredSize(new Dimension(60, 60));
                
                if ((row + col) % 2 == 0) {
                    squares[row][col].setBackground(Color.WHITE);
                } else {
                    squares[row][col].setBackground(new Color(100, 150, 80));
                }

                squares[row][col].setOpaque(true);
                squares[row][col].setBorderPainted(false);
                //Action listener
                boardPanel.add(squares[row][col]);
            }
        }
        setupPanel.add(boardPanel,"Chessboard");
        cardLayout.show(setupPanel, "Chessboard");
    }


    @Override
    public void displayBoard(Piece[][] board) {
        if (selectedColor.equals("BLACK")) {
            this.displayBlack(board);
        }
        else{
            this.displayWhite(board);
        }
    }

    private void displayBlack(Piece[][] board){

    }

    private void displayWhite(Piece[][] board){
        for(int row = 0; row< 8; row++){
            for(int column = 0; column< 8; column++){

                if(board[row][column] instanceof Pawn){
                    if(board[row][column].getColour() == Colour.WHITE){
                        squares[row][column].setText("♟");
                    }
                    else
                        squares[row][column].setText("♙");
                }
                else if(board[row][column] instanceof Bishop){

                    if(board[row][column].getColour() == Colour.WHITE){
                        squares[row][column].setText("♝");
                    }
                    else
                        squares[row][column].setText("♗");

                }
                else if(board[row][column] instanceof King){

                    if(board[row][column].getColour() == Colour.WHITE){
                        squares[row][column].setText("♚");
                    }
                    else
                        squares[row][column].setText("♔");
                }
                else if(board[row][column] instanceof Knight){

                    if(board[row][column].getColour() == Colour.WHITE){
                        squares[row][column].setText("♞");
                    }
                    else
                        squares[row][column].setText("♘");

                }
                else if(board[row][column] instanceof Queen){

                    if(board[row][column].getColour() == Colour.WHITE){
                        squares[row][column].setText("♛");
                    }
                    else
                        squares[row][column].setText("♕");
                }
                else if(board[row][column] instanceof Rook){

                    if(board[row][column].getColour() == Colour.WHITE){
                        squares[row][column].setText("♜");
                    }
                    else
                        squares[row][column].setText("♖");

                }
                Font currentFont = squares[row][column].getFont();
                squares[row][column].setFont(currentFont.deriveFont(48f));
            }   
        }
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