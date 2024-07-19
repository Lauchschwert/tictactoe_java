package GUI.Buttons;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static utilz.ButtonStates.*;
import static utilz.Constants.ButtonConstants.*;

import java.awt.image.BufferedImage;

import utilz.HelpMethods;

public class ImageButton extends JButton {
    private BufferedImage[][] sprites;
    private BufferedImage sprite;
    private int buttonState = IDLE;
    private int playerState;

    public ImageButton() {
        super();
        setBorder(new MatteBorder(2, 2, 2, 2, Color.black));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        initImages();

        // Add a mouse listener to handle hover and click events
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonState = HOVERED;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonState = IDLE;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                buttonState = PRESSED;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (buttonState != HOVERED) {
                    buttonState = IDLE;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(sprite, 0, 0, SPRITE_SIZE * SCALE, SPRITE_SIZE * SCALE, null);
    }

    private void initImages() {
        try {
            sprites = HelpMethods.GetButtonSprites("src/res/mc_btn_spritesheet.png");
        } catch (Exception e) {
            e.printStackTrace();  // Handle exceptions during image loading
        }
    }

    private void setSprite(int state, int player) {
        sprite = sprites[state][player];
    }

    public void update() {
        switch (buttonState) {
            case HOVERED:
                if (playerState == X_BTN) {
                    setSprite(HOVERED, X_BTN);
                } else if (playerState == O_BTN) {
                    setSprite(HOVERED, O_BTN);
                } else {
                    setSprite(HOVERED, DEFAULT);
                }
                break;

            case PRESSED:
                if (playerState == X_BTN) {
                    setSprite(PRESSED, X_BTN);
                } else if (playerState == O_BTN) {
                    setSprite(PRESSED, O_BTN);
                } else {
                    setSprite(PRESSED, DEFAULT);
                }
                break;

            case IDLE:
                if (playerState == X_BTN) {
                    setSprite(IDLE, X_BTN);
                } else if (playerState == O_BTN) {
                    setSprite(IDLE, O_BTN);
                } else {
                    setSprite(IDLE, DEFAULT);
                }
                break;

            default:
                break;
        }
        repaint();
    }

    public void setPlayerState(String player) {
        switch (player) {
            case " ":{
                playerState = DEFAULT;
                break;
            }
            case "X":{
                playerState = X_BTN;
                
                break;
            }
            case "O":{
                playerState = O_BTN;
                break;
            }
        }
        repaint();
    }
}
