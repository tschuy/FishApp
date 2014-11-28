package com.tschuy.fishapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class Product implements Parcelable {
    public String origin;
    public long story;
    public String description;
    public String variety;
    public String season;
    public Uri image;
    public String alternate_name;
    public long id;
    public String name;

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

        JsonElement image_element = obj.get("image");
        if (!image_element.isJsonNull()) {
            image = Uri.parse(image_element.getAsString());
        }
        else {
            image = null;
        }
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
        out.writeString(image.toString());
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
        image = Uri.parse(in.readString());
        description = in.readString();
    }
}