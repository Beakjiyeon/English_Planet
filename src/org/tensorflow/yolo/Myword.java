package org.tensorflow.yolo;

public class Myword {
    private int m_id;
    private int uid;
    private int b_id;
    private String m_word_k;
    private String m_word_e;


    public Myword(int m_id, int uid, int b_id, String m_word_k, String m_word_e) {
        this.m_id = m_id;
        this.uid = uid;
        this.b_id = b_id;
        this.m_word_k = m_word_k;
        this.m_word_e = m_word_e;
    }


    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getB_id() {
        return b_id;
    }

    public void setB_id(int b_id) {
        this.b_id = b_id;
    }

    public String getM_word_k() {
        return m_word_k;
    }

    public void setM_word_k(String m_word_k) {
        this.m_word_k = m_word_k;
    }

    public String getM_word_e() {
        return m_word_e;
    }

    public void setM_word_e(String m_word_e) {
        this.m_word_e = m_word_e;
    }
}
