package org.tensorflow.yolo.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tensorflow.yolo.MainActivity;
import org.tensorflow.yolo.R;
import org.tensorflow.yolo.model.BoxPosition;
import org.tensorflow.yolo.setting.AppSetting;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.tensorflow.yolo.Config.LOGGING_TAG;

/**
 * Camera activity class.
 * Modified by Zoltan Szabo
 */
public abstract class CameraActivity extends Activity implements OnImageAvailableListener {
    private static final int PERMISSIONS_REQUEST = 1;

    private Handler handler;
    private HandlerThread handlerThread;
    boolean not_this_time=false;

    Button hidebutton;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_camera);

        hidebutton=(Button)findViewById(R.id.hidebutton);

        AppSetting.viewWidth=hidebutton.getRootView().getWidth();
        AppSetting.viewHeight=hidebutton.getRootView().getHeight();
        if (hasPermission()) {
            setFragment();
        } else {
            requestPermission();
        }

    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        handlerThread = new HandlerThread("inference");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    public synchronized void onPause() {
        if (!isFinishing()) {
            finish();
        }

        handlerThread.quitSafely();
        try {
            handlerThread.join();
            handlerThread = null;
            handler = null;
        } catch (final InterruptedException ex) {
            Log.e(LOGGING_TAG, "Exception: " + ex.getMessage());
        }

        super.onPause();
    }

    protected synchronized void runInBackground(final Runnable runnable) {
        if (handler != null) {
            handler.post(runnable);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions,
                                           final int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    setFragment();
                } else {
                    requestPermission();
                }
            }
        }
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                    || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(CameraActivity.this,
                        "Camera AND storage permission are required for this demo", Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST);
        }
    }

    protected void setFragment() {
        CameraConnectionFragment cameraConnectionFragment = new CameraConnectionFragment();
        cameraConnectionFragment.addConnectionListener((final Size size, final int rotation) ->
                CameraActivity.this.onPreviewSizeChosen(size, rotation));
        cameraConnectionFragment.addImageAvailableListener(this);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, cameraConnectionFragment)
                .commit();
    }

    public void requestRender() {
        final OverlayView overlay = (OverlayView) findViewById(R.id.overlay);
        if (overlay != null) {

            overlay.postInvalidate();
        }
    }

    public void addCallback(final OverlayView.DrawCallback callback) {
        final OverlayView overlay = (OverlayView) findViewById(R.id.overlay);
        if (overlay != null) {
            overlay.addCallback(callback);
        }
    }

    protected abstract void onPreviewSizeChosen(final Size size, final int rotation);

    // 백지연 : 다이얼로그
    void showMeans(String means)
    {
        not_this_time=false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("단어장에 추가할까요?");
        builder.setMessage(AppSetting.object.getTitle()+" : "+means);
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                     //   Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
                      //  intent.putExtra("뷰", getApplicationContext());
                      //  intent.putExtra("메시지", "이것이 메시지의 vaule입니다.");
                      //  startActivity(intent);//액티비티 띄우기
                        Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                        dialog.cancel();// 네 버튼을 눌렀을 때, 다이얼로그가 종료되게 한다.


                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                        dialog.cancel();// 아니오 버튼을 눌렀을 때, 다이얼로그가 종료되게 한다.

                    }
                });
        // show함수에 왔다는 것은 다이얼로그가 0번 떴다는 뜻이다.그러므로 띄워도 된다.

            builder.show();
            // show함수를 모두 실행했으니 다이얼로그가 뜬 상태므로 1을 할당시킨다.
        AppSetting.dialog_flag=1;

    }
    // 백지연
// 백지연 추가
    @SuppressLint("WrongThread")
    private void saveView(View view, String title )

    {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);

        if(b!=null){
            try {
                File f = new File(path+"/notes");
                f.mkdir();
                File f2 = new File(path + "/notes/"+title+".png");
                Canvas c = new Canvas( b );
                view.draw( c );
                FileOutputStream fos = new FileOutputStream(f2);


                if ( fos != null )
                {
                    b.compress(Bitmap.CompressFormat.PNG, 100, fos );
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    public boolean onTouchEvent(MotionEvent event) {  //onTouchEvent 함수
        AppSetting.dialog_flag=0;
        if(AppSetting.object!=null){
            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
            jsoupAsyncTask.execute();
        }
        else{
            Toast.makeText(this, "검출중입니다.", Toast.LENGTH_SHORT).show();
        }
        //터치하는 곳의 좌표값을 얻어오기 위한 식
        float x = event.getX();
        float y = event.getY();
        Display display = getWindowManager().getDefaultDisplay();  // in Activity
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.v("좌표_터치값", x + "," + y); //터치하는 곳의 좌표값을 로그 창에서 볼 수 있다.
        Log.v("좌표_디스플레이 너비높이", width + "," + height); //터치하는 곳의 좌표값을 로그 창에서 볼 수 있다.
        Log.v("좌표_박스 ltrb", AppSetting.box_left+"-"+AppSetting.box_top+"-"+AppSetting.box_right+"-"+AppSetting.box_bottom);
        Log.v("좌표_박스 계산 후", AppSetting.box_left+"박스 오른쪽 좌표"+(width-Math.round(AppSetting.box_right)));

        int right=width-Math.round(AppSetting.box_right);

        //switch 문을 걸어 down동작이 일어날때만 조건이 실행되도록 한다. invalidate()를 사용해 View와 연결시켜주고, 변수들을 가져와서 조건을 작성해준다.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //코드의 가독성 때문에 0,1,2보다 이렇게 작성해주는 것이 좋다.
                if (AppSetting.box_left< x && right> x ) { //사각형 안을 누르면
                    //&& AppSetting.box_top< y && height-AppSetting.box_bottom > y
                  //  Toast.makeText(this, "맞다", Toast.LENGTH_SHORT).show();
                } else { //사각형 안을 누르지 않고 밖을 누르면
                   // Toast.makeText(this, "틀리다", Toast.LENGTH_SHORT).show();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        };

        return true;
    }
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        String means="뜻은?";
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String htmlPageUrl="https://dic.daum.net/search.do?type=eng&q="+AppSetting.object.getTitle();
                Document doc = Jsoup.connect(htmlPageUrl).get();
                // Elements mean = doc.select("span");
                // Elements mean = doc.select("span.trans dtrans dtrans-se");
                Elements mean = doc.select("meta").attr("name","description");

                int c=0;

            for(Element e:mean){
                c++;
                if(c==7){
                    means=means+e.attr("content");
                    Log.v("파싱", e+"");
                }
                //means=means+e+"\n";

            }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(AppSetting.dialog_flag==0){
                // 다이얼로그가 0번 띄워진 상태라면 show함수로 가서 다이얼로그를 설정한다.
                showMeans(means);
            }

        }
    }
}
