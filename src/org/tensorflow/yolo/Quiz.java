package org.tensorflow.yolo;

public class Quiz {
    int q_id;
    int b_id;
    String q_word;
    String q_sentence_k;
    String q_sentence_e;

    public Quiz(int q_id, int b_id, String q_word, String q_sentence_e, String q_sentence_k){
        this.q_id=q_id;
        this.b_id=b_id;
        this.q_word=q_word;
        this.q_sentence_e=q_sentence_e;
        this.q_sentence_k=q_sentence_k;
    }

    public int getQ_id() {
        return q_id;
    }

    public void setQ_id(int q_id) {
        this.q_id = q_id;
    }

    public int getB_id() {
        return b_id;
    }

    public void setB_id(int b_id) {
        this.b_id = b_id;
    }

    public String getQ_word() {
        return q_word;
    }

    public void setQ_word(String q_word) {
        this.q_word = q_word;
    }

    public String getQ_sentence_k() {
        return q_sentence_k;
    }

    public void setQ_sentence_k(String q_sentence_k) {
        this.q_sentence_k = q_sentence_k;
    }

    public String getQ_sentence_e() {
        return q_sentence_e;
    }

    public void setQ_sentence_e(String q_sentence_e) {
        this.q_sentence_e = q_sentence_e;
    }
}
