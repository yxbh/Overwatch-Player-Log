package me.benh.overwatchplayerlog.data;

import java.security.InvalidParameterException;
import java.sql.Date;

import me.benh.lib.helpers.DateTimeHelper;
import me.benh.lib.helpers.UuidHelper;
import me.benh.overwatchplayerlog.helpers.PlayerTagHelper;

/**
 * Created by benhuang on 22/11/16.
 */

public class OwPlayerRecord {
    private String id = UuidHelper.newUuidString();

    private String battleTag;
    private String region;
    private String platform;

    private boolean isFavorite = false;

    private Date recordCreateDatetime = DateTimeHelper.getCurrentSqlDate();
    private Date recordLastUpdateDatetime = DateTimeHelper.getCurrentSqlDate();

    private String note = "";

    public enum Rating {
        Dislike(-1),
        Neutral(0),
        Like(1);

        private final int value;
        private Rating(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    private Rating rating = Rating.Neutral;

    public OwPlayerRecord() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBattleTag() {
        return this.battleTag;
    }

    public void setBattleTag(String battleTag) {
        this.battleTag = battleTag;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Date getRecordCreateDatetime() {
        return recordCreateDatetime;
    }

    public void setRecordCreateDatetime(Date recordCreateDatetime) {
        this.recordCreateDatetime = recordCreateDatetime;
    }

    public Date getRecordLastUpdateDatetime() {
        return recordLastUpdateDatetime;
    }

    public void setRecordLastUpdateDatetime(Date recordLastUpdateDatetime) {
        this.recordLastUpdateDatetime = recordLastUpdateDatetime;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static Rating valueToRating(int ratingValue) {
        if (ratingValue == OwPlayerRecord.Rating.Dislike.getValue()) {
            return OwPlayerRecord.Rating.Dislike;
        } else if (ratingValue == OwPlayerRecord.Rating.Neutral.getValue()) {
            return OwPlayerRecord.Rating.Neutral;
        } else if (ratingValue == OwPlayerRecord.Rating.Like.getValue()) {
            return OwPlayerRecord.Rating.Like;
        }

        throw new InvalidParameterException("Invalid rating value [" + String.valueOf(ratingValue) + "]");
    }

    public boolean isValid() {
        return null != battleTag && !battleTag.isEmpty() &&
                PlayerTagHelper.isValidTag(this) &&
                null != platform && !platform.isEmpty() &&
                null != region && !region.isEmpty() &&
                null != recordCreateDatetime && null != recordLastUpdateDatetime;
    }

    @Override
    public String toString() {
        return "{id[" +getId() +
                "], battleTag[" + getBattleTag() +
                "], platform[" + getPlatform() +
                "], region[" + getRegion() +
                "], note[" + getNote() + "]}";
    }
}
