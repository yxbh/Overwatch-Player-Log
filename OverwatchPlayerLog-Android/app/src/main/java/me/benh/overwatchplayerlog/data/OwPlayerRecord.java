package me.benh.overwatchplayerlog.data;

import java.sql.Date;
import java.util.List;

import me.benh.overwatchplayerlog.helpers.UuidHelper;

/**
 * Created by benhuang on 22/11/16.
 */

public class OwPlayerRecord {
    private String id;

    private String battleTag;
    private String region;
    private String platform;

    private Date recordCreateDatetime;
    private Date recordLastUpdateDatetime;

    enum Rating {
        Dislike,
        Undecided,
        Like
    }

    private Rating rating;

    public OwPlayerRecord() {
        this.id = UuidHelper.NewUuidString();
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

    public static List<OwPlayerRecord> GetAll() {
        // TODO: implement
        return null;
    }

    public static OwPlayerRecord GetById(String id) {
        // TODO: implement
        return null;
    }

    public static boolean HasId(String id) {
        // TODO: implement
        return false;
    }

}
