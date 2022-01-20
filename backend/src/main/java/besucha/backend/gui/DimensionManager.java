package besucha.backend.gui;

import java.awt.*;

public class DimensionManager {
    private int maxX, maxY;

    public DimensionManager(){
        this(
            (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 
            (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()  
        );
    }

    public DimensionManager(Dimension d){
        this((int)d.getWidth(), (int)d.getHeight());
    }

    public DimensionManager(int maxX, int maxY){
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Sets the dimentions of the provided window to 
     * @param w
     * @param xRatio
     * @param yRatio
     */
    public void setDimensions(Window w, double xRatio, double yRatio) throws IllegalArgumentException {
        if ((xRatio < 0 || xRatio > 1) && (yRatio < 0 || yRatio > 1)){
            throw new IllegalArgumentException("Both ratio's invalid; should be between 0 and 1");
        } else if (xRatio < 0 || xRatio > 1){
            throw new IllegalArgumentException("width ratio's invalid; should be between 0 and 1");
        } else if (yRatio < 0 || yRatio > 1){
            throw new IllegalArgumentException("height ratio's invalid; should be between 0 and 1");
        } else {
            w.setSize((int)(maxX * xRatio), (int)(maxY * yRatio));
        }
    }

    public void setSquareDimensions(Window w, double heightRatio){
        this.setDimensions(w, maxY * heightRatio / maxX, heightRatio);
    }

    public void setQuarterDimensions(Window w){
        this.setDimensions(w, 0.25, 0.25);
    }

    public double getX(double widthRatio){
        return maxX * widthRatio;
    }

    public double getY(double heightRatio){
        return maxY * heightRatio;
    }

    public Dimension getAdjustedDimension(double widthRatio, double heightRatio){
        return new Dimension((int)this.getX(widthRatio), (int)this.getY(heightRatio));
    }
}
