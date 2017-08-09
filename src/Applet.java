import processing.core.PApplet;

import java.util.ArrayList;

public class Applet extends PApplet {
    private int x;
    private int y;

    protected ArrayList<Button> buttons = new ArrayList<>();

    @Override
    public Shape createShape(int kind, float... p) {
        return new Shape(this, this.g, kind, p);
    }

    @Override
    public void translate(float x, float y) {
        super.translate(x, y);

        this.x += x;
        this.y += y;
    }

    @Override
    public void draw() {
        x = 0;
        y = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void showGrid() {
        int inc = 50;

        pushMatrix();

        textSize(15);
        textAlign(CENTER, BOTTOM);

        fill(Color.WHITE);
        stroke(Color.GRID_COLOR);

        for (int i = 0; i <= width; i += inc) {
            text(i-width/2, i, 50);
            text(i, i, 25);
            line(i, 0, i, height);
        }

        popMatrix();
    }

    public void showBlackDebugBox() {
        pushMatrix();
        noStroke();
        fill(Color.BLACK);
        rect(0, height, width, -100);
        popMatrix();
    }

    public void center() {
        translate(width / 2, height / 2);
    }

    public void writeInPage(Object text, float x, float y) {
        pushMatrix();

        textSize(15);
        textAlign(CENTER, TOP);

        stroke(Color.RED);
        fill(Color.BRIGHT_GREEN);

        text(String.valueOf(text), -width / 2 + x, height / 2 - 100 + y);

        popMatrix();
    }

    public void addButton(Button button) {
        buttons.add(button);
    }

    public void displayButtons() {
        for (Button b : buttons) {
            b.display();
        }
    }

    @Override
    public void mousePressed() {
        for (Button b : buttons) {
            if (b.isMouseInside()) {
                b.callListener();
            }
        }
    }

}
