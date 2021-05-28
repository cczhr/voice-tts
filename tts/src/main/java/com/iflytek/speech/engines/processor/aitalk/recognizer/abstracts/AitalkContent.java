package com.iflytek.speech.engines.processor.aitalk.recognizer.abstracts;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AitalkContent implements Parcelable {
    public static final Parcelable.Creator<AitalkContent> CREATOR = new Parcelable.Creator<AitalkContent>() {
        public AitalkContent createFromParcel(Parcel in) {
            return new AitalkContent(in);
        }

        public AitalkContent[] newArray(int size) {
            return new AitalkContent[size];
        }
    };
    public static final String LABEL = "Aitalk";
    private final String TAG = "AitalkResult";
    public final int mConfidence;
    public final int mSentenceId;
    public final int mSlot;
    public final List<Slot> mSlotList;

    public AitalkContent(Parcel in) {
        this.mSentenceId = in.readInt();
        this.mConfidence = in.readInt();
        this.mSlot = in.readInt();
        this.mSlotList = new ArrayList();
        in.readList(this.mSlotList, Slot.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mSentenceId);
        out.writeInt(this.mConfidence);
        out.writeInt(this.mSlot);
        out.writeList(this.mSlotList);
    }

    public AitalkContent(int sentendId, int confidence, int slots) {
        this.mSentenceId = sentendId;
        this.mConfidence = confidence;
        this.mSlot = slots;
        this.mSlotList = new ArrayList();
    }

    public void AddSlot(String slotName, int slotType, int itemCount, int[] itemIds, String[] itemTexts, LinkedList<String> itemTestsList, int confidence) {
        this.mSlotList.add(new Slot(slotName, slotType, itemCount, itemIds, itemTexts, itemTestsList, confidence));
    }

    public String toString() {
        return "[mSentenceId=" + this.mSentenceId + ", mConfidence=" + this.mConfidence + ", mSlot=" + this.mSlot + ", mSlotList=" + this.mSlotList.size() + "]";
    }
}
