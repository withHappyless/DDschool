package cn.ddshcool.services.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ddshcool.entity.BmobBean.Comment;
import cn.ddshcool.entity.BmobBean.User;
import cn.ddshcool.main.R;
import cn.ddshcool.utils.TimeUtil;

/**
 * 回复加载适配器
 * Created by yosemite on 15/9/9.
 */
public class CommentAdapter extends BaseAdapter{

    private Context context;
    private List<Comment> comments;
    private ImageLoader imageLoader;

    public CommentAdapter(Context context,List<Comment> comments){

        this.context = context;
        this.comments = comments;
        //实例化imageloader
        imageLoader  = ImageLoader.getInstance();

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        //如果不存在缓存视图
        if(convertView == null){
            //链接视图文件
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_comment,null);
            holder.ll_comment_item_layout = (LinearLayout) convertView.findViewById(R.id.ll_comment_item_layout);
            holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tv_floor = (TextView) convertView.findViewById(R.id.tv_floor);
            holder.iv_avatar_sex = (ImageView) convertView.findViewById(R.id.iv_avatar_sex);
            holder.iv_avatar_level = (ImageView) convertView.findViewById(R.id.iv_avatar_level);
            holder.tv_subhead = (TextView) convertView.findViewById(R.id.tv_subhead);
            holder.tv_caption = (TextView) convertView.findViewById(R.id.tv_caption);
            holder.rl_content = (RelativeLayout) convertView.findViewById(R.id.rl_content);
            holder.tv_comment_content = (TextView) convertView.findViewById(R.id.tv_comment_content);
            //储存视图
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        /**
         * 获取评论主体
         */
        final Comment comment = comments.get(position);
        final User user = comment.getAuthor();
        /**
         * 判断该用户是否存在 如果不存在 则直接跳过 --- 【抽楼】
         */
        if(user != null && comment.getIsVisible()){   // || !user.getIsLock()  ---  判断该用户是否已经被软删除 或 禁言 之类的操作

            /**
             * 头像、昵称相关处理
             */
            //判断匿名  ----  图像判断没改回来
            if (!comment.getIsVisible()) {
                //加载用户头像
                imageLoader.displayImage(user.getAvatar(), holder.iv_avatar);

                //加载用户昵称
                holder.tv_subhead.setText(user.getNick());
            } else {
                //加载用户头像
                imageLoader.displayImage("https://www.baidu.com/img/bd_logo1.png", holder.iv_avatar);

                //加载用户昵称
                holder.tv_subhead.setText("某同学");
            }
            /**
             * 楼层相关处理
             */
            holder.tv_floor.setVisibility(View.VISIBLE);    //显示楼层
            holder.tv_floor.setText(position + 1);          //设置当前楼层
            /**
             * 回复时间处理
             */
            holder.tv_caption.setText(TimeUtil.getDescriptionTimeFromTimestamp(
                    TimeUtil.stringToLong(comment.getCreatedAt(), "yyyy-MM-dd HH:mm")));
            /**
             * 回复文本内容处理
             */
            holder.tv_comment_content.setText(comment.getContent());
            /**
             * 引用回复内容处理 TODO 没有实现所回复用户名点击处理 等待个人信息页面布局完成后处理
             */
            if(comment.getReComment() != null){
                String reUserName;
                if(comment.getReComment().getIsVisible()){

                    reUserName = comment.getReComment().getAuthor().getUsername();

                }else{

                    reUserName = "某同学";

                }
                holder.tv_comment_recontent.setText("回复@"+reUserName+":" + comment.getReComment().getContent());

            }
            /**
             * 点击头像 进入 用户信息界面
             */
//            holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(context,UserInfo.class);
//                    intent.putExtra("UserInfo",comment.getAuthor());
//                    context.startActivity(intent);
//
//                }
//            });
            /**
             * 点击回复布局 弹出操作菜单    TODO 应该在commentListView中设置itemClick事件
             */

        }else {
            //当用户不存在或者评论被删除时 则进行抽楼
            holder.ll_comment_item_layout.setVisibility(View.GONE);

        }

        return convertView;
    }

    public static class ViewHolder {

        public LinearLayout ll_comment_item_layout; //回复整体布局

        public ImageView iv_avatar;                //用户头像框

        public TextView tv_floor;              //用于显示当前楼层
        public RelativeLayout rl_content;        //用户信息布局
        public ImageView iv_avatar_sex;            //性别图片
        public ImageView iv_avatar_level;        //等级图片框
        public TextView tv_subhead;                //昵称
        public TextView tv_caption;                //发布时间

        public TextView tv_comment_content;        //回复布局

        public TextView tv_comment_recontent;      //回复引用布局

    }

}
