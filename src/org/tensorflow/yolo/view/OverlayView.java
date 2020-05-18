package org.tensorflow.yolo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.tensorflow.yolo.Config;
import org.tensorflow.yolo.R;
import org.tensorflow.yolo.model.BoxPosition;
import org.tensorflow.yolo.model.Recognition;
import org.tensorflow.yolo.setting.AppSetting;
import org.tensorflow.yolo.util.ClassAttrProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple View providing a render callback to other classes.
 * Modified by Zoltan Szabo
 */
public class OverlayView extends View {
    private final Paint paint;
    private final List<DrawCallback> callbacks = new LinkedList();
    private List<Recognition> results;
    private List<Integer> colors;
    private float resultsViewHeight;
    Bitmap bitmap;



    public OverlayView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                70, getResources().getDisplayMetrics())); //15
        resultsViewHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                112, getResources().getDisplayMetrics()); //112
        colors = ClassAttrProvider.newInstance(context.getAssets()).getColors();
    }

    public void addCallback(final DrawCallback callback) {
        callbacks.add(callback);
    }

    // 백지연
    @Override
    public synchronized void onDraw(final Canvas canvas) {
        for (final DrawCallback callback : callbacks) {
            callback.drawCallback(canvas);
        }

        if (results != null) {

           for (int i = 0; i < results.size(); i++) {
            //for (int i = 0; i < 1; i++) {
                AppSetting.object=results.get(i);
                RectF box = reCalcSize(results.get(i).getLocation());

                String title = results.get(i).getTitle() + ":"
                        + String.format("%.2f", results.get(i).getConfidence());
                paint.setColor(colors.get(results.get(i).getId()));
                canvas.drawRect(box, paint);
                canvas.drawText(title, box.left, box.top, paint);
                int viewWidth =AppSetting.viewWidth;
                int viewHeight=AppSetting.viewHeight;

                float W=bitmap.getWidth()/1080 ;
                float H=bitmap.getHeight()/720;
                int a=Math.round(box.left*W);
                int b=Math.round(box.top*H);
                int c=Math.round((box.right-box.left)*W);
                int d=Math.round((box.bottom-box.top)*H);
                Log.d("비트맵","-----------------------------------------------------------");
                Log.d("비트맵","box.left"+box.left);
                Log.d("비트맵","box.top"+box.top);
                Log.d("비트맵","box.right"+box.right);
                Log.d("비트맵","box.bottom"+box.bottom);
                Log.d("비트맵","박스너비"+(box.right-box.left));
                Log.d("비트맵","박스높이"+(box.bottom-box.top));
                Log.d("비트맵","bitmap width"+bitmap.getWidth());
                Log.d("비트맵","bitmap height"+bitmap.getHeight());
                ;  Log.d("비트맵","-----------------------------------------------------------");

                    if((a+bitmap.getWidth()/4)<bitmap.getWidth()){
                        c=bitmap.getWidth();
                    }else{
                        c=bitmap.getWidth()/2;
                    }
                    if((b+bitmap.getHeight()/4)<bitmap.getHeight()){
                        d=bitmap.getHeight();
                    }else{
                        d=bitmap.getHeight()/2;
                     }

                Bitmap newbitmap=Bitmap.createBitmap( bitmap,a,b,c,d);
                // 지연!===============================================================================
               //saveView(newbitmap);
               AppSetting.image=newbitmap;
                //break;
                // 추가

                // saveView(this,title);
                 //break;


            }
        }
    }



    public void setResults(final List<Recognition> results,Bitmap bitmap) {
        this.results = results;
        this.bitmap=bitmap;
        postInvalidate();
    }

    /**
     * Interface defining the callback for client classes.
     */
    public interface DrawCallback {
        void drawCallback(final Canvas canvas);
    }

    private RectF reCalcSize(BoxPosition rect) {
        int padding = 5;
        float overlayViewHeight = this.getHeight() - resultsViewHeight;
        float sizeMultiplier = Math.min((float) this.getWidth() / (float) Config.INPUT_SIZE,
                overlayViewHeight / (float) Config.INPUT_SIZE);

        float offsetX = (this.getWidth() - Config.INPUT_SIZE * sizeMultiplier) / 2;
        float offsetY = (overlayViewHeight - Config.INPUT_SIZE * sizeMultiplier) / 2 + resultsViewHeight;

        float left = Math.max(padding,sizeMultiplier * rect.getLeft() + offsetX);
        float top = Math.max(offsetY + padding, sizeMultiplier * rect.getTop() + offsetY);

        float right = Math.min(rect.getRight() * sizeMultiplier, this.getWidth() - padding);
        float bottom = Math.min(rect.getBottom() * sizeMultiplier + offsetY, this.getHeight() - padding);
        AppSetting.box_left=left;
        AppSetting.box_top=top;
        AppSetting.box_right=right;
        AppSetting.box_bottom=bottom;

        return new RectF(left, top, right, bottom);
    }
}
