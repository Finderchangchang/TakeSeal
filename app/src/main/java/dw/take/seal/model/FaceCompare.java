package dw.take.seal.model;

import java.io.Serializable;

/**
 * 人像比对结果
 * Created by Administrator on 2017/8/2.
 */

public class FaceCompare implements Serializable{
    private boolean SamePerson;//比对是否成功
    private double FaceScore;//相似度

    public boolean isSamePerson() {
        return SamePerson;
    }

    public void setSamePerson(boolean samePerson) {
        SamePerson = samePerson;
    }

    public double getFaceScore() {
        return FaceScore;
    }

    public void setFaceScore(double faceScore) {
        FaceScore = faceScore;
    }
}
