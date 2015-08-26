package cn.ddshcool.entity.BmobBean;

import cn.bmob.im.bean.BmobChatUser;

/**
 * 用户类
 */
public class User extends BmobChatUser {

    private Boolean userSex;       //true是女
    private Integer userExperience;
    private String userSchool;
    private String userDept;
    private String userSpeak;
    private String whatTime;
    private Integer likeCount;

    public String getWhatTime() {
        return whatTime;
    }

    public void setWhatTime(String whatTime) {
        this.whatTime = whatTime;
    }

    public String getUserDept() {
        return userDept;
    }

    public void setUserDept(String userDept) {
        this.userDept = userDept;
    }

    public Integer getUserExperience() {
        return userExperience;
    }

    public void setUserExperience(Integer userExperience) {
        this.userExperience = userExperience;
    }

    public String getUserSchool() {
        return userSchool;
    }

    public void setUserSchool(String userSchool) {
        this.userSchool = userSchool;
    }

    public Boolean getUserSex() {
        return userSex;
    }

    public void setUserSex(Boolean userSex) {
        this.userSex = userSex;
    }

    public String getUserSpeak() {
        return userSpeak;
    }

    public void setUserSpeak(String userSpeak) {
        this.userSpeak = userSpeak;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}
