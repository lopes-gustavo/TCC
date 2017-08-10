import processing.core.PConstants;

import java.util.ArrayList;

public class ConveyorBelt extends Shape {

    private static final int size = 500;
    private static final int footHeight = 100;

    private ArrayList<Sensor> sensors = new ArrayList<>();

    private MovingObject object;
    private int canMove = 1;
    private boolean dropSelected = false;

    public ConveyorBelt (String name, Applet context) {
        super.context = context;
        setName(name);

        addBase();
        addFeet(0.1, 0.9);
        addMast();
        addDropDown();
        addSensors(0.25, 0.5, 0.7);
        addDropDownSensor();
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
        for (double sensorPosition : sensorPositions) {
            Sensor sensor = new Sensor(context.createShape(PConstants.ELLIPSE, (float) sensorPosition * size, 0, 5, 5), Sensor.VERTICAL);
            this.sensors.add(sensor);
            addChild("sensor " + String.valueOf(sensorPosition), sensor);
        }
    }

    private void addDropDownSensor() {
        Sensor sensor = new Sensor(context.createShape(PConstants.ELLIPSE, size/2, -70, 5, 5), Sensor.HORIZONTAL);
        this.sensors.add(sensor);
        addChild("sensor " + size/2, sensor);
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
            for (Sensor sensor : sensors) {

                boolean isActive = false;
                switch (sensor.getType()) {
                    case Sensor.HORIZONTAL:
                        isActive = dropSelected;
                        break;
                    case Sensor.VERTICAL: //TODO
                        isActive = (sensor.getPosition() - object.getPosX()) > 0 && (sensor.getPosition() - object.getPosX()) < object.getSizeX();
                        break;
                }

                if (isActive) {
                    sensor.setActive(true);
                    sensor.setStroke(Color.YELLOW);
                } else {
                    sensor.setActive(false);
                    sensor.setStroke(Color.RED);
                }
            }
        }).start();
    }

    public void displaySensorsTable() {
        for (int i = 0; i < sensors.size(); i ++) {
            context.writeInPage("S" + i, 30 + 40*i, 10);
            context.writeInPage(sensors.get(i).isActive() ? 1 : 0, 30 + 40*i, 40);
        }
    }

    public void displayControllers() {
        int spacing = 60;
        Button forw = new Button(context, 200 + spacing*0, context.height - 70, 40, 40, button -> object.setDirection(MovingObject.Direction.POSITIVE));
        Button back = new Button(context, 200 + spacing*1, context.height - 70, 40, 40, button -> object.setDirection(MovingObject.Direction.NEGATIVE));
        Button stop = new Button(context, 200 + spacing*2, context.height - 70, 40, 40, button -> object.setDirection(MovingObject.Direction.STOP));
        Button drop = new Button(context, 200 + spacing*3, context.height - 70, 40, 40, button -> dropSelected = button.isSelected());

        forw.setText(">>");
        back.setText("<<");
        stop.setText("||");
        drop.setText("\\/");

        context.addButton(forw);
        context.addButton(back);
        context.addButton(stop);
        context.addButton(drop);

    }

    public String getSensorsActive() {
        StringBuilder out = new StringBuilder();
        for (Sensor sensor : sensors) {
            out.append(sensor.isActive() ? 1 : 0);
        }
        return out.toString();
    }

    private static class Sensor extends Shape{
        public static final int VERTICAL = 0;
        public static final int HORIZONTAL = 1;

        private float position;
        private boolean active;
        private int type;

        Sensor(Shape shape, int type) {
            super(shape.context, shape.context.g, shape.getKind(), shape.getParams());
            this.position = shape.getParam(type);
            this.active = false;
            this.type = type;

            setStroke(Color.RED);
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public float getPosition() {
            return position;
        }

        public int getType() {
            return type;
        }
    }
}
