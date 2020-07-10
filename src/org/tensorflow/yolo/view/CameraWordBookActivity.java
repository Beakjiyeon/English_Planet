package org.tensorflow.yolo.view;

import android.app.Activity;
import android.graphics.Rect;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CameraWordBookActivity extends Activity {

    NetworkService networkService;
    TextView cword_text;
    TextView cword_text2;
    CameraAdpater adapter;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_camera_recycle);

        // 리사이클러뷰
        RecyclerView recyclerView=findViewById(R.id.camera_recycler);
        adapter=new CameraAdpater(new CameraAdpater.OnCameraClickListener(){

            @Override
            public void onCameraClicked(Camera model) {

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

                //Camera b = (Camera)response.body();
                //String b_text = b.getC_word_e();

                if(response!=null) {
                    List<Camera> list=response.body();
                    adapter.setItems(list);
                    int totalElements = list.size();// arrayList의 요소의 갯수를 구한다.
                    for (int index = 0; index < totalElements; index++) {

                        if(list.get(index).getUid().equals(AppSetting.uid)) {
                            Log.d("########", list.get(index).getC_word_e() + "/" + list.get(index).getC_word_k());
                            FirebaseStorage fs = FirebaseStorage.getInstance();

                            //StorageReference imagesRef = fs.getReference().child("Images/");
                            ImageView profileImgView=findViewById(R.id.cimage);
                            /*
                            Glide.with(getApplicationContext())
                                    .load(list.get(index).getC_url())
                                    .into(profileImgView);

                            cword_text=findViewById(R.id.cword_text);
                            cword_text.setText(list.get(index).getC_word_e());
                            cword_text2=findViewById(R.id.cword_text2);
                            cword_text2.setText(list.get(index).getC_word_k());

                             */
                        }
                    }
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

        @Override
        public void onBindViewHolder(@NonNull CameraViewHolder holder, int position) {
            Camera item = mItems.get(position);
            holder.c_word_e.setText(item.getC_word_e());
            holder.c_word_k.setText(item.getC_word_k());
            // 이미지 속도 처리
            Glide
                    .with(holder.c_url.getContext())
                    .load(item.getC_url())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.star)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .fitCenter())
                    .into(holder.c_url);


        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public static class CameraViewHolder extends RecyclerView.ViewHolder {
            TextView c_word_e;
            TextView c_word_k;
            ImageView c_url;

            public CameraViewHolder(@NonNull View itemView) {
                super(itemView);
                c_word_e=itemView.findViewById(R.id.cword_text);
                c_word_k=itemView.findViewById(R.id.cword_text2);
                c_url= itemView.findViewById(R.id.cimage);

            }


        }
    }

}