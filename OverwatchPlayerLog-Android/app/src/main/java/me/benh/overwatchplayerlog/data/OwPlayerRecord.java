package me.benh.overwatchplayerlog.data;

import java.sql.Date;

/**
 * Created by benhuang on 22/11/16.
 */

public class OwPlayerRecord {
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
}
