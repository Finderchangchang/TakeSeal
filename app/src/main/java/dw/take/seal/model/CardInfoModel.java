package dw.take.seal.model;

import java.io.Serializable;

/**
 * 身份证信息
 * Created by Administrator on 2017/8/1.
 */

public class CardInfoModel implements Serializable {
    private String IdentyNumber;
    private String PersonName;
    private String PersonNation;
    private String PersonAddress;
    private String PersonFaceImage;

    public String getIdentyNumber() {
        return IdentyNumber;
    }

    public void setIdentyNumber(String identyNumber) {
        IdentyNumber = identyNumber;
    }

    public String getPersonName() {
        return PersonName;
    }

    public void setPersonName(String personName) {
        PersonName = personName;
    }

    public String getPersonNation() {
        return PersonNation;
    }

    public void setPersonNation(String personNation) {
        PersonNation = personNation;
    }

    public String getPersonAddress() {
        return PersonAddress;
    }

    public void setPersonAddress(String personAddress) {
        PersonAddress = personAddress;
    }

    public String getPersonFaceImage() {
        return PersonFaceImage;
    }

    public void setPersonFaceImage(String personFaceImage) {
        PersonFaceImage = personFaceImage;
    }
}
