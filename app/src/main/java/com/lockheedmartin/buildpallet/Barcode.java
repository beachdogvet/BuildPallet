package com.lockheedmartin.buildpallet;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by qvfr34 on 9/19/2014.
 */
public class Barcode implements Parcelable {


    byte[] barcodeData;
    int barcodeType;
    int fromScannerID;

    private String baggageTagLong;
    private String baggageTagRank;
    private String baggageTagFirstName;
    private String baggageTagLastName;
    private String bagggageTagSSN;
    private String baggageTagPocPhone;
    private String bagggeTagShort;
    private Boolean isBaggageTag;






    public Barcode(byte[] barcodeData, int barcodeType, int fromScannerID) {
        this.barcodeData=barcodeData;
        this.barcodeType=barcodeType;
        this.fromScannerID=fromScannerID;
        parseBarcodeText();
    }
    public Barcode(Parcel in) {
        this.barcodeData=in.readString().getBytes();
        this.barcodeType=in.readInt();
        this.fromScannerID=in.readInt();
    }

    public Barcode() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(new String(barcodeData));
        parcel.writeInt(barcodeType);
        parcel.writeInt(fromScannerID);
    }
    public static final Creator<Barcode> CREATOR = new Creator<Barcode>() {

    @Override
    public Barcode createFromParcel(Parcel source) {
        return new Barcode(source);
    }

    @Override
    public Barcode[] newArray(int size) {
        return new Barcode[size];
    }
};


    public void parseBarcodeText() {

        if(barcodeData != null)
        {
            String barcodeText = new String(barcodeData);
            if(barcodeText != null) {
                if(barcodeText.startsWith("~~"))
                {
                    setIsBaggageTag(true);
                    String[] sArray = barcodeText.split("~~");
                    setBaggageTagRank(sArray[1]);
                    setBaggageTagFirstName(sArray[2]);
                    setBaggageTagLastName(sArray[3]);
                    setBagggageTagSSN(sArray[4]);
                    setBaggageTagPocPhone(sArray[6]);
                    setBagggeTagShort(sArray[13]);
                }
                else {
                    setIsBaggageTag(false);
                }
            }
        }

    }

//region Getters and Setters

    public byte[] getBarcodeData() {
        return barcodeData;
    }

    public void setBarcodeData(byte[] barcodeData) {
        this.barcodeData = barcodeData;
    }

    public int getFromScannerID() {
        return fromScannerID;
    }

    public void setFromScannerID(int fromScannerID) {
        this.fromScannerID = fromScannerID;
    }

    public int getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(int barcodeType) {
        this.barcodeType = barcodeType;
    }


    public String getBaggageTagLong() {
        return baggageTagLong;
    }

    public void setBaggageTagLong(String baggageTagLong) {
        this.baggageTagLong = baggageTagLong;
    }

    public String getBaggageTagRank() {
        return baggageTagRank;
    }

    public void setBaggageTagRank(String baggageTagRank) {
        this.baggageTagRank = baggageTagRank;
    }

    public String getBaggageTagFirstName() {
        return baggageTagFirstName;
    }

    public void setBaggageTagFirstName(String baggageTagFirstName) {
        this.baggageTagFirstName = baggageTagFirstName;
    }

    public String getBaggageTagLastName() {
        return baggageTagLastName;
    }

    public void setBaggageTagLastName(String baggageTagLastName) {
        this.baggageTagLastName = baggageTagLastName;
    }

    public String getBagggageTagSSN() {
        return bagggageTagSSN;
    }

    public void setBagggageTagSSN(String bagggageTagSSN) {
        this.bagggageTagSSN = bagggageTagSSN;
    }

    public String getBaggageTagPocPhone() {
        return baggageTagPocPhone;
    }

    public void setBaggageTagPocPhone(String baggageTagPocPhone) {
        this.baggageTagPocPhone = baggageTagPocPhone;
    }

    public String getBagggeTagShort() {
        return bagggeTagShort;
    }

    public void setBagggeTagShort(String bagggeTagShort) {
        this.bagggeTagShort = bagggeTagShort;
    }


    public Boolean getIsBaggageTag() {
        return isBaggageTag;
    }

    public void setIsBaggageTag(Boolean baggageTag) {
        isBaggageTag = baggageTag;
    }

//endregion


}
