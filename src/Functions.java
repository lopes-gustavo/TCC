import static java.lang.Math.*;

public class Functions {
    private static float millisOld;
    private static float gTime;
    private Sketch context;

    public Functions(Sketch context) {
        this.context = context;
    }

    private int millis() {
        return context.millis();
    }

    private void IK() {
        float X = context.posX;
        float Y = context.posY;
        float Z = context.posZ;

        double L = sqrt(Y * Y + X * X);
        double dia = sqrt(Z * Z + L * L);

        float f = 50;
        float t = 70;
        context.alpha = (float) ((PI / 2) - (atan2(L, Z) + acos((t * t - f * f - dia * dia) / (-2 * f * dia))));
        context.beta = (float) (-PI + acos((dia * dia - t * t - f * f) / (-2 * f * t)));
        context.gamma = (float) atan2(Y, X);
    }

    private void setTime() {
        float gSpeed = 4;
        gTime += ((float) millis() / 1000 - millisOld) * (gSpeed / 4);
        if (gTime >= 4) gTime = 0;
        millisOld = (float) millis() / 1000;
    }

    public void writePos() {
        IK();
        setTime();
        context.posX = (float) (sin(gTime * PI / 2) * 20);
        context.posZ = (float) (sin(gTime * PI) * 10);
    }
}