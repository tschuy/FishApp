package com.tschuy.fishapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ProductVendor implements Parcelable {
    public String vendor_name;
    public long vendor_id;

    public ProductVendor(JsonObject obj) {
        vendor_name = obj.get("name").getAsString();
        vendor_id = obj.get("id").getAsLong();
    }

    @Override
    public String toString() {
        return String.format("%s", vendor_name);
    }

    public static final Parcelable.Creator<ProductVendor> CREATOR = new Parcelable.Creator<ProductVendor>() {
        public ProductVendor createFromParcel(Parcel in) {
            return new ProductVendor(in);
        }

        public ProductVendor[] newArray(int size) { return new ProductVendor[size]; }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(vendor_name);
        out.writeLong(vendor_id);
    }

    private ProductVendor(Parcel in) {
        vendor_name = in.readString();
        vendor_id = in.readLong();
    }
}

public class Product implements Parcelable {
    public String origin;
    public long story;
    public String description;
    public String variety;
    public String season;
    public String image;
    public String alternate_name;
    public long id;
    public String name;
    public List<ProductVendor> vendors;

    @Override
    public String toString() {
            return this.name;
        }

    public Product(JsonObject obj) {
        name = obj.get("name").getAsString();
        origin = obj.get("origin").getAsString();
        description = obj.get("description").getAsString();
        variety = obj.get("variety").getAsString();
        season = obj.get("season").getAsString();
        alternate_name = obj.get("alt_name").getAsString();

        JsonElement story_element = obj.get("story");
        if (!story_element.isJsonNull()) {
            story = story_element.getAsLong();
        }
        else {
            story = 0;
        }
        id = obj.get("id").getAsLong();

        JsonObject image_element = obj.getAsJsonObject("image");
        if (!image_element.isJsonNull()) {
            image = "http://seagrant-staging-api.osuosl.org" + image_element.get("link").getAsString();
        }
        else {
            image = null;
        }
        vendors = new ArrayList<ProductVendor>();
    }

    public void addVendor(ProductVendor vendor) {
            vendors.add(vendor);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(origin);
        out.writeString(description);
        out.writeString(variety);
        out.writeString(season);
        out.writeString(alternate_name);
        out.writeLong(story);
        out.writeLong(id);
        out.writeString(image);
        out.writeList(vendors);
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    private Product(Parcel in) {
        name = in.readString();
        origin = in.readString();
        description = in.readString();
        variety = in.readString();
        season = in.readString();
        alternate_name = in.readString();
        story = in.readLong();
        id = in.readLong();
        image = in.readString();
        vendors = new ArrayList<ProductVendor>();
        in.readList(vendors, VendorProduct.class.getClassLoader());
    }
}