import processing.core.PGraphics;
import processing.core.PShape;

import java.util.Arrays;

public class Shape extends PShape {
    protected Applet context;

    public Shape() {
        super();
    }

    public Shape(Applet context, PGraphics g, int kind, float[] p) {
        super(g, kind, p);
        this.context = context;
    }

    public void addChild(String name, PShape shape) {
        shape.setName(name);
        addChild(shape);
    }

    @Override
    public void setName(String name) {
        super.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public float[] getAbsoluteParams() {
        float[] out = this.getParams();
        out[0] += context.getX();
        out[1] += context.getY();

        return out;
    }

    @Override
    public Shape[] getChildren() {
        // TODO: Analyze to see if (children.length-1) will work
        return Arrays.copyOf(children, children.length-1, Shape[].class);
    }

    @Override
    public Shape getChild(String target) {
        return (Shape) super.getChild(target);
    }


}
