package GUI.WaitingScreen;


import DAL.DataAccessLayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WaitingPanel extends JPanel implements ActionListener {
    JLabel waitingLabel;
    private DataAccessLayer dataAccessLayer;
    JButton readyButton;
    public WaitingPanel(DataAccessLayer dataAccessLayer) {
        this.dataAccessLayer = dataAccessLayer;
        setBackground(Color.black);
        setBorder(new EmptyBorder(10, 20, 20, 20));
        setLayout(new GridLayout(2, 1));
        
        initClasses();

    }

    private void initClasses() {
        waitingLabel = new JLabel("<html><center>Waiting Players: " + dataAccessLayer.getPlayerCount() + "/2<br>Waiting for Players to Ready Up: " + dataAccessLayer.getReadyPlayers() + "/2<br>Your Symbol: " + dataAccessLayer.getPlayerSymbol() +"<center></html>");
        waitingLabel.setForeground(Color.white);
        
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        
        readyButton = new JButton("Ready Up!");
        readyButton.setPreferredSize(new Dimension(120,60));
        readyButton.setFocusPainted(false);
        readyButton.addActionListener(this);
        btnPanel.add(readyButton);
        add(waitingLabel);
        add(btnPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == readyButton) {
            if (!dataAccessLayer.isReady()) {
                dataAccessLayer.setReady(true);
                readyButton.setText("Cancel");
            } else if(dataAccessLayer.isReady()){
                dataAccessLayer.setReady(false);
                readyButton.setText("Ready Up!");
            }
        }
    }
    
    public void update() {
        waitingLabel.setText("<html><center>Waiting Players: " + dataAccessLayer.getPlayerCount() + "/2<br>Waiting for Players to Ready Up: " + dataAccessLayer.getReadyPlayers() + "/2<br>Your Symbol: " + dataAccessLayer.getPlayerSymbol() +"<center></html>");
    }
}
