package com.wheels2spin.enduser.parse;

import android.location.Address;
import android.location.Location;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("BikeRentalServiceOwner")
public class BikeRentalServiceOwner extends ParseUser {

    public Address getAddress() {
        return (Address) get("address");
    }

    public void setAddress(final Address address) {
        put("address", address);
    }

    public Location getLocation() {
        return (Location) get("location");
    }

    public void setLocation(final Location location) {
        put("location", location);
    }

    public List<ParseObject> getBikes() {
        return getList("bikes");
    }

}