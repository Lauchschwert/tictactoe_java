package GUI;

import DAL.DataAccessLayer;
import GUI.Buttons.ImageButton;
import Game.GameLogic;
import utilz.HelpMethods;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.Color.*;

public class GamePanel extends JPanel {
    private ImageButton[][] board;
    private DataAccessLayer dataAccessLayer;
    private JLabel currentPlayerLabel;
private JPanel btnHolder;
    public GamePanel(DataAccessLayer dataAccessLayer) {
        super();
        this.dataAccessLayer = dataAccessLayer;
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(15, 15, 15, 15));

        initClasses();
        startBoardRefreshThread();
    }

    private void startBoardRefreshThread() {
        Thread bRT = new Thread(new BoardRefreshThread());
        bRT.start();
    }

    private void initClasses() {
        btnHolder = new JPanel(new GridLayout(3, 3, 15, 15));
        btnHolder.setBorder(new EmptyBorder(10,0,0,0));
        btnHolder.setOpaque(false);
        
        board = new ImageButton[3][3];
        
        currentPlayerLabel = new JLabel();
        currentPlayerLabel.setHorizontalAlignment(JLabel.CENTER);
        currentPlayerLabel.setForeground(WHITE);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        add(currentPlayerLabel, BorderLayout.NORTH);
        add(btnHolder, BorderLayout.CENTER);
        
        initButtons();
    }

    private void initButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = new ImageButton();
                board[i][j].setPreferredSize(new Dimension(100, 100));
                board[i][j].addActionListener(new GameLogic(board, this));
                btnHolder.add(board[i][j]);
            }
        }
    }

    private void updateBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j].setPlayerState(dataAccessLayer.getSymbolAt(j,i));
                board[i][j].update();
            }
        }
    }

    public DataAccessLayer getDataAccessLayer() {
        return dataAccessLayer;
    }
    public void updateLabel() {
        currentPlayerLabel.setText("Current player: " + dataAccessLayer.getCurrentPlayer());
    }
    class BoardRefreshThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                updateBoard();
            }
        }

 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Ensure the panel itself is painted first

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(HelpMethods.GetImageFrom("src/res/background.png"), 0, 0, getWidth(), getHeight(), null);
    }
}
