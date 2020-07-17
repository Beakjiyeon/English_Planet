package org.tensorflow.yolo.setting;

import android.graphics.Bitmap;

import org.tensorflow.yolo.model.BoxPosition;
import org.tensorflow.yolo.model.Recognition;

public class AppSetting {
    // 백지연
    public static boolean dp_bool=true;
    // 박스 포지션
    public static float box_left;
    public static float box_top;
    public static float box_right;
    public static float box_bottom;
    public static Recognition object;
    public static int dialog_flag=0;
    public static int viewWidth;
    public static int viewHeight;

    public static Bitmap image;
    public static String image_name;
    public static String means;
    public static String uarl;

    // 민주희 : 가입자 정보
    public static String uid;
    public static String upwd;
    public static String unickname;
    public static int progress;
}
