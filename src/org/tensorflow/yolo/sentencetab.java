package org.tensorflow.yolo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.tensorflow.yolo.setting.AppSetting;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sentencetab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sentencetab extends Fragment {
    TableLayout sentable;
    NetworkService networkService;
    List<Myword> list;

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
        networkService = RetrofitSender.getNetworkService();
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

                    for(int i=0;i<list2.size();i++){
                        TableRow tableRow = new TableRow(getActivity());

                        if(tableRow.getParent() != null) {
                            ((ViewGroup)tableRow.getParent()).removeView(tableRow); // <- fix
                        }
                        TextView textView = new TextView(getActivity());
                        TextView textView2 = new TextView(getActivity());
                        textView.setText(list2.get(i).getM_word_e());
                        textView2.setText(list2.get(i).getM_word_k());
                        textView.setTextColor(Color.WHITE);
                        textView2.setTextColor(Color.WHITE);
                        textView.setGravity(Gravity.CENTER);
                        textView2.setGravity(Gravity.CENTER);
                        textView.setTextSize(20);
                        textView2.setTextSize(20);
                        tableRow.addView(textView, new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                        tableRow.addView(textView2, new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                        sentable.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
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
}