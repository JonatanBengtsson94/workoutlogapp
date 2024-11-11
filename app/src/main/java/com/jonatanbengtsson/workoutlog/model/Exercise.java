package com.jonatanbengtsson.workoutlog.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {
    private int id;
    private String name;
    public Exercise(int id, String name) {
        this.id = id;
        this.name = name;
    }

    Exercise(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel source) {
            return new Exercise(source);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public int describeContents() { return 0; }

    public int getId() { return id; }
    public String getName() { return name; }
}
