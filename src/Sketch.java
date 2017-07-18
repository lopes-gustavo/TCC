import processing.core.PApplet;
import processing.core.PShape;

public class Sketch extends PApplet {
    private PShape base, shoulder, upArm, loArm, end, table;

    private float rotX, rotY;
    public float posX = 1;
    public float posY = 50;
    public float posZ = 50;
    public float alpha;
    public float beta;
    public float gamma;

    private final float[] Xsphere = new float[99];
    private final float[] Ysphere = new float[99];
    private final float[] Zsphere = new float[99];

    private Functions functions = new Functions(this);

    @Override
    public void setup() {
        size(1200, 800, P3D);

        base = loadShape("base.obj");
        shoulder = loadShape("shoulder.obj");
        upArm = loadShape("upArm.obj");
        loArm = loadShape("loArm.obj");
        end = loadShape("end.obj");
        table = loadShape("Part1.obj");

        table.scale(4);

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
        functions.writePos();

        background(Color.BACKGROUND);
        smooth();
        lights();
        directionalLight(51, 102, 126, -1, 0, 0);

        for (int i = 0; i < Xsphere.length - 1; i++) {
            Xsphere[i] = Xsphere[i + 1];
            Ysphere[i] = Ysphere[i + 1];
            Zsphere[i] = Zsphere[i + 1];
        }

        Xsphere[Xsphere.length - 1] = posX;
        Ysphere[Ysphere.length - 1] = posY;
        Zsphere[Zsphere.length - 1] = posZ;

        noStroke();

        translate(width / 2, height / 2);
        rotateX(rotX);
        rotateY(-rotY);
        scale(-4);

        for (int i = 0; i < Xsphere.length; i++) {
            pushMatrix();
            translate(-Ysphere[i], -Zsphere[i] - 11, -Xsphere[i]);
            fill(0xFFD003FF, 25);
            sphere((float) (i) / 20);
            popMatrix();
        }

        fill(0xFFFF0000);
        //translate(0, 100, 0);
        shape(table);

        fill(0xFFFFE308);
        translate(0, -40, 0);
        shape(base);

        translate(0, 4, 0);
        rotateY(gamma);
        shape(shoulder);

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

    }


    @Override
    public void mouseDragged() {
        rotY -= (mouseX - pmouseX) * 0.01;
        rotX -= (mouseY - pmouseY) * 0.01;
    }

    @Override
    public PShape loadShape(String filename) {
        PShape pShape = super.loadShape(filename);

        println(filename + ".getWidth", pShape.getWidth());
        println(filename + ".getHeight", pShape.getHeight());

        return pShape;
    }
}
