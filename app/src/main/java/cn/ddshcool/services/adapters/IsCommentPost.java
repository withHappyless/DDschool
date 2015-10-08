package cn.ddshcool.services.adapters;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.CountListener;
import cn.ddshcool.entity.BmobBean.Comment;
import cn.ddshcool.entity.BmobBean.Post;
import cn.ddshcool.entity.BmobBean.User;
import cn.ddshcool.main.R;
import cn.ddshcool.utils.StringUtils;
import cn.ddshcool.utils.TimeUtil;

/**
 * 用于给吐槽回复界面提供帖子信息
 */
public class IsCommentPost {

    private Post post;
    private Context context;
    private ImageLoader imageLoader;
    private View.OnClickListener onClickLinstener;
    public IsCommentPost(Context context,Post post){

        this.context = context;
        this.post = post;
        this.imageLoader = ImageLoader.getInstance();

    }

    public View getPostView(View convertView){
        final ViewHolder holder;
        /**
         * 加载视图
         */
        holder = new ViewHolder();
        convertView = View.inflate(context, R.layout.item_post, null);
        holder.ll_card_content = (LinearLayout) convertView
                .findViewById(R.id.ll_card_content);
        holder.iv_avatar = (ImageView) convertView
                .findViewById(R.id.iv_avatar);
        holder.rl_content = (RelativeLayout) convertView
                .findViewById(R.id.rl_content);
        holder.tv_subhead = (TextView) convertView
                .findViewById(R.id.tv_subhead);
        holder.tv_caption = (TextView) convertView
                .findViewById(R.id.tv_caption);

        holder.tv_content = (TextView) convertView
                .findViewById(R.id.tv_content);
        holder.include_status_image = (FrameLayout) convertView
                .findViewById(R.id.include_status_image);
        holder.gv_images = (GridView) holder.include_status_image
                .findViewById(R.id.gv_images);
        holder.iv_image = (ImageView) holder.include_status_image
                .findViewById(R.id.iv_image);
        //   性别
        holder.iv_avatar_sex = (ImageView) convertView.
                findViewById(R.id.iv_avatar_sex);
        //   等级
        holder.iv_avatar_level = (ImageView) convertView
                .findViewById(R.id.iv_avatar_level);

        holder.ll_share_bottom = (LinearLayout) convertView
                .findViewById(R.id.ll_share_bottom);
        holder.iv_share_bottom = (ImageView) convertView
                .findViewById(R.id.iv_share_bottom);
//			holder.tv_share_bottom = (TextView) convertView
//					.findViewById(R.id.tv_share_bottom);			//暂时不加入分享统计功能
        holder.ll_comment_bottom = (LinearLayout) convertView
                .findViewById(R.id.ll_comment_bottom);
        holder.iv_comment_bottom = (ImageView) convertView
                .findViewById(R.id.iv_comment_bottom);
        holder.tv_comment_bottom = (TextView) convertView
                .findViewById(R.id.tv_comment_bottom);
        holder.ll_like_bottom = (LinearLayout) convertView
                .findViewById(R.id.ll_like_bottom);
        holder.iv_like_bottom = (ImageView) convertView
                .findViewById(R.id.iv_like_bottom);
        holder.tv_like_bottom = (TextView) convertView
                .findViewById(R.id.tv_like_bottom);
        convertView.setTag(holder);
        //如果帖子能正常显示
        //TODO 前面帖子的处理逻辑是反的  到时候要修改
        if(!post.getIsVisible()){
            final User user = post.getAuthor();
            //TODO 忘记考虑当用户不存已经被彻底删除是的情况 稍后修改
            //设置基本信息
            holder.tv_subhead.setText(user.getNick());
            holder.tv_caption.setText(TimeUtil.getDescriptionTimeFromTimestamp(
                    TimeUtil.stringToLong(post.getCreatedAt(), "yyyy-MM-dd HH:mm")));
            //// TODO: 15/9/26  性别判断历史遗留问题  待解决
            if(user.getUserSex() != null && user.getUserSex()){
                //如果是女生则更改图片
                holder.iv_avatar_sex.setImageResource(R.drawable.ic_th_sex_girl);
            }

            //TODO 等级显示暂时没有处理
            holder.iv_avatar_level.setImageResource(R.drawable.ic_rating_lv_1);
            holder.tv_content.setText(StringUtils.getPostContent(
                    context, holder.tv_content, post.getcontent()));
            BmobQuery<Comment> query = new BmobQuery<>();
            query.addWhereEqualTo("post", post);
            query.count(context, Comment.class, new CountListener() {
                @Override
                public void onSuccess(int i) {
                    //显示评论总数
                    holder.tv_comment_bottom.setText("" + i);

                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
            BmobQuery<User> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("likes", new BmobRelation(post));
            bmobQuery.count(context, User.class, new CountListener() {
                @Override
                public void onSuccess(int i) {

                    //显示点赞总数
                    holder.tv_like_bottom.setText("" + i);

                }

                @Override
                public void onFailure(int i, String s) {

                }
            });

            setImages(post, holder.include_status_image, holder.gv_images, holder.iv_image);
            /**
             *
             */

            /**
             * 点击头像进入个人信息界面
             */
            holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                }
            });

            /**
             * 点赞业务逻辑实现
             */
            holder.ll_like_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点赞业务逻辑






                }
            });

            holder.ll_comment_bottom.setOnClickListener(onClickLinstener);

            //TODO 没有实现分享功能
//            holder.ll_share_bottom.setOnClickListener(    );
        }else{
            //设置昵称为某同学
            holder.tv_subhead.setText("某同学");
            //TODO 日后应该把图像改为某同学
            holder.iv_avatar.setImageResource(R.drawable.input_error);
            //设置发帖时间
            holder.tv_caption.setText(TimeUtil.getDescriptionTimeFromTimestamp(
                    TimeUtil.stringToLong(post.getCreatedAt(), "yyyy-MM-dd HH:mm")));
            //设置基本信息
            holder.iv_avatar_sex.setImageResource(R.drawable.ic_th_sex_boy);
            holder.iv_avatar_level.setImageResource(R.drawable.ic_rating_lv_1);

            holder.tv_content.setText("该帖子内容违规或已被楼主删除");

        }

        return convertView;
    }

    public void setReCommentLinstener(View.OnClickListener onClickListener){
        this.onClickLinstener = onClickListener;
    }

    //设置图片
    private void setImages(final Post post, FrameLayout imgContainer,
                           GridView gv_images, ImageView iv_image) {

        ArrayList<String> image_urls = post.getImages();    //获取图片地址
        ArrayList<String> thumbnail_images = post.getThumbnail_images();    //获取缩略图地址
        //如果有图片 且 数量大于一
        if (image_urls != null && image_urls.size() > 1) {
            //显示九宫格布局 隐藏 单图imageView
            imgContainer.setVisibility(View.VISIBLE);
            gv_images.setVisibility(View.VISIBLE);
            iv_image.setVisibility(View.GONE);

            //设置gridView适配器 , 并传入 缩略图   thumbnail_images
            StatusGridImgsAdapter gvAdapter = new StatusGridImgsAdapter(context, image_urls);

            gv_images.setAdapter(gvAdapter);

            //转到图片浏览器
//			gv_images.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//					Intent intent = new Intent(context, ImageBrowserActivity.class);
//					intent.putExtra("status", status);
//					intent.putExtra("position", position);
//					context.startActivity(intent);
//				}
//			});
        } else if (thumbnail_images != null && thumbnail_images.size() == 1) {
            //如果只有一张图片 则只显示单图imageView
            imgContainer.setVisibility(View.VISIBLE);
            gv_images.setVisibility(View.GONE);
            iv_image.setVisibility(View.VISIBLE);
            //读取缩略图 thumbnail_images
            imageLoader.displayImage(image_urls.get(0), iv_image);
            //转到图片浏览器 ...
//			iv_image.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Intent intent = new Intent(context, ImageBrowserActivity.class);
//					intent.putExtra("status", status);
//					context.startActivity(intent);
//				}
//			});
        } else {
            //没有图片 不显示 图片布局
            imgContainer.setVisibility(View.GONE);
        }
    }

    public static class ViewHolder{

        public LinearLayout ll_card_content;    //帖子布局
        public ImageView iv_avatar;                //用户头像框

        public ImageView iv_avatar_see_more;    //其他操作按钮
        public RelativeLayout rl_content;        //用户信息布局
        public ImageView iv_avatar_sex;            //性别图片
        public ImageView iv_avatar_level;        //等级图片框
        public TextView tv_subhead;                //昵称
        public TextView tv_caption;                //发布时间

        public TextView tv_content;                //帖子内容
        public FrameLayout include_status_image;//图片显示
        public GridView gv_images;                //多图gridView
        public ImageView iv_image;                //单图imageView

        public LinearLayout ll_share_bottom;    //顶部操作栏布局
        public ImageView iv_share_bottom;        //分享按钮
        //		public TextView tv_share_bottom;		//分享数目		暂时不加入统计分析数据功能
        public LinearLayout ll_comment_bottom;    //评论布局
        public ImageView iv_comment_bottom;        //评论按钮
        public TextView tv_comment_bottom;        //评论数目
        public LinearLayout ll_like_bottom;        //点赞布局
        public ImageView iv_like_bottom;        //点赞按钮
        public TextView tv_like_bottom;            //点赞数目

    }


}
