package org.tensorflow.yolo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class AudioActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_audio);
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.RECORD_AUDIO)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }

            }
            final Button ButtonStart=(Button)findViewById(R.id.buttonS);
            final Button ButtonPause=(Button)findViewById(R.id.buttonPause);
            final Button ButtonStop=(Button)findViewById(R.id.buttonStop);

            final EditText editText1=(EditText)findViewById(R.id.editText1);
            final EditText editText2=(EditText)findViewById(R.id.editText2);
            SeekBar seekBar1=(SeekBar)findViewById(R.id.seekBar1);
            SeekBar seekBar2=(SeekBar)findViewById(R.id.seekBar2);

            seekBar1.setMax(4);
            seekBar1.setProgress(2);
            seekBar2.setMax(999);
            seekBar2.setProgress(999);

            ButtonStart.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //스타트 버튼 클릭시 수행
                    RecordManager rm=new RecordManager(new BoardManager(getApplicationContext()));
                    rm.onStart();
                    rm.start();
                }
            });

            ButtonPause.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //일시정지 버튼 클릭시 수행
                }
            });

            ButtonStop.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //정지 버튼 클릭시 스타트, 일시정지 구동 못하도록 만듬
                    ButtonStart.setEnabled(false);
                    ButtonPause.setEnabled(false);
                    //정지 버튼 클릭시 수행
                }
            });

            seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    editText1.setText(String.valueOf(+progress+1));
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
            });
            seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    editText2.setText(String.valueOf(progress + 1));
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
            });
        }
    }