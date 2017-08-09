import processing.core.PConstants;

public class ConveyorBelt extends Shape {

    private static final int size = 500;
    private static final int footHeight = 100;

    private float[] sensorPositions;
    private boolean[] sensorsActive;

    private MovingObject object;
    private int canMove = 1;



    public ConveyorBelt (String name, Applet context) {
        super.context = context;
        setName(name);

        addBase();
        addFeet(0.1, 0.9);
        addMast();
        addDropDown();
        addSensors(0.25, 0.5, 0.7);
        addMovingObject();
        reposition();
    }

    private void addMovingObject() {
        object = new MovingObject("moving object", context, size);
    }

    private void addBase() {
        Shape base = context.createShape(PConstants.LINE, 0, 0, size, 0);
        base.setStroke(Color.WHITE);
        addChild("base", base);
    }

    private void addFeet(double... feetPosition) {
        for (double footPosition : feetPosition) {
            float position = (float) footPosition * size;
            Shape feet = context.createShape(PConstants.LINE, position, footHeight, position, 0);
            feet.setStroke(Color.WHITE);
            addChild("feet " + footPosition, feet);
        }
    }

    private void addMast() {
        float mastPosition = 0.5f * size;
        float mastHeight = 200;
        Shape mast = context.createShape(PConstants.LINE, mastPosition, footHeight, mastPosition, -mastHeight);
        mast.setStroke(Color.WHITE);
        addChild("mast", mast);
    }

    private void addSensors(double... sensorPositions) {
        this.sensorPositions = new float[sensorPositions.length];
        this.sensorsActive = new boolean[sensorPositions.length];

        for (int i = 0; i < sensorPositions.length; i++) {
            this.sensorPositions[i] = (float) sensorPositions[i] * size;
            this.sensorsActive[i] = false;

            Shape sensor = context.createShape(PConstants.ELLIPSE, this.sensorPositions[i], 0, 5, 5);
            sensor.setStroke(Color.RED);
            addChild("sensor " + i, sensor);
        }
    }

    private void addDropDown() {
        float dropDownWidth = 50;
        float dropDownHeight = 80;
        float dropDownStartPosition = size/2 - dropDownWidth;
        Shape dropDown = context.createShape(PConstants.RECT, dropDownStartPosition, -80, dropDownWidth, -dropDownHeight);
        addChild("dropDown", dropDown);
    }

    private void reposition() {
        translate(-size/2, 0);
    }

    public void moveObject(double dx) {
        object.move(dx * canMove);
    }

    @Override
    public float getWidth() {
        return size;
    }

    public void display() {
        context.shape(this);
        context.shape(object);
    }

    public void turnSensorsOn() {
        new Thread(() -> {
            for (int i = 0; i < sensorPositions.length; i++) {
                Shape sensor = getChild("sensor " + i);

                if ((sensorPositions[i] - object.getPosX()) > 0 && (sensorPositions[i] - object.getPosX()) < object.getSizeX()) {
                    sensorsActive[i] = true;
                    sensor.setStroke(Color.YELLOW);
                } else {
                    sensorsActive[i] = false;
                    sensor.setStroke(Color.RED);
                }
            }
        }).start();
    }

    public void displaySensorsTable() {
        for (int i = 0; i < sensorsActive.length; i ++) {
            context.writeInPage("S" + i, 30 + 40*i, 10);
            context.writeInPage(sensorsActive[i] ? 1 : 0, 30 + 40*i, 40);
        }
    }

    public void displayControllers() {
        int spacing = 60;
        Button forward = new Button(context, 200 + spacing*0, context.height - 70, 40, 40, () -> object.setDirection(MovingObject.Direction.POSITIVE));
        Button backward = new Button(context, 200 + spacing*1, context.height - 70, 40, 40, () -> object.setDirection(MovingObject.Direction.NEGATIVE));
        Button stop = new Button(context, 200 + spacing*2, context.height - 70, 40, 40, () -> object.setDirection(MovingObject.Direction.STOP));

        forward.setText(">>");
        backward.setText("<<");
        stop.setText("||");

        context.addButton(forward);
        context.addButton(backward);
        context.addButton(stop);

    }

}
