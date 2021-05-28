package com.iflytek.speech.engines.processor.aitalk.recognizer.abstracts;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.LinkedList;

public class Slot implements Parcelable {
    public static final Parcelable.Creator<Slot> CREATOR = new Parcelable.Creator<Slot>() {
        public Slot createFromParcel(Parcel in) {
            return new Slot(in);
        }

        public Slot[] newArray(int size) {
            return new Slot[size];
        }
    };
    public final int mConfidence;
    public final int mItemCount;
    public final int[] mItemIds;
    public final String[] mItemTexts;
    public LinkedList<String> mItemTextsList;
    public final String mName;
    public final int mType;

    public Slot(String slotName, int type, int itemCount, int[] itemIds, String[] itemTexts, LinkedList<String> itemTextsList, int confidence) {
        this.mItemCount = itemCount;
        this.mItemIds = itemIds;
        this.mItemTexts = itemTexts;
        this.mItemTextsList = itemTextsList;
        this.mName = slotName;
        this.mType = type;
        this.mConfidence = confidence;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeInt(this.mType);
        dest.writeInt(this.mItemCount);
        dest.writeIntArray(this.mItemIds);
        dest.writeStringArray(this.mItemTexts);
        dest.writeList(this.mItemTextsList);
        dest.writeInt(this.mConfidence);
    }

    public Slot(Parcel in) {
        this.mName = in.readString();
        this.mType = in.readInt();
        this.mItemCount = in.readInt();
        this.mItemIds = new int[this.mItemCount];
        this.mItemTexts = new String[this.mItemCount];
        in.readIntArray(this.mItemIds);
        in.readStringArray(this.mItemTexts);
        in.readList(this.mItemTextsList, (ClassLoader) null);
        this.mConfidence = in.readInt();
    }
}
