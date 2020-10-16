package org.tensorflow.yolo;

import java.io.Serializable;

public class Myword implements Serializable {
    private int m_id;
    private String uid;
    private int b_id;
    private String m_word_k;
    private String m_word_e;
    private int check_ws;


    public Myword(int m_id, String uid, int b_id, String m_word_k, String m_word_e, int check_ws) {
        this.m_id = m_id;
        this.uid = uid;
        this.b_id = b_id;
        this.m_word_k = m_word_k;
        this.m_word_e = m_word_e;
        this.check_ws = check_ws;
    }


    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
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

    public int getCheck_ws() {
        return check_ws;
    }

    public void setCheck_ws(int check_ws) {
        this.check_ws = check_ws;
    }
}
