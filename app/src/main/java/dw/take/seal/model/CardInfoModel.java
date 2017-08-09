package dw.take.seal.model;

import java.io.Serializable;

/**
 * 身份证信息
 * Created by Administrator on 2017/8/1.
 */

public class CardInfoModel implements Serializable {
    private int id;
    private String IdentyNumber;
    private String PersonName;
    private String PersonNation;
    private String PersonAddress;
    private String PersonFaceImage;
    private String isFaren;//true法人  false 经办人
    private String PersonBaseImg;//身份证照片

    public String getPersonBaseImg() {
        return PersonBaseImg;
    }

    public void setPersonBaseImg(String personBaseImg) {
        PersonBaseImg = personBaseImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsFaren() {
        return isFaren;
    }

    public void setIsFaren(String isFaren) {
        this.isFaren = isFaren;
    }

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
