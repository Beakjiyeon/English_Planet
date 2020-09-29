package org.tensorflow.yolo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link wordtab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class wordtab extends Fragment {

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
        return inflater.inflate(R.layout.fragment_wordtab, container, false);


    }
}