package com.app.linj.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zhangshenglan on 16/5/4.
 */
public class Activity implements Serializable,Parcelable {
    private Long id;
    private String title;
    private String beginTime;
    private String endTime;
    private String beginPlace;
    private String endPlace;
    private Long captainId;
    private Integer totalNum;
    private Integer applied;
    private Integer liked;
    private String activityType;   //活动类型 0全部 1一日游 2周末游 3毕业游 4出境游
    private String introduce;
    private String images;
    private Long creatorId;
    private Integer origin;         //活动来源 0全部 1个人 2领队
    private String createTime;
    private String updateTime;

    private Integer viewCount;      //浏览量
    private long days;           //几日行
    private String time;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getBeginPlace() {
        return beginPlace;
    }

    public void setBeginPlace(String beginPlace) {
        this.beginPlace = beginPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public Long getCaptainId() {
        return captainId;
    }

    public void setCaptainId(Long captainId) {
        this.captainId = captainId;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getApplied() {
        return applied;
    }

    public void setApplied(Integer applied) {
        this.applied = applied;
    }

    public Integer getLiked() {
        return liked;
    }

    public void setLiked(Integer liked) {
        this.liked = liked;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(beginTime);
        dest.writeString(endTime);
        dest.writeString(beginPlace);
        dest.writeString(endPlace);
        dest.writeLong(captainId);
        dest.writeInt(totalNum);
        dest.writeInt(applied);
        dest.writeInt(liked);
        dest.writeString(activityType);
        dest.writeString(introduce);
        dest.writeString(images);
        dest.writeLong(creatorId);
        dest.writeInt(origin);
        dest.writeString(updateTime);
        dest.writeInt(viewCount);
        dest.writeLong(days);
        dest.writeString(time);

    }
    public static final Creator<Activity> CREATOR=new Creator<Activity>() {
        @Override
        public Activity createFromParcel(Parcel source) {
            Activity activity = new Activity();
            activity.setId(source.readLong());
            activity.setTitle(source.readString());
            activity.setBeginTime(source.readString());
            activity.setBeginPlace(source.readString());
            activity.setEndPlace(source.readString());
            activity.setCaptainId(source.readLong());
            activity.setTotalNum(source.readInt());
            activity.setApplied(source.readInt());
            activity.setLiked(source.readInt());
            activity.setActivityType(source.readString());
            activity.setIntroduce(source.readString());
            activity.setImages(source.readString());
            activity.setCreatorId(source.readLong());
            activity.setOrigin(source.readInt());
            activity.setUpdateTime(source.readString());
            activity.setViewCount(source.readInt());
            activity.setDays(source.readLong());
            activity.setTime(source.readString());
            return activity;
        }

        @Override
        public Activity[] newArray(int size) {
            return new Activity[0];
        }
    };
}
