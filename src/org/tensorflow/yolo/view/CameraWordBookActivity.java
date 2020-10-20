package org.tensorflow.yolo.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tensorflow.yolo.Camera;
import org.tensorflow.yolo.NetworkService;
import org.tensorflow.yolo.R;
import org.tensorflow.yolo.RetrofitSender;
import org.tensorflow.yolo.setting.AppSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Path;

import static android.media.audiofx.AudioEffect.ERROR;

public class CameraWordBookActivity extends Activity {

    NetworkService networkService;
    TextView cword_text;
    TextView cword_text2;
    CameraAdpater adapter;
    static TextToSpeech  tts;
    RecyclerView recyclerView;
    List<Camera> list;
    int delete;



    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
    }
    public static void voice(String text){
        tts.setLanguage(Locale.ENGLISH); // tts 언어설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    @Override
    protected void onDestroy() {

        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_camera_recycle);



// tts추가
        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.GERMAN);
                    if(result==TextToSpeech.LANG_MISSING_DATA
                            ||result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("tts","language not supported");
                    }
                    else{
                        //mButtonSpeak.setEnabled(true);
                    }
                }else{
                    Log.e("tts","initialized failed");
                }
            }
        });


        // 리사이클러뷰
        recyclerView=findViewById(R.id.camera_recycler);
        adapter=new CameraAdpater(new CameraAdpater.OnCameraClickListener(){

            @Override
            public void onCameraClicked(Camera model) {
                // 다이얼로그 메세지 띄우기
                AlertDialog.Builder builder = new AlertDialog.Builder(CameraWordBookActivity.this);
                builder.setTitle("단어장에서 삭제하시겠습니까?");

                builder.setMessage(model.getC_word_e());
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                                // 단어 db에서 삭제 하는 함수 호출
                                delete=model.getC_id();
                                deleteCword(delete);

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
            }
        });

        LinearLayoutManager horizonalLayoutManager
                = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizonalLayoutManager);


        recyclerView.setAdapter(adapter);
        MyListDecoration decoration = new MyListDecoration();
        recyclerView.addItemDecoration(decoration);

        networkService = RetrofitSender.getNetworkService();
        // b_id : 1번으로 설정
        Call<List<Camera>> response = networkService.get_Cameras();
        response.enqueue(new Callback<List<Camera>>() {
            @Override
            public void onResponse(Call<List<Camera>> call, Response<List<Camera>> response) {
                if(response!=null) {
                    ArrayList<Camera> list2=new ArrayList<Camera>();

                    list=response.body();
                    //adapter.setItems(list);
                    int totalElements = list.size();// arrayList의 요소의 갯수를 구한다.
                    for (int index = 0; index < totalElements; index++) {
                        if(list.get(index).getUid().equals(AppSetting.uid)) {
                            Log.d("########", list.get(index).getC_word_e() + "/" + list.get(index).getC_word_k());
                            list2.add(list.get(index));
                        }
                    }
                    adapter.setItems(list2);
                }else{
                    Log.d("########", "xxxxxxx");
                }
                Log.d("########", "xxxㅇㅇxxxx");
            }

            @Override
            public void onFailure(Call<List<Camera>> call, Throwable t) {
                Log.d("########", t.getMessage());
            }
        });
    }// oncreate()

    // 사물인식 단어장 단어 삭제하는 함수
    private void deleteCword(int c_id){
        networkService = RetrofitSender.getNetworkService();
        // b_id : 1번으로 설정
        Call<ResponseBody> response = networkService.delete_camera(c_id);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response!=null) {
                    Log.d("########", "삭제하고싶다");

                    Call<List<Camera>> response2 = networkService.get_Cameras();
                    response2.enqueue(new Callback<List<Camera>>() {
                        @Override
                        public void onResponse(Call<List<Camera>> call, Response<List<Camera>> response2) {
                            if(response2!=null) {
                                ArrayList<Camera> list2=new ArrayList<Camera>();
                                list=response2.body();
                                int totalElements = list.size();// arrayList의 요소의 갯수를 구한다.
                                for (int index = 0; index < totalElements; index++) {
                                    if(list.get(index).getUid().equals(AppSetting.uid)) {
                                        Log.d("########", list.get(index).getC_word_e() + "/" + list.get(index).getC_word_k());
                                        list2.add(list.get(index));
                                    }
                                }
                                adapter.setItems(list2);
                                Toast.makeText(getApplicationContext(),"삭제했습니다...",Toast.LENGTH_LONG).show();

                            }else{
                                Log.d("########", "xxxxxxx");
                            }
                            Log.d("########", "xxxㅇㅇxxxx");
                        }

                        @Override
                        public void onFailure(Call<List<Camera>> call, Throwable t) {
                            Log.d("########", t.getMessage());
                        }
                    });


                }else{
                    Log.d("########", "ㄴㄴㄴxxxxxxx");
                }
                Log.d("########", "ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇxxxㅇㅇxxxx");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("########", t.getMessage());
            }
        });

    }
    // 리사이클러뷰
    private static class CameraAdpater extends RecyclerView.Adapter<CameraAdpater.CameraViewHolder> {
        interface OnCameraClickListener {
            void onCameraClicked(Camera model);
        }

        private OnCameraClickListener mListener;

        private List<Camera> mItems = new ArrayList<>();

        public CameraAdpater() {}

        public CameraAdpater(OnCameraClickListener listener) {
            mListener = listener;
        }

        public void setItems(List<Camera> items) {
            this.mItems = items;
            notifyDataSetChanged();
        }

        //TODO
        @NonNull
        @Override
        public CameraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_camera, parent, false);
            final CameraViewHolder viewHolder = new CameraViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        final Camera item = mItems.get(viewHolder.getAdapterPosition());
                        mListener.onCameraClicked(item);
                    }
                }
            });

            return viewHolder;
        }

        // TODO 버튼선언
        @Override
        public void onBindViewHolder(@NonNull CameraViewHolder holder, int position) {
            Camera item = mItems.get(position);
            holder.c_word_e.setText(item.getC_word_e());
            holder.c_word_k.setText(item.getC_word_k());
            String str = holder.c_word_e.getText().toString();

            holder.btn_word_sound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    voice(str);
                }
            });

            Glide
                    .with(holder.c_url.getContext())
                    .load(item.getC_url())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.star)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .fitCenter())
                    .into(holder.c_url);

        } // onBindViewHolder

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public static class CameraViewHolder extends RecyclerView.ViewHolder {

            TextView c_word_e;
            TextView c_word_k;
            ImageView c_url;
            //TODO
            ImageView btn_word_x;
            ImageView btn_word_sound;

            public CameraViewHolder(@NonNull View itemView) {
                super(itemView);
                // findviewbyid 설정
                btn_word_sound = itemView.findViewById(R.id.btn_word_sound);
                btn_word_x = itemView.findViewById(R.id.btn_word_x);
                c_word_e=itemView.findViewById(R.id.cword_text);
                c_word_k=itemView.findViewById(R.id.cword_text2);
                c_url= itemView.findViewById(R.id.cimage);



            }


        }
    }

}