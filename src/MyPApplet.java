import processing.core.PApplet;
import processing.core.PShape;

public abstract class MyPApplet extends PApplet{

    /**
     * Add some debugging options on the method
     * @see PApplet#loadShape
     */
    public Shape loadShape(String filename) {
        PShape pShape = super.loadShape(filename);
        Shape shape = new Shape(pShape);

        println(filename + ".getWidth", pShape.getWidth());
        println(filename + ".getHeight", pShape.getHeight());

        return shape;
    }

    /**
     * Add support for double input
     * @see PApplet#scale
     */
    public void scale(double s) {
        scale((float) s);
    }



    public class ShapeConstructor {
        private int x = 0;
        private int y = 0;
        private int z = 0;
        private float rotX = 0;
        private float rotY = 0;
        private float rotZ = 0;
        private int color = 0;

        private final Shape shape;

        public ShapeConstructor(Shape shape) {
            this.shape = shape;
        }

        public ShapeConstructor addOwnHeight() {
            y += shape.getShape().getHeight();

            return this;
        }

        public ShapeConstructor position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;

            return this;
        }

        public ShapeConstructor rotateRadians(float rotX, float rotY, float rotZ) {
            this.rotX = rotX;
            this.rotY = rotY;
            this.rotZ = rotZ;

            return this;
        }

        public ShapeConstructor rotateDegrees(int rotX, int rotY, int rotZ) {
            this.rotX = radians(rotX);
            this.rotY = radians(rotY);
            this.rotZ = radians(rotZ);

            return this;
        }

        public ShapeConstructor color(int color) {
            this.color = color;

            return this;
        }

        public Shape create() {
            int currentColor = getGraphics().fillColor;

            pushMatrix();

            translate(x, y, z);
            rotateX(rotX);
            rotateY(rotY);
            rotateZ(rotZ);
            fill(color);

            shape(shape.getShape());

            popMatrix();

            fill(currentColor);

            return shape;
        }

    }

}
