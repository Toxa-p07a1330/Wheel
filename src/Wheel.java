import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseMotionListener;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class ButtomMatcher{

    Robot robot;
    final int numberOfButtons = 5;
    int buttonSection = 360/numberOfButtons;
    int baseShift = 90-buttonSection/2;
    ArrayList<Integer> codes = new ArrayList<>();
    void matchButtons(){
        for (int i =49; i<54; i++)
            codes.add(i);

    }
    int getCodeByAngle(double angle){
        angle= (angle+360-baseShift)%360;
        int section = (int) angle/buttonSection;
        System.out.println(codes.get(section));
        return codes.get(section);
    }
    ButtomMatcher() {
        matchButtons();
        try
        {
            robot = new Robot();
        }
        catch (AWTException e) {
            e.printStackTrace();
        }
    }
    void pressKey(int keyPress)
    {

        robot.keyPress(keyPress);
        robot.keyRelease(keyPress);
    }
}
class Shift{
    int xShift;
    int yShift;

    public int getxShift() {
        return xShift;
    }

    public int getyShift() {
        return yShift;
    }

    public void setxShift(int xShift) {
        this.xShift = xShift;
    }

    public void setyShift(int yShift) {
        this.yShift = yShift;
    }
    double getLenght(){
        return Math.sqrt(xShift*xShift+yShift*yShift);
    }

}
class NormalisedShift{
  double normalizedXShift;
  double normalizedYShift;
  double angle;
  NormalisedShift (Shift shift){
      if (shift.getLenght()>0)
      {
          normalizedXShift = -shift.getxShift()/shift.getLenght();
        normalizedYShift = -shift.getyShift()/shift.getLenght();
      }
      else
      {
          normalizedXShift = 0;
          normalizedYShift = 0;
      }
  }
  void calcAngle(){
      angle = Math.acos(normalizedXShift);
  }

  double getAngle(){
      calcAngle();
      return (normalizedYShift<0?angle*360/2/Math.PI:360-angle*360/2/Math.PI);
    };
};
public class Wheel  implements NativeMouseMotionListener, NativeKeyListener {

    final int KEYCODE = 15;
    Shift prev = new Shift();
    Shift current = new Shift();
    ButtomMatcher mathcer = new ButtomMatcher();


    boolean wasPressed = false;

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
        current.setxShift(nativeMouseEvent.getX());
        current.setyShift(nativeMouseEvent.getY());

    }
    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {

    }
    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        if (nativeKeyEvent.getKeyCode()==KEYCODE){
            if (!wasPressed)
            {
                prev.setxShift(current.getxShift());
                prev.setyShift(current.getyShift());
            }
            wasPressed = true;

        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        if (nativeKeyEvent.getKeyCode() == KEYCODE){
            Shift shift = new Shift();
            shift.setxShift(prev.getxShift() - current.getxShift());
            shift.setyShift(prev.getyShift() - current.getyShift());
            NormalisedShift normalisedShift = new NormalisedShift(shift);
            mathcer.pressKey(mathcer.getCodeByAngle(normalisedShift.getAngle()));
            wasPressed = false;
        }
    }



    public static void main(String[] args) throws FileNotFoundException, AWTException {

       // PrintStream err = new PrintStream(new FileOutputStream("err.log"));
       // System.setErr(err);
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

        }
        Wheel wheel = new Wheel();
    GlobalScreen.addNativeMouseMotionListener(wheel);
    GlobalScreen.addNativeKeyListener(wheel);

    }



}
