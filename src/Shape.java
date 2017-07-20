import processing.core.PApplet;
import processing.core.PShape;

public class Shape extends PShape{
    private float angle = 0;
    private PShape shape;

    Shape(PShape shape) {
        this.shape = shape;
    }


    public void rotateY(float angle) {
        shape.rotateY(angle);

        this.angle += angle;
        if (this.angle >= PI) {
            this.angle = -PI;
        }
    }

    public void rotateYDegrees(int angle) {
        rotateY(PApplet.radians(angle));
    }

    public PShape getShape() {
        return shape;
    }

    public float getAngle() {
        return PApplet.degrees(angle);
    }
}
