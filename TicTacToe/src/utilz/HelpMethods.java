package utilz;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static utilz.Constants.ButtonConstants.*;

public class HelpMethods {
    public static BufferedImage GetImageFrom(String path) {
        try {
            File image = new File(path);
            return ImageIO.read(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage[][] GetButtonSprites(String path) {
        BufferedImage[][] array;
        File image;
        try {
            image = new File(path);
            array = new BufferedImage[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++)
                    array[i][j] = ImageIO.read(image).getSubimage(j * SPRITE_SIZE, i * SPRITE_SIZE, SPRITE_SIZE,SPRITE_SIZE);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return array;
    }
}
