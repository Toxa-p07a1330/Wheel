import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseMotionListener;

import java.io.*;
public class Wheel  implements NativeMouseMotionListener, NativeKeyListener {

    final int KEYCODE = 9;
    private int shiftX = 0, shiftY=0;
    int prevX; int prevY;
    int currentXPos, currentYPos;
    boolean wasPressed = false;

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
       currentXPos = nativeMouseEvent.getX();
       currentYPos = nativeMouseEvent.getY();

    }
    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {

    }
    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        if (!wasPressed)
        {
            prevX = currentXPos;
            prevY = currentYPos;
        }
        wasPressed = true;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        shiftX = prevX - currentXPos;
        shiftY = prevY - currentYPos;
        System.out.println(shiftX+":"+shiftY);
        wasPressed = false;
    }



    public static void main(String[] args) throws FileNotFoundException {

        PrintStream err = new PrintStream(new FileOutputStream("err.log"));
        System.setErr(err);
        boolean isPressed = false;
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
