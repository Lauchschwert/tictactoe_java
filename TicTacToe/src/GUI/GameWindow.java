package GUI;

import javax.swing.*;

import DAL.DataAccessLayer;


import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class GameWindow extends JFrame implements WindowListener {
    private DataAccessLayer dataAccessLayer;

    public GameWindow(DataAccessLayer dataAccessLayer) {
        this.dataAccessLayer = dataAccessLayer;
        this.addWindowListener(this);
        this.setTitle("TicTacToe");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel(dataAccessLayer);
        add(gamePanel);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        dataAccessLayer.disconnect();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
