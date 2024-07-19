package GUI.WaitingScreen;

import DAL.DataAccessLayer;
import GUI.GameWindow;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

public class WaitingFrame extends JFrame implements WindowFocusListener, WindowListener {
    private DataAccessLayer dataAccessLayer;
    private WaitingPanel waitingPanel;

    public WaitingFrame() {
        dataAccessLayer = new DataAccessLayer();

        addWindowFocusListener(this);
        addWindowListener(this);

        this.setTitle("Waiting for Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        waitingPanel = new WaitingPanel(dataAccessLayer);
        add(waitingPanel);
        
        Thread t = new Thread(new GameListener(dataAccessLayer));
        t.start();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        requestFocus();
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

    class GameListener implements Runnable {
        private DataAccessLayer dataAccessLayer;

        public GameListener(DataAccessLayer dataAccessLayer) {
            this.dataAccessLayer = dataAccessLayer;
        }

        @Override
        public void run() {
            while (true) {
                waitingPanel.update();
                if (!dataAccessLayer.lookingForGame()) {
                    SwingUtilities.invokeLater(() -> {
                        dispose();
                        new GameWindow(dataAccessLayer);
                    });
                    break;
                }
            }
        }
    }
}
