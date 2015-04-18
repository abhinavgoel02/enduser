package com.wheels2spin.enduser.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.codec.binary.StringUtils;

/**
 * Created by Abhinav Goel on 4/15/2015.
 */
@ParseClassName("Address")
public class Address extends ParseObject {
    public Address() {}

    public String getShopNumber() {
        return getString("shopNumber");
    }

    public void setShopNumber(String shopNumber) {
        put("shopNumber", shopNumber);
    }

    public String getLocality() {
        return getString("locality");
    }

    public void setLocality(String locality) {
        put("locality", locality);
    }

    public String getArea() {
        return getString("area");
    }

    public void setArea(String area) {
        put("area", area);
    }

    public String getCity() {
        return getString("city");
    }

    public void setCity(String city) {
        put("city", city);
    }

    public String getPinCode() {
        return getString("pinCode");
    }

    public void setPinCode(String pinCode) {
        put("pinCode", pinCode);
    }

    @Override
    public String toString() {
        return String.format("%s, %s\n%s, %s\nPIN: %s",
                getShopNumber(),
                getLocality(),
                getArea(),
                getCity(),
                getPinCode());
    }
}