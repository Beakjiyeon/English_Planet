package org.tensorflow.yolo;

public class Camera {
    // 카메라 단어 pk추가
    private int c_id;

    private String c_url;
    private String c_word_e;

    public int getC_id(){
        return c_id;
    }
    public void setC_id(int c_id){
        this.c_id=c_id;
    }

    public String getC_url() {
        return c_url;
    }

    public void setC_url(String c_url) {
        this.c_url = c_url;
    }

    public String getC_word_e() {
        return c_word_e;
    }

    public void setC_word_e(String c_word_e) {
        this.c_word_e = c_word_e;
    }

    public String getC_word_k() {
        return c_word_k;
    }

    public void setC_word_k(String c_word_k) {
        this.c_word_k = c_word_k;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String c_word_k;
    private String uid;
}
