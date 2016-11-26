package me.benh.overwatchplayerlog.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.sql.Date;

/**
 * Created by benhuang on 26/11/16.
 */

public class OwPlayerRecordWrapper implements Parcelable {

    OwPlayerRecord record;

    public OwPlayerRecordWrapper(@NonNull OwPlayerRecord record) {
        this.record = record;
    }

    public OwPlayerRecordWrapper(@NonNull Parcel source) {
        record = new OwPlayerRecord();

        record.setId(source.readString());
        record.setBattleTag(source.readString());
        record.setPlatform(source.readString());
        record.setRegion(source.readString());
        record.setRating(OwPlayerRecord.valueToRating(source.readInt()));
        record.setNote(source.readString());
        record.setRecordCreateDatetime(new Date(source.readLong()));
        record.setRecordLastUpdateDatetime(new Date(source.readLong()));
    }

    public OwPlayerRecord getRecord() {
        return record;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(record.getId());
        parcel.writeString(record.getBattleTag());
        parcel.writeString(record.getPlatform());
        parcel.writeString(record.getRegion());
        parcel.writeInt(record.getRating().getValue());
        parcel.writeString(record.getNote());
        parcel.writeLong(record.getRecordCreateDatetime().getTime());
        parcel.writeLong(record.getRecordLastUpdateDatetime().getTime());
    }

    public static final Creator<OwPlayerRecordWrapper> CREATOR = new Creator<OwPlayerRecordWrapper>() {
        @Override
        public OwPlayerRecordWrapper[] newArray(int size) {
            return new OwPlayerRecordWrapper[size];
        }

        @Override
        public OwPlayerRecordWrapper createFromParcel(Parcel source) {
            return new OwPlayerRecordWrapper(source);
        }
    };
}
