package org.tensorflow.yolo;

public class Bookword {

    int bw_id;
    int b_id;
    String b_word_k;
    String b_word_e;

    public Bookword(int bw_id, int b_id, String b_word_e, String b_word_k){
        this.bw_id=bw_id;
        this.b_id=b_id;
        this.b_word_e=b_word_e;
        this.b_word_k=b_word_k;
    }

    public int getBw_id() {
        return bw_id;
    }

    public void setBw_id(int bw_id) {
        this.bw_id = bw_id;
    }

    public int getB_id() {
        return b_id;
    }

    public void setB_id(int b_id) {
        this.b_id = b_id;
    }

    public String getB_word_k() {
        return b_word_k;
    }

    public void setB_word_k(String b_word_k) {
        this.b_word_k = b_word_k;
    }

    public String getB_word_e() {
        return b_word_e;
    }

    public void setB_word_e(String b_word_e) {
        this.b_word_e = b_word_e;
    }
}
