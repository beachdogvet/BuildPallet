package com.lockheedmartin.buildpallet;

public class SpinnerItem {

    public String value;
    public int valueId;


    public SpinnerItem(int _id, String _value)
    {
        this.value = _value;
        this.valueId = _id;
    }

    @Override
    public String toString() {
        return value;
    }
}
