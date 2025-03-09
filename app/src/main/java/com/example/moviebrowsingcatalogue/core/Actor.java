package com.example.moviebrowsingcatalogue.core;

import android.os.Parcel;
import android.os.Parcelable;

public class Actor implements Parcelable {

    private int id;
    private String name;
    private int age;
    private String gender;
    private String image;

    // Constructor for Actor
    public Actor(int id, String name, int age, String gender, String image) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.image = image;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getImage() { return image; }

    // Constructor for Parcelable
    protected Actor(Parcel in) {
        id = in.readInt();
        name = in.readString();
        age = in.readInt();
        gender = in.readString();
        image = in.readString();
    }

    // Parcelable Creator
    public static final Creator<Actor> CREATOR = new Creator<Actor>() {
        @Override
        public Actor createFromParcel(Parcel in) {
            return new Actor(in);
        }

        @Override
        public Actor[] newArray(int size) {
            return new Actor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(gender);
        dest.writeString(image);
    }
}
