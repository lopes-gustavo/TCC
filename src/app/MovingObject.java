package app;

import processing.core.PApplet;
import processing.core.PShape;

public class MovingObject extends PShape {
    private PApplet context;

    private final float maxX;
    private final float minX;
    private final float beltWidth;
    private float posX = 0;
    private int posY = 0;
    private int sizeX = 40;
    private int sizeY = 30;
    private int direction = 1;

    public MovingObject(String name, PApplet context, float beltWidth) {
        this.context = context;
        this.beltWidth = beltWidth;
        this.minX = -beltWidth/2;
        this.maxX = beltWidth/2 - sizeX;

        setName(name);
        create();
    }

    private void create() {
        PShape obj = context.createShape(RECT, minX, -sizeY, sizeX, sizeY);
        obj.setFill(Color.RED);
        addChild(obj);

        posX = (int) minX;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void move(double speed) {
        double move = direction * speed;
        posX += move;

        if (posX <= minX){
            direction = Direction.STOP;
            move += 1;
            posX += 1;
        } else if (posX >= maxX) {
            direction = Direction.STOP;
            move -= 1;
            posX -= 1;
        }

        context.pushMatrix();
        translate((float) move, 0);
        context.popMatrix();
    }

    public float getPosX() {
        return posX + (int) beltWidth/2;
    }

    public int getSizeX() {
        return sizeX;
    }

    interface Direction {
        int POSITIVE = 1;
        int NEGATIVE = -1;
        int STOP = 0;
    }
}

