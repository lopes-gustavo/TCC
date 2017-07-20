import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class Sketch extends MyPApplet {
    private Shape base, shoulder, upArm, loArm, end, table;

    private float rotX = radians(-20);
    private float rotY = radians(60);

    public float alpha;
    public float beta;
    public int gamma = 1;
    public float posX = 1;
    public float posY = 50;
    public float posZ = 50;
    public double scale = -2.5;

    private final float[] xSphere = new float[99];
    private final float[] ySphere = new float[99];
    private final float[] zSphere = new float[99];

    private Shape shoulderShape;

    private Functions functions = new Functions(this);

    @Override
    public void setup() {
        size(1200, 800, P3D);

        base = loadShape("base.obj");
        shoulder = loadShape("shoulder.obj");
        upArm = loadShape("upArm.obj");
        loArm = loadShape("loArm.obj");
        end = loadShape("end.obj");
        table = loadShape("table.obj");

        //table.scale(10);

        shoulder.disableStyle();
        upArm.disableStyle();
        loArm.disableStyle();
        table.disableStyle();

    }

    @Override
    public void settings() {
        size(1200, 800, P3D);
    }

    @Override
    public void draw() {
        //functions.writePos();

        functions.updateGamma();

        background(Color.BACKGROUND);
        smooth();
        lights();
        directionalLight(51, 102, 126, -1, 0, 0);


        for (int i = 0; i < xSphere.length - 1; i++) {
            xSphere[i] = xSphere[i + 1];
            ySphere[i] = ySphere[i + 1];
            zSphere[i] = zSphere[i + 1];
        }

        xSphere[xSphere.length - 1] = posX;
        ySphere[ySphere.length - 1] = posY;
        zSphere[zSphere.length - 1] = posZ;


        noStroke();

        translate(width / 2, height / 2);
        rotateX(rotX);
        rotateY(-rotY);
        scale(scale);

        for (int i = 0; i < xSphere.length; i++) {
            pushMatrix();
            translate(-ySphere[i], -zSphere[i] - 11, -xSphere[i]);
            fill(0xFFD003FF, 25);
            sphere((float) (i) / 20);
            popMatrix();
        }

        displayGrid();

        Shape tableShape = new ShapeConstructor(table)
                .position(0,-40,0)
                .rotateDegrees(0,90,0)
                .color(Color.RED)
                .create();

        Shape baseShape = new ShapeConstructor(base)
                .position(0,-25,0)
                .addOwnHeight()
                .color(Color.YELLOW)
                .create();

        shoulderShape = new ShapeConstructor(shoulder)
                .position(0,4,0)
                .color(Color.YELLOW)
                .create();

        shoulder.rotateYDegrees(gamma);


/*
        translate(0, 25, 0);
        rotateY(PI);
        rotateX(alpha);
        shape(upArm);

        translate(0, 0, 50);
        rotateY(PI);
        rotateX(beta);
        shape(loArm);

        translate(0, 0, -50);
        rotateY(PI);
        shape(end);
*/



    }

    private void displayGrid() {
        int size = width;
        int gridSize = width/10;
        int markerSize = 10;

        stroke(Color.RED);
        textSize(32);
        fill(Color.WHITE);

        line(-size, 0, 0, size, 0, 0);
        text("x", 100, 5, 0);

        line(0, -size, 0, 0, size, 0);
        text("y", 0, 100, 0);

        line(0, 0, -size, 0, 0, size);
        text("z", 0, 0, 100);

        for (int pos = -size; pos <= size; pos+=gridSize) {
            line(pos, -markerSize, 0, pos, markerSize, 0);
            text(pos, pos, markerSize + 5, 0);

            line(0, pos, -markerSize, 0, pos, markerSize);
            text(pos, 0, pos, markerSize + 5);

            line(-markerSize, 0, pos, markerSize, 0, pos);
            text(pos,markerSize + 5, 0, pos);
        }

    }

    @Override
    public void mouseDragged() {
        rotY -= (mouseX - pmouseX) * 0.01;
        rotX -= (mouseY - pmouseY) * 0.01;
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        scale -= 0.1*event.getCount();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if(event.getKey() == 'p') {
            gamma = 0;
            println(shoulderShape.getAngle());
        } else if (event.getKey() == 'o') {
            gamma = 1;
        }
    }
}
