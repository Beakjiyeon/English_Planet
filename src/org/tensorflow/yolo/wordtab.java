package org.tensorflow.yolo;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.tensorflow.yolo.setting.AppSetting;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link wordtab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class wordtab extends Fragment implements TextToSpeech.OnInitListener  {
    //tts
    TextToSpeech tts;
    TableLayout wordtable;
    NetworkService networkService;
    List<Myword> list;
    //View view;
    public wordtab() {
        // Required empty public constructor
    }

    public static wordtab newInstance() {
        wordtab wt = new wordtab();
        return wt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wordtab, container, false);
        wordtable = (TableLayout)v.findViewById(R.id.wordtable);
        networkService = RetrofitSender.getNetworkService();
        // tts create
        tts = new TextToSpeech(this.getActivity(), this, "com.google.android.tts");
        Call<List<Myword>> call = networkService.get_myword();
        call.enqueue(new Callback<List<Myword>>() {
            @Override
            public void onResponse(Call<List<Myword>> call, Response<List<Myword>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Myword> list2=new ArrayList<Myword>();
                    int count=0;
                    list=response.body();
                    int totalElements = list.size();// arrayList의 요소의 갯수를 구한다.
                    for (int index = 0; index < totalElements; index++) {
                        if (list.get(index).getUid().equals(AppSetting.uid) && list.get(index).getCheck_ws()==0) {
                            list2.add(list.get(index));
                        }
                    }

                    for(int i=0;i<list2.size();i++){
                        TableRow tableRow = new TableRow(getActivity());
                        int ii=i;
                        if(tableRow.getParent() != null) {
                            ((ViewGroup)tableRow.getParent()).removeView(tableRow); // <- fix
                        }
                            Button btnView = new Button(getActivity());
                            Button btnView2 = new Button(getActivity());
                            Button button = new Button(getActivity());
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    tts.speak(list2.get(ii).getM_word_e(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            });
                            button.setBackgroundResource(R.drawable.btn_sound);
                            button.setGravity(Gravity.CENTER);
                            btnView.setText(list2.get(i).getM_word_e());
                            btnView2.setText(list2.get(i).getM_word_k());
                            btnView.setBackgroundResource(R.drawable.btn_ee);
                            btnView2.setBackgroundResource(R.drawable.btn_kk);
                            btnView.setTextColor(Color.WHITE);
                            btnView2.setTextColor(Color.WHITE);
                            btnView.setGravity(Gravity.CENTER);
                             btnView2.setGravity(Gravity.CENTER);
                            btnView.setTextSize(20);
                            btnView2.setTextSize(20);
                            tableRow.addView(btnView, new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                            tableRow.addView(btnView2, new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                            tableRow.addView(button, new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                            wordtable.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Myword>> call, Throwable t) {
                Log.d("tag", "onFailure: " + t.toString()); //서버와 연결 실패
            }
        });

        return v;

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            //en-us-x-sfg#female_2-local
            tts.setLanguage(Locale.ENGLISH);
            Voice v = new Voice("en-us-x-sfg#female_2-local",
                    new Locale("en", "US"),
                    400,
                    400,
                    true,
                    null);
            tts.setVoice(v);
            tts.setSpeechRate(0.8f);
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

}