import processing.core.PApplet;
import processing.core.PShape;

import java.util.ArrayList;

public class ShapeConstructor {
    private ArrayList<int[]> vertex = new ArrayList<>();
    private PApplet context;
    private PShape shape;

    public ShapeConstructor(PApplet context) {
        this.context = context;
        this.shape = context.createShape();
    }

    public ShapeConstructor addVertex(int x, int y) {
        vertex.add(new int[]{x, y});
        return this;
    }

    public ShapeConstructor line(int x1, int y1, int x2, int y2) {
        context.pushMatrix();
        context.line(x1, y1, x2, y2);
        context.popMatrix();
        return this;
    }

    public PShape create() {
        shape.beginShape();

        for (int[] vertex : vertex) {
            shape.vertex(vertex[0], vertex[1]);
        }

        shape.endShape();

        return shape;
    }
}