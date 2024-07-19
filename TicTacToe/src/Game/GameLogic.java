package Game;
import DAL.DataAccessLayer;
import GUI.Buttons.ImageButton;
import GUI.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLogic implements ActionListener {
    private ImageButton[][] board;
    private DataAccessLayer dataAccessLayer;
    private String playerSymbol;
    private String winner;
    private GamePanel gamePanel;

    public GameLogic(ImageButton[][] board, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.board = board;
        this.dataAccessLayer = gamePanel.getDataAccessLayer();
        this.playerSymbol = dataAccessLayer.getPlayerSymbol();
        Thread gOverListenerThread = new Thread(new GameoverListener());
        gOverListenerThread.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (dataAccessLayer.isWon())
            return;
        JButton clickedButton = (JButton) e.getSource();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                
                if (board[i][j] == clickedButton) {
                    System.out.println("Button: y: "+ i + " | x: "+ j);
                    if (dataAccessLayer.getCurrentPlayer().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "The game has not started yet.");
                        return;
                    } else if (!dataAccessLayer.getCurrentPlayer().equals(playerSymbol)) {
                        JOptionPane.showMessageDialog(null, "It's not your turn!.");
                        return;
                    } else {
                        if (dataAccessLayer.getSymbolAt(j, i).trim().isEmpty()) {
                            dataAccessLayer.setSymbolAt(j, i, playerSymbol);
                            gamePanel.updateLabel();
                            dataAccessLayer.updateQueue();
                        }
                    }
                }
            }
        }
    }
    
    public void setWinner(String winner) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j].getText().equals(winner)) {
                    board[i][j].setForeground(Color.GREEN);
                }
            }
        }
    }
    class GameoverListener implements Runnable {
        
        @Override
        public void run() {
            while (!dataAccessLayer.isWon()) {
                dataAccessLayer.checkWinner();
            }
            setWinner(dataAccessLayer.getWinner());
        }
    }
}
