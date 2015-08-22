package cn.ddshcool.entity.BmobBean;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 对应后台 post_list表
 * ctrl + o  重写父类方法
 */
public class post_list extends BmobObject{

    private String content;
    private ArrayList<String> images;
    private Boolean isVisible;
    private User author;
    private BmobRelation likes;

    public User getAuthor() {
        return author;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getcontent() {
        return content;
    }

    public void setcontent(String content) {
        this.content = content;
    }


    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }
}
