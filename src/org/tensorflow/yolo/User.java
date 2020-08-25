package org.tensorflow.yolo;

public class User {
    private String uid;
    private String upw;
    private String nickname;
    private int p_progress;

    public int getBig_progress() {
        return big_progress;
    }

    public void setBig_progress(int big_progress) {
        this.big_progress = big_progress;
    }

    private int big_progress;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUpw() {
        return upw;
    }

    public void setUpw(String upw) {
        this.upw = upw;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getP_progress() {
        return p_progress;
    }

    public void setP_progress(int p_progress) {
        this.p_progress = p_progress;
    }
}
