package com.app.linj.util;

import com.app.linj.bean.Activity;
import com.app.linj.bean.Captain;
import com.app.linj.bean.UserB;

import org.json.JSONObject;

/**
 * Created by zhangshenglan on 16/4/30.
 */
public class JsonTools {
    public static UserB convertUserB(JSONObject rowsJson){
        UserB userB = new UserB();
        try {
            userB.setId(rowsJson.getLong("id"));
            userB.setName(rowsJson.getString("name"));
            userB.setUserName(rowsJson.getString("userName"));
            userB.setPassword(rowsJson.getString("password"));
            userB.setSchoolId(rowsJson.getLong("schoolId"));
            userB.setGender(rowsJson.getInt("gender"));
            userB.setIntroduce(rowsJson.getString("introduce"));
            userB.setCreateTime(rowsJson.getString("createTime"));
            userB.setAvatar(rowsJson.getString("avatar"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return userB;
    }

    public static Activity convertActivity(JSONObject rowsJson){
        Activity activity = new Activity();
        try {
            activity.setId(rowsJson.getLong("id"));
            activity.setTitle(rowsJson.getString("title"));
            activity.setBeginTime(rowsJson.getString("beginTime"));
            activity.setEndTime(rowsJson.getString("endTime"));
            activity.setBeginPlace(rowsJson.getString("beginPlace"));
            activity.setEndPlace(rowsJson.getString("endPlace"));
            activity.setCaptainId(rowsJson.getLong("captainId"));
            activity.setTotalNum(rowsJson.getInt("totalNum"));
            activity.setApplied(rowsJson.getInt("applied"));
            activity.setLiked(rowsJson.getInt("liked"));
            activity.setActivityType(rowsJson.getString("type"));
            activity.setIntroduce(rowsJson.getString("introduce"));
            activity.setImages(rowsJson.getString("images"));
            activity.setCreatorId(rowsJson.getLong("creatorId"));
            activity.setOrigin(rowsJson.getInt("origin"));
            activity.setCreateTime(rowsJson.getString("createTime"));
            activity.setViewCount(rowsJson.getInt("viewCount"));
            activity.setDays(rowsJson.getLong("days"));
            activity.setTime(rowsJson.getString("time"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return activity;
    }

    public static Captain convertCaptain(JSONObject rowsJson){
        Captain captain = new Captain();
        try {
            captain.setId(rowsJson.getLong("id"));
            captain.setName(rowsJson.getString("name"));
            captain.setAvatar(rowsJson.getString("avatar"));
            captain.setFansNum(rowsJson.getInt("fansNum"));
            captain.setLikedNum(rowsJson.getInt("likedNum"));
            captain.setSchoolId(rowsJson.getLong("schoolId"));
            captain.setGroupId(rowsJson.getLong("groupId"));
            captain.setIntroduce(rowsJson.getString("introduce"));
            captain.setUserId(rowsJson.getLong("userId"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return captain;
    }
}
