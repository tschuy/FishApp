package com.tschuy.fishapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class VendorProduct implements Parcelable {
    public String product_name;
    public String preparation_name;
    public long product_id;
    public long preparation_id;

    public VendorProduct(JsonObject obj) {
        product_name = obj.get("name").getAsString();
        preparation_name = obj.get("preparation").getAsString();
        product_id = obj.get("product_id").getAsLong();
        preparation_id = obj.get("preparation_id").getAsLong();
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", product_name, preparation_name);
    }

    public static final Parcelable.Creator<VendorProduct> CREATOR = new Parcelable.Creator<VendorProduct>() {
        public VendorProduct createFromParcel(Parcel in) {
            return new VendorProduct(in);
        }

        public VendorProduct[] newArray(int size) { return new VendorProduct[size]; }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(product_name);
        out.writeString(preparation_name);
        out.writeLong(product_id);
        out.writeLong(preparation_id);
    }

    private VendorProduct(Parcel in) {
        product_name = in.readString();
        preparation_name = in.readString();
        product_id = in.readLong();
        preparation_id = in.readLong();
    }
}


public class Vendor implements Parcelable {
    public String name;
    public long id;
    public String street;
    public String contact_name;
    public LatLng location;
    public String city;
    public String zip;
    public String state;
    public String email;
    public String website;
    public String description;
    public String hours;
    public String phone;
    public String location_description;
    public List<VendorProduct> products;

    public void logDescription() {
        Log.e("VENDOR", name);
        Log.e("VENDOR", street);
        Log.e("VENDOR", description);
        Log.e("VENDOR", Long.toString(id));
    }

    @Override
    public String toString() {
            return this.name;
        }

    public Vendor(JsonObject obj) {
        name = obj.get("name").getAsString();
        street = obj.get("street").getAsString();
        contact_name = obj.get("contact_name").getAsString();
        city = obj.get("city").getAsString();
        zip = obj.get("zip").getAsString();
        state = obj.get("state").getAsString();
        email = obj.get("email").getAsString();
        website = obj.get("website").getAsString();
        description = obj.get("description").getAsString();
        hours = obj.get("hours").getAsString();
        location_description = obj.get("location_description").getAsString();
        id = obj.get("id").getAsLong();
        location = new LatLng(obj.get("lat").getAsDouble(), obj.get("lng").getAsDouble());
        JsonArray products_json = obj.get("products").getAsJsonArray();

        Iterator<JsonElement> products_iterator = products_json.iterator();
        products = new ArrayList<VendorProduct>();
        while(products_iterator.hasNext()) {
            VendorProduct product = new VendorProduct(products_iterator.next().getAsJsonObject());
            products.add(product);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(street);
        out.writeString(contact_name);
        out.writeString(city);
        out.writeString(zip);
        out.writeString(state);
        out.writeString(email);
        out.writeString(website);
        out.writeString(description);
        out.writeString(hours);
        out.writeString(phone);
        out.writeString(location_description);
        out.writeDouble(location.latitude);
        out.writeDouble(location.longitude);
        out.writeLong(id);
        out.writeList(products);
    }

    public static final Parcelable.Creator<Vendor> CREATOR = new Parcelable.Creator<Vendor>() {
        public Vendor createFromParcel(Parcel in) {
            return new Vendor(in);
        }

        public Vendor[] newArray(int size) {
            return new Vendor[size];
        }
    };

    private Vendor(Parcel in) {
        name = in.readString();
        street = in.readString();
        contact_name = in.readString();
        city = in.readString();
        zip = in.readString();
        state = in.readString();
        email = in.readString();
        website = in.readString();
        description = in.readString();
        hours = in.readString();
        phone = in.readString();
        location_description = in.readString();
        location = new LatLng(in.readDouble(), in.readDouble());
        id = in.readLong();
        products = new ArrayList<VendorProduct>();
        in.readList(products, VendorProduct.class.getClassLoader());
    }
}