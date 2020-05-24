package org.Logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The Flashing signal thread class.
 */
public class FlashingSignalThread extends Thread {

    /**
     * The ImageView of light.
     */
    ImageView light;
    /**
     * The Image with enable light.
     */
    Image lightEnable;
    private boolean flag = true;

    /**
     * Instantiates a new Flashing signal thread.
     *
     * @param light       the ImagView of light
     * @param lightEnable the Image of light when is enable
     */
    public FlashingSignalThread(ImageView light, Image lightEnable) {
        this.lightEnable = lightEnable;
        this.light = light;
    }

    /**
     * Stop flashing.
     */
    public void stopFlashing() {
        this.flag = false;
    }

    public void run() {
        this.light.setImage(lightEnable);
        while(flag) {
            this.light.setOpacity(0.88);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) { }
            this.light.setOpacity(0.1);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
    }
}
