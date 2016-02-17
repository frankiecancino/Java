package fcb6faudioviz;

import static java.lang.Integer.min;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author frankiecancino
 */
public class Fcb6fVisual implements Visualizer {
    
    private String name = "Fcb6f Visual";
    
    private Integer numBands;
    private AnchorPane vizPane;
    
    private String vizPaneInitialStyle = "";
    
    private Double bandHeightPercentage = 1.5;
    private Double minRectangleHeight = 15.0;
    private Double rotatePhaseMultiplier = 300.0;
    
    private Double width = 0.0;
    private Double height = 0.0;
    
    private Double bandWidth = 0.0;
    private Double bandHeight = 0.0;
    private Double halfBandHeight = 0.0;
    
    private Double startHue = 260.0;
    private Integer j = 1;
    
    private Rectangle[] rectangles;
    
    public Fcb6fVisual() {
    }

    @Override
    public String getName() {
        return name;
    }
    
    
    @Override
    public void start(Integer numBands, AnchorPane vizPane) {        
        end();
        
        vizPaneInitialStyle = vizPane.getStyle();
        
        this.numBands = numBands;
        this.vizPane = vizPane;
        
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        
        Rectangle clip = new Rectangle(width, height);
        clip.setLayoutY(0);
        clip.setLayoutX(0);
        vizPane.setClip(clip);
        
        bandWidth = width / numBands;
        bandHeight = height * bandHeightPercentage;
        halfBandHeight = bandHeight / 2;
        rectangles = new Rectangle[numBands];
        
        for (int i = 0; i < numBands; i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setX(bandWidth * i); 
            rectangle.setWidth(bandWidth);
            rectangle.setHeight(bandWidth);
            rectangle.setY((height) - (rectangle.getHeight()));
            rectangle.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(rectangle);
            rectangles[i] = rectangle;
        }
    }
    
    //Nothing to do here
    @Override
    public void end() {
        if (rectangles != null) {
            for (Rectangle rectangle : rectangles) {
                vizPane.getChildren().remove(rectangle);
            }
            rectangles = null;
            vizPane.setClip(null);
            vizPane.setStyle(vizPaneInitialStyle);
        }        
    }

    
    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if (rectangles == null) {
            return;
        }
        
        Integer num = min(rectangles.length, magnitudes.length);
        
        for (int i = 0; i < num; i++) {
            rectangles[i].setHeight(((60.0 + magnitudes[i])/15.0) * halfBandHeight + minRectangleHeight);
            rectangles[i].setY((height) - (rectangles[i].getHeight()));
            rectangles[i].setFill(Color.hsb(startHue - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
        }
        
        //Changes background color
        Double hue = ((60.0 + magnitudes[0])/60.0) * 360;
        vizPane.setStyle("-fx-background-color: hsb(" + hue + ", 20%, 100%)" );
    }
}