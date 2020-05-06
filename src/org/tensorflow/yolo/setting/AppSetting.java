package org.tensorflow.yolo.setting;

import org.tensorflow.yolo.model.BoxPosition;
import org.tensorflow.yolo.model.Recognition;

public class AppSetting {
    // 백지연
    // 박스 포지션
    public static float box_left;
    public static float box_top;
    public static float box_right;
    public static float box_bottom;
    public static Recognition object;
    public static int dialog_flag=0;
    public static int viewWidth;
    public static int viewHeight;
}
