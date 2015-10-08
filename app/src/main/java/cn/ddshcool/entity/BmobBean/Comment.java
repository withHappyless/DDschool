package cn.ddshcool.entity.BmobBean;

import cn.bmob.v3.BmobObject;

/**
 *
 * Created by yosemite on 15/9/10.
 */
public class Comment extends BmobObject {

    private String content;
    private User author;
    private Post post;
    private Comment reComment;
    private Boolean isVisible;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Comment getReComment() {
        return reComment;
    }

    public void setReComment(Comment comment) {
        this.reComment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }
}
