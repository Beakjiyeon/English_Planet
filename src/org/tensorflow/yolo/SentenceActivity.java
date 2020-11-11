package org.tensorflow.yolo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONObject;
import org.tensorflow.yolo.setting.AppSetting;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SentenceActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    ImageView soundform;

    //tts
    TextToSpeech tts;
    Button buttonSound;

    public static final String PREFS_NAME = "prefs";
    private static final String MSG_KEY = "status";

    Button buttonStart;
    TextView textResult,sen;
    //Spinner spinnerMode;
    EditText editID;
    String strsen;
    String[] array_sen;
    public static String qw="";

    String curMode;
    String result;
    int countword;
    int mB_id;

    NetworkService networkService;

    int maxLenSpeech = 16000 * 45;
    byte [] speechData = new byte [maxLenSpeech * 2];
    int lenSpeech = 0;
    boolean isRecording = false;
    boolean forceStop = false;
    private static final int REQUEST_MICROPHONE = 3;


    private final Handler handler = new Handler() {
        @Override
        public synchronized void handleMessage(Message msg) {
            Bundle bd = msg.getData();
            String v = bd.getString(MSG_KEY);
            switch (msg.what) {
                // 녹음이 시작되었음(버튼)
                case 1:
                    textResult.setText(v);
                    //buttonStart.setText("PUSH TO STOP");
                    break;
                // 녹음이 정상적으로 종료되었음(버튼 또는 max time)
                case 2:
                    textResult.setText(v);
                    buttonStart.setEnabled(false);
                    break;
                // 녹음이 비정상적으로 종료되었음(마이크 권한 등)
                case 3:
                    textResult.setText(v);
                    //buttonStart.setText("PUSH TO START");
                    break;
                // 인식이 비정상적으로 종료되었음(timeout 등)
                case 4:
                    textResult.setText(v);
                    buttonStart.setEnabled(true);
                    //buttonStart.setText("PUSH TO START");
                    break;
                // 인식이 정상적으로 종료되었음 (thread내에서 exception포함)
                case 5:
                    textResult.setText(StringEscapeUtils.unescapeJava(result));
                    buttonStart.setEnabled(true);
                    //buttonStart.setText("PUSH TO START");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void SendMessage(String str, int id) {
        Message msg = handler.obtainMessage();
        Bundle bd = new Bundle();
        bd.putString(MSG_KEY, str);
        msg.what = id;
        msg.setData(bd);
        handler.sendMessage(msg);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MICROPHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "승인이 허가되어 있습니다.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "아직 승인받지 않았습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);
        // getIntent
        Intent intents = getIntent();
        mB_id = intents.getIntExtra("b_id", 0);

        // back
        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // imageview
        soundform = findViewById(R.id.soundform);
        Glide.with(this).load(R.drawable.soundform).into(soundform);
        soundform.setVisibility(View.INVISIBLE);



        // tts create
        tts = new TextToSpeech(this.getApplicationContext(), this, "com.google.android.tts");

        // buttonSound 영어문장퀴즈 tts
        buttonSound = (Button)findViewById(R.id.buttonSound);
        // onclick evnet
        buttonSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(sen.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });


        sen=(TextView)findViewById(R.id.sen);
        buttonStart = (Button)findViewById(R.id.buttonStart);
        textResult = (TextView)findViewById(R.id.textResult);
        textResult.setVisibility(View.GONE);
        //spinnerMode = (Spinner)findViewById(R.id.spinnerMode);
        editID = (EditText)findViewById(R.id.editID);
        editID.setVisibility(View.GONE);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        editID.setText(settings.getString("client-id", "9b9949b9-b277-438a-9658-a1007305d28e"));

        // 서버 데이터 가져옴
        networkService = RetrofitSender.getNetworkService();
        Call<List<Quiz>> call =  networkService.get_qb(mB_id);
        call.enqueue(new Callback<List<Quiz>>(){
            @Override
            public void onResponse(Call<List<Quiz>> call, Response<List<Quiz>> response) {
                if (response.isSuccessful()) {
                    Quiz qq= response.body().get(AppSetting.quizsen);
                    String bo=qq.q_sentence_e;
                    qw=qq.q_word;
                    sen.setText(bo);
                }
                else {

                }

            }

            @Override
            public void onFailure(Call<List<Quiz>> call, Throwable t) {
                Log.d("tag", "onFailure: " + t.toString()); //서버와 연결 실패
            }
        });

        //앱권한이 없으면 권한 요청
        int permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        if (permssionCheck!= PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"권한 승인이 필요합니다",Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this,"000부분 사용을 위해 마이크 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_MICROPHONE );
                Toast.makeText(this,"000부분 사용을 위해 마이크 권한이 필요합니다.",Toast.LENGTH_LONG).show();

            }
        }


        /* ArrayList<String> modeArr = new ArrayList<>();
        modeArr.add("한국어인식");
        modeArr.add("영어인식");
        modeArr.add("영어발음평가");
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, modeArr);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /*spinnerMode.setAdapter(modeAdapter);
        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                curMode = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
                curMode = "";
            }
        });*/
        final Intent intent=new Intent(getApplicationContext(),ScoreActivity.class);

        editID.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("client-id", v.getText().toString());
                    editor.apply();
                }
                return false;
            }
        });



        buttonStart.setOnClickListener(new  View.OnClickListener() {
            public void onClick(View v) {
                if (isRecording) {
                    soundform.setVisibility(View.INVISIBLE);
                    buttonStart.setBackgroundResource(R.drawable.sen_btn);
                    forceStop = true;
                } else {
                    soundform.setVisibility(View.VISIBLE);
                    buttonStart.setBackgroundResource(R.drawable.sen_recoding_btn);

                    try {
                        new Thread(new Runnable() {
                            public void run() {
                                SendMessage("Recording...", 1);
                                try {
                                    recordSpeech();
                                    SendMessage("Recognizing...", 2);
                                } catch (RuntimeException e) {
                                    SendMessage(e.getMessage(), 3);
                                    return;
                                }

                                Thread threadRecog = new Thread(new Runnable() {
                                    public void run() {
                                        result = sendDataAndGetResult();
                                        JSONObject obj = null;
                                        try {
                                            obj = new JSONObject(result);
                                            obj=new JSONObject(obj.getString("return_object"));
                                            String score=obj.getString("score");
                                            String recognized = obj.getString("recognized");
                                            countword = countWord(recognized);


                                            // Log.d("score",score);
                                            if(Double.parseDouble(score)>=3) {
                                                Log.d("점수", "Excellent");
                                            }
                                            else if(Double.parseDouble(score)>=3) {
                                                Log.d("점수", "Good");
                                            }
                                            else {
                                                Log.d("점수", "miss");
                                            }
                                            Log.d("흠냐리확인",obj+"");
                                            Log.d("흠냐리카운트",countword+"");
                                            Log.d("흠냐리스코어",Double.parseDouble(score)+"");
                                            intent.putExtra("countword",countword);
                                            intent.putExtra("myscore",Double.parseDouble(score));

                                            intent.putExtra("b_id",  mB_id);
                                            startActivity(intent);
                                            finish();
                                        }catch(Throwable t){
                                            Log.d("에러","object 없음");
                                        }



                                    }
                                });



                                threadRecog.start();
                                try {
                                    threadRecog.join(20000);
                                    if (threadRecog.isAlive()) {
                                        threadRecog.interrupt();
                                        SendMessage("No response from _server for 20 secs", 4);
                                    } else {
                                        SendMessage("OK", 5);
                                    }
                                } catch (InterruptedException e) {
                                    SendMessage("Interrupted", 4);
                                }
                            }
                        }).start();
                    } catch (Throwable t) {
                        textResult.setText("ERROR: " + t.toString());
                        forceStop = false;
                        isRecording = false;
                    }
                }
            }
        });
    }
    //단어 비교
    public static int countWord(String recognized){
        if (recognized.length() > 0 && recognized.charAt(recognized.length()-1)=='.') {
            recognized = recognized.substring(0, recognized.length()-1); // 인식된 단어
        }
        String[] array_recognized = recognized.split(" ");  //인식된 단어 배열
        String[] array_word = qw.split(" "); // 비교단어 배열
        for(int i=0;i<array_recognized.length;i++){

        }

        int cw=0; // 일치값
        for(int i=0;i<array_recognized.length;i++){
            for(int j=0;j<array_word.length;j++){
                Log.d("흠냘쓰",array_recognized[i]+" , "+array_word[j]);
                if(array_recognized[i].equals(array_word[j]))  // 단어가 일치하면
                    cw++;   // 일치값 증가
            }
        }
        Log.d("숫자1",String.valueOf(cw));
        return cw;  // 일치값 리턴

    }

    public static String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    public void recordSpeech() throws RuntimeException {
        try {
            int bufferSize = AudioRecord.getMinBufferSize(
                    16000, // sampling frequency
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            AudioRecord audio = new AudioRecord(
                    MediaRecorder.AudioSource.VOICE_RECOGNITION,
                    16000, // sampling frequency
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize);
            lenSpeech = 0;
            if (audio.getState() != AudioRecord.STATE_INITIALIZED) {
                throw new RuntimeException("ERROR: Failed to initialize audio device. Allow app to access microphone");
            }
            else {
                short [] inBuffer = new short [bufferSize];
                forceStop = false;
                isRecording = true;
                audio.startRecording();

                while (!forceStop) {
                    int ret = audio.read(inBuffer, 0, bufferSize);
                    for (int i = 0; i < ret ; i++ ) {
                        if (lenSpeech >= maxLenSpeech) {
                            forceStop = true;
                            break;
                        }
                        speechData[lenSpeech*2] = (byte)(inBuffer[i] & 0x00FF);
                        speechData[lenSpeech*2+1] = (byte)((inBuffer[i] & 0xFF00) >> 8);
                        lenSpeech++;
                    }
                }
                audio.stop();
                audio.release();
                isRecording = false;
            }
        } catch(Throwable t) {
            throw new RuntimeException(t.toString());
        }
    }

    public String sendDataAndGetResult () {
        String openApiURL = "http://aiopen.etri.re.kr:8000/WiseASR/Recognition";
        String accessKey = editID.getText().toString().trim();
        String languageCode;
        String audioContents;

        Gson gson = new Gson();

       /* switch (curMode) {
            case "한국어인식":
                languageCode = "korean";
                break;
            case "영어인식":
                languageCode = "english";
                break;
            case "영어발음평가":
                languageCode = "english";
                openApiURL = "http://aiopen.etri.re.kr:8000/WiseASR/Pronunciation";
                break;
            default:
                return "ERROR: invalid mode";
        }

        */

        languageCode = "english";
        openApiURL = "http://aiopen.etri.re.kr:8000/WiseASR/Pronunciation";

        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();

        audioContents = Base64.encodeToString(
                speechData, 0, lenSpeech*2, Base64.NO_WRAP);

        argument.put("language_code", languageCode);
        argument.put("audio", audioContents);

        request.put("access_key", accessKey);
        request.put("argument", argument);

        URL url;
        Integer responseCode;
        String responBody;
        try {
            url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(gson.toJson(request).getBytes("UTF-8"));
            wr.flush();
            wr.close();

            responseCode = con.getResponseCode();
            if ( responseCode == 200 ) {
                InputStream is = new BufferedInputStream(con.getInputStream());
                responBody = readStream(is);
                return responBody;
            }
            else
                return "ERROR: " + Integer.toString(responseCode);
        } catch (Throwable t) {
            return "ERROR: " + t.toString();
        }
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            Set<String> a=new HashSet<>();
            a.add("female");//here you can give male if you want to select male voice.
            Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale("en","US"),400,200,true,a);
            tts.setLanguage(Locale.ENGLISH);
            tts.setVoice(v);
            tts.setSpeechRate(0.8f);
        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }
}
