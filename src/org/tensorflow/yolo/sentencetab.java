package org.tensorflow.yolo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.tensorflow.yolo.setting.AppSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sentencetab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sentencetab extends Fragment implements TextToSpeech.OnInitListener  {
    //tts
    TextToSpeech tts;
    TableLayout sentable;
    NetworkService networkService;
    List<Myword> list;
    Button rebtn_s;

    public sentencetab() {
        // Required empty public constructor
    }


    public static sentencetab newInstance() {
        sentencetab st = new sentencetab();
        return st;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sentencetab, container, false);
        sentable = (TableLayout)v.findViewById(R.id.sentable);
        rebtn_s = (Button)v.findViewById(R.id.rebtn_s);
        networkService = RetrofitSender.getNetworkService();
        // 재학습 숫자 초기화
        AppSetting.myquizcount=0;
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
                        if (list.get(index).getUid().equals(AppSetting.uid) && list.get(index).getCheck_ws()==1) {
                            list2.add(list.get(index));
                        }
                    }

                    // 재학습 버튼 나타내기
                    if(list2.size()>0){
                        rebtn_s.setVisibility(View.VISIBLE);
                    }
                    for(int i=0;i<list2.size();i++){
                        TableRow tableRow = new TableRow(getActivity());
                        int ii=i;
                        if(tableRow.getParent() != null) {
                            ((ViewGroup)tableRow.getParent()).removeView(tableRow); // <- fix
                        }
                        Button btnView = new Button(getActivity());
                        Button btnView2 = new Button(getActivity());
                        btnView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tts.speak(list2.get(ii).getM_word_e(), TextToSpeech.QUEUE_FLUSH,null);
                            }
                        });
                        TextView textView = new TextView(getActivity());
                        TextView textView2 = new TextView(getActivity());
                        btnView.setText(list2.get(i).getM_word_e());
                        btnView2.setText(list2.get(i).getM_word_k());
                        btnView.setTextColor(Color.BLACK);
                        btnView2.setTextColor(Color.BLACK);
                        btnView.setBackgroundResource(R.drawable.img_ee);
                        btnView2.setBackgroundResource(R.drawable.img_kk);
                        btnView.setGravity(Gravity.CENTER);
                        btnView2.setGravity(Gravity.CENTER);
                        btnView.setTypeface(getResources().getFont(R.font.cookierun_bold));
                        btnView2.setTypeface(getResources().getFont(R.font.cookierun_regular));
                        btnView.setTextSize(20);
                        btnView2.setTextSize(20);
                        tableRow.addView(btnView, new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                        tableRow.addView(btnView2, new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                        sentable.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Myword>> call, Throwable t) {
                Log.d("tag", "onFailure: " + t.toString()); //서버와 연결 실패
            }
        });

        rebtn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),MysenQuizActivity.class);
                startActivity(i);
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