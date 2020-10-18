package org.tensorflow.yolo.ownmodel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tensorflow.yolo.Camera;
import org.tensorflow.yolo.NetworkService;
import org.tensorflow.yolo.R;
import org.tensorflow.yolo.RetrofitSender;
import org.tensorflow.yolo.setting.AppSetting;
import org.tensorflow.yolo.view.CameraActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraMainActivity extends AppCompatActivity {

    List<Classifier.Recognition> resultss;
    // DB
    StorageReference mStorageRef;
    String imageUrl;
    NetworkService networkService;
    String fuckUrl;

    // 로딩
    public ProgressDialog pdialog;
    public Context mctk=CameraMainActivity.this;

    Bitmap bitmap;
    private static final String MODEL_PATH = "comon_output_quant.tflite";
    private static final boolean QUANT = true;
    private static final String LABEL_PATH = "comon_model_labels.txt";
    private static final int INPUT_SIZE = 224;

    private Classifier classifier;

    private Executor executor = Executors.newSingleThreadExecutor();
    private TextView textViewResult;
    private Button btnDetectObject, btnToggleCamera;
    private ImageView imageViewResult;
    private CameraView cameraView;
    private Button btnSaveDB;
    boolean captureFlag=true;
    private String english_word;
    private String korea_word;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_main);
        cameraView = findViewById(R.id.cameraView);
        imageViewResult = findViewById(R.id.imageViewResult);
        textViewResult = findViewById(R.id.textViewResult);

        textViewResult.setMovementMethod(new ScrollingMovementMethod());

        btnToggleCamera = findViewById(R.id.btnToggleCamera);
        btnDetectObject = findViewById(R.id.btnDetectObject);
        btnSaveDB=findViewById(R.id.btnSaveDB);
        btnSaveDB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               saveView();
            }
        });

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                bitmap = cameraKitImage.getBitmap();
                imageViewResult.setImageBitmap(bitmap);
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
                final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
                resultss=results;
                // 지연 : result들 중 퍼센트가 가장 높은 1개만 처리
                String max=results.get(0).toString();
                english_word=(max.split(" "))[1];
                Log.d("인식",results.toString());
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();




            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        btnToggleCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.toggleFacing();
            }
        });

        btnDetectObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(captureFlag){ //
                    btnSaveDB.setVisibility(View.VISIBLE);
                    btnDetectObject.setBackgroundResource(R.drawable.btn_shoot2); // 다시하기 버튼 설정
                    cameraView.captureImage();


                }else{
                    textViewResult.setVisibility(View.INVISIBLE);
                    btnSaveDB.setVisibility(View.INVISIBLE);
//                    btnDetectObject.setText("캡처하기");
//                    btnDetectObject.setBackgroundColor(Color.RED);
                    btnDetectObject.setBackgroundResource(R.drawable.btn_shoot1); // 연한색깔 버튼
                    captureFlag=true;
                    imageViewResult.setVisibility(View.INVISIBLE);

                }

            }

        });

        initTensorFlowAndLoadModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_PATH,
                            LABEL_PATH,
                            INPUT_SIZE,
                            QUANT);
                    makeButtonVisible();
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    private void makeButtonVisible() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnDetectObject.setVisibility(View.VISIBLE);
            }
        });
    }
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            pdialog=new ProgressDialog(mctk);
            pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdialog.setMessage("처리 중 입니다...");
            pdialog.show();
            super.onPreExecute();

        }
        String means="";
        @Override
        protected Void doInBackground(Void... params) {

            try {
                String htmlPageUrl="https://dic.daum.net/search.do?type=eng&q="+ english_word;
                Document doc = Jsoup.connect(htmlPageUrl).get();
                Elements mean = doc.select("meta").attr("name","description");
                int c=0;
                for(Element e:mean){
                    c++;
                    if(c==7){
                        means=means+e.attr("content");
                        Log.v("파싱", e+"");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //imageViewResult.setVisibility(View.VISIBLE);

            korea_word=means;
            Toast.makeText(CameraMainActivity.this, resultss.toString(), Toast.LENGTH_SHORT).show();
            textViewResult.setText(english_word+"\n"+korea_word);
            // 하.../
            //textViewResult.setText(resultss.toString());
            pdialog.dismiss();

            textViewResult.setVisibility(View.VISIBLE);
            Log.d("뭐지",textViewResult.getVisibility()+" ");


            Log.d("뭐지",textViewResult.getVisibility()+" ");
            captureFlag=false;
        }
    }
    // 파이어베이스 연결
    private void saveView()
    {
        if(bitmap!=null){
            mStorageRef= FirebaseStorage.getInstance().getReference("Images/"+english_word);
            Bitmap image_bitmap = bitmap; //?????????????????????????????/
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mStorageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(CameraMainActivity.this,"실패",Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // FIREBASE STORAGE 이미지 저장
                    // 성공하면
                    Toast.makeText(CameraMainActivity.this,"저장 되었습니다. . .",Toast.LENGTH_LONG).show();

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return mStorageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                networkService = RetrofitSender.getNetworkService();
                                // MYSQL CAMERA TABLE DB 저장
                                Call<Camera> response = networkService.register_camera(task.getResult().toString(),english_word,korea_word,AppSetting.uid);
                                response.enqueue(new Callback<Camera>() {
                                    @Override
                                    public void onResponse(Call<Camera> call, Response<Camera> response) {
                                        Log.d("카메라","성공================================================================");
                                    }
                                    @Override
                                    public void onFailure(Call<Camera> call, Throwable t) {
                                        Log.d("카메라","실패================================================================");
                                    }
                                });
                                fuckUrl=imageUrl;
                                AppSetting.uarl=imageUrl;
                                Log.e("fuckUrl"+fuckUrl,"==============================================");
                            } else {
                                Log.d("이미지","실패");
                            }
                        }
                    });

                }
            });


        }
    }

}
