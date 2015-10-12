package cn.ddshcool.services.adapters;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.ddshcool.entity.BmobBean.Post;
import cn.ddshcool.entity.BmobBean.User;
import cn.ddshcool.main.R;
import cn.ddshcool.utils.StringUtils;
import cn.ddshcool.utils.TimeUtil;

/**
 * ！！！！！
 * 点赞那里有点问题 呵呵哒
 * 异步的问题真是恶心到家啊 ！！
 * ！！！！！
 *
 * 缩略图的问题没有解决
 *
 */
public class PostsAdapter extends BaseAdapter {

    private Context context;
    private List<Post> datas;
    private ImageLoader imageLoader;
    private List<User> likedUser;
    private boolean isLiked;

    public PostsAdapter(Context context, List<Post> datas) {
        this.context = context;
        this.datas = datas;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Post getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_post, null);
            holder.ll_card_content = (LinearLayout) convertView
                    .findViewById(R.id.ll_card_content);
            holder.iv_avatar = (ImageView) convertView
                    .findViewById(R.id.iv_avatar);
            holder.iv_avatar_sex = (ImageView) convertView
                    .findViewById(R.id.iv_avatar_sex);
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
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // bind data
        final Post post = getItem(position);
        final User user = post.getAuthor();

        //要判断用户是否已经注销   注销显示特定头像 以及 修改昵称的其他操作
//        if(user != null){

        //判断匿名  ----》图像判断没改回来
        if (!post.getIsVisible()) {
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



//        }

        //加载发帖时间
        holder.tv_caption.setText(TimeUtil.getDescriptionTimeFromTimestamp(
                TimeUtil.stringToLong(post.getCreatedAt(), "yyyy-MM-dd HH:mm")));
        //加载性别

        if (user.getUserSex() != null && user.getUserSex()) {        //false是男

            holder.iv_avatar_sex.setImageResource(R.drawable.ic_th_sex_girl);
        }
        //加载等级(预留)

        //加载更多操作按钮 并添加 操作监听器(稍后写)

        //加载文本内容
        holder.tv_content.setText(StringUtils.getPostContent(
                context, holder.tv_content, post.getcontent()));

        //进入个人中心？！
//		holder.iv_avatar.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context, UserInfoActivity.class);
//				intent.putExtra("userName", user.getName());
//				context.startActivity(intent);
//			}
//		});
//
//		holder.tv_subhead.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context, UserInfoActivity.class);
//				intent.putExtra("userName", user.getName());
//				context.startActivity(intent);
//			}
//		});

        setImages(post, holder.include_status_image, holder.gv_images, holder.iv_image);


        //引用部分
//		final Status retweeted_status = status.getRetweeted_status();
//		if(retweeted_status != null) {
//			User retUser = retweeted_status.getUser();
//
//			holder.include_retweeted_status.setVisibility(View.VISIBLE);
//			String retweetedContent = "@" + retUser.getName() + ":"
//					+ retweeted_status.getText();
//			holder.tv_retweeted_content.setText(StringUtils.getWeiboContent(
//					context, holder.tv_retweeted_content, retweetedContent));
//
//			setImages(retweeted_status,
//					holder.include_retweeted_status_image,
//					holder.gv_retweeted_images, holder.iv_retweeted_image);
//		} else {
//			holder.include_retweeted_status.setVisibility(View.GONE);
//		}

        //转发、评论、赞 数量统计 以及 显示
//		holder.tv_share_bottom.setText(status.getReposts_count() == 0 ?
//				"转发" : status.getReposts_count() + "");
//
//		holder.tv_comment_bottom.setText(post.getComments_count() == 0 ?
//				"评论" : status.getComments_count() + "");
//
        holder.iv_like_bottom.setEnabled(false);
        // 查询喜欢这个帖子的所有用户，因此查询的是用户表
        BmobQuery<User> query = new BmobQuery<User>();
        //likes是Post表中的字段，用来存储所有喜欢该帖子的用户
        query.addWhereRelatedTo("likes", new BmobPointer(post));
        query.findObjects(context, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                //显示个数
                if (list.isEmpty() && list.size() > 0) {
                    holder.tv_like_bottom.setText(list.size() + "");
                    likedUser = list;    //给喜欢的人赋值
                } else {
                    holder.tv_like_bottom.setText("点赞");
                }

                holder.iv_like_bottom.setEnabled(true);

            }

            @Override
            public void onError(int i, String s) {

                holder.tv_like_bottom.setText("点赞");
                holder.iv_like_bottom.setEnabled(true);

            }
        });

//		holder.ll_card_content.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context, StatusDetailActivity.class);
//				intent.putExtra("status", status);
//				context.startActivity(intent);
//			}
//		});
//
//		holder.include_retweeted_status.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context, StatusDetailActivity.class);
//				intent.putExtra("status", retweeted_status);
//				context.startActivity(intent);
//			}
//		});
//
//		holder.ll_share_bottom.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context, WriteStatusActivity.class);
//				intent.putExtra("status", status);
//				context.startActivity(intent);
//			}
//		});
//		//评论按钮监听器
//		holder.ll_comment_bottom.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(status.getComments_count() > 0) {
//					Intent intent = new Intent(context, StatusDetailActivity.class);
//					intent.putExtra("status", status);
//					intent.putExtra("scroll2Comment", true);
//					context.startActivity(intent);
//				} else {
//					Intent intent = new Intent(context, WriteCommentActivity.class);
//					intent.putExtra("status", status);
//					context.startActivity(intent);
//				}
//				ToastUtils.showToast(context, "评个论~", Toast.LENGTH_SHORT);
//			}
//		});

        //点赞按钮监听器  ---》有问题
        holder.ll_like_bottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    //取消点赞
                    holder.iv_like_bottom.setImageResource(R.drawable.ic_card_like);
                    if (holder.tv_like_bottom.getText().toString().equals("1")) {

                        holder.tv_like_bottom.setText("点赞");

                    } else {
                        holder.tv_like_bottom.setText("" + (Integer.parseInt(
                                holder.tv_like_bottom.getText().toString()) - 1));
                    }
                    delLike(post);
                } else {
                    //点赞
                    holder.iv_like_bottom.setImageResource(R.drawable.ic_card_liked);
                    if (holder.tv_like_bottom.getText().toString().equals("点赞")) {
                        holder.tv_like_bottom.setText("1");
                    } else {

                        int count = Integer.parseInt(holder.tv_like_bottom.getText().toString()) + 1;
                        holder.tv_like_bottom.setText("" + count);
                    }
                    addLike(post);
                }
                isLiked = !isLiked;
//                addLike(post);
            }

        });

        return convertView;
    }

    private void addLike(Post post) {
        //将用户B添加到多对多关联中
        BmobRelation relation = new BmobRelation();
        User user = BmobUser.getCurrentUser(context,User.class);
        user.increment("likeCount");    //原子更新操作
        user.update(context);

        relation.add(user);
        //多对多关联指向`post`的`likes`字段
        post.setLikes(relation);
        post.update(context, new UpdateListener() {

            @Override
            public void onSuccess() {


            }

            @Override
            public void onFailure(int arg0, String arg1) {


            }
        });
    }

    private void delLike(Post post) {

        BmobRelation relation = new BmobRelation();
        relation.remove(BmobUser.getCurrentUser(context, User.class));
        post.setLikes(relation);
        post.update(context, new UpdateListener() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int arg0, String arg1) {

            }
        });
    }

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

    public static class ViewHolder {

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
