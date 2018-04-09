package daynight.daynnight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

/**
 * Created by Antoine Mascolo on 2018-04-04.
 */
//Inspiré par le tutoriel suivant
//http://www.instructables.com/id/A-Simple-Android-UI-Joystick/

public class Joystick extends SurfaceView implements Callback, View.OnTouchListener {

    float centerX;
    float centerY;
    float jsBottom;
    float jsTop;
    JoystickListener joystickCallback;

    public Joystick(Context context) {
        super(context);
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        setOnTouchListener(this);
        if(context instanceof JoystickListener){
            joystickCallback = (JoystickListener) context;
        }
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        setOnTouchListener(this);
        if(context instanceof JoystickListener){
            joystickCallback = (JoystickListener) context;
        }

    }

    public Joystick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        setOnTouchListener(this);
        if(context instanceof JoystickListener){
            joystickCallback = (JoystickListener) context;
        }

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        setDimension();
        draw(centerX,centerY);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(view.equals(this)){
            float displacement = (float) Math.sqrt(Math.pow(motionEvent.getX() - centerX, 2) + Math.pow(motionEvent.getY() - centerY, 2));
            try {
                if(motionEvent.getAction() != MotionEvent.ACTION_UP){
                    if (displacement < jsBottom){
                        draw(motionEvent.getX(), motionEvent.getY());
                            joystickCallback.onJoystickMoved((motionEvent.getX() - centerX) / jsBottom, ((motionEvent.getY() - centerY) / jsBottom)*-1, getId());

                    }
                    else{
                        float ratio = jsBottom / displacement;
                        float constrainedX = centerX + (motionEvent.getX() - centerX) * ratio;
                        float constrainedY = centerY + (motionEvent.getY() - centerY) * ratio;
                        draw(constrainedX, constrainedY);
                        joystickCallback.onJoystickMoved((constrainedX - centerX) / jsBottom, ((constrainedY - centerY) / jsBottom) *-1, getId());
                    }
                }else{
                    draw(centerX,centerY);
                    joystickCallback.onJoystickMoved(0, 0, getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void setDimension(){
        centerX = getWidth()/2;
        centerY = getHeight()/2;
        jsBottom = Math.min(getWidth(), getHeight()) / 2;
        jsTop = Math.min(getWidth(), getHeight()) / 5;

    }

    private void draw(float x,float y){

        if(getHolder().getSurface().isValid()){
            Canvas canvas = this.getHolder().lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            Paint color = new Paint();

            color.setARGB(100, 50,50,50);
            canvas.drawCircle(centerX, centerY, jsBottom, color);
            color.setARGB(100, 255,0,0);
            canvas.drawCircle(x, y, jsTop, color);
            getHolder().unlockCanvasAndPost(canvas);
        }

    }

    public interface JoystickListener
    {
        void onJoystickMoved(float xPercent, float yPercent, int source) throws IOException;
    }

}
