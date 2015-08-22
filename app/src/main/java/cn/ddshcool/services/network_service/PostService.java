package cn.ddshcool.services.network_service;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;

import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.ddshcool.entity.BaseActivity;
import cn.ddshcool.entity.BmobBean.post_list;
import cn.ddshcool.utils.ImageUtils;
import cn.ddshcool.utils.MyApplication;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by yosemite on 15/8/22.
 */
public class PostService {

    private static ArrayList<Uri> myImgUris = new ArrayList<Uri>();
    private static post_list myPostlist = new post_list();

    public static void whatSend(BaseActivity newPostActivity , ArrayList<Uri> imgUris , post_list postlist){

        myImgUris = imgUris;
        myPostlist = postlist;

        //显示加载中...progressDialog
        SweetAlertDialog sendDialog = new SweetAlertDialog(newPostActivity, SweetAlertDialog.PROGRESS_TYPE);
        sendDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sendDialog.setTitleText("上传中,请稍后...");
        sendDialog.setCancelable(false);
        sendDialog.show();

        //判断是否是图片帖子
        if(!imgUris.isEmpty()){
            PostService.sendPost(newPostActivity, postlist, imgUris , sendDialog);
        }else{
            PostService.sendPost(newPostActivity, postlist,sendDialog);
        }
    }

    //发送帖子
    public static void sendPost(final BaseActivity newPostActivity, post_list postlist,final SweetAlertDialog dialog) {


        postlist.save(newPostActivity, new SaveListener() {
            @Override
            public void onSuccess() {
                PostService.showDalog(newPostActivity, dialog, true);
            }

            @Override
            public void onFailure(int i, String s) {
                PostService.showDalog(newPostActivity, dialog, false);
            }
        });
    }

    //发送带图片帖子
    public static void sendPost(final BaseActivity newPostActivity,final post_list postlist, ArrayList<Uri> images ,final SweetAlertDialog dialog) {
        String[] imagePaths = new String[images.size()];

        if(Build.VERSION.SDK_INT > 19){
            //获取图片路径
            for(int i = 0 ;i < images.size();i++){
                imagePaths[i] = (ImageUtils.getImageAbsolutePath19(newPostActivity, images.get(i)));
            }
        }else{
            //获取图片路径
            for(int i = 0 ;i < images.size();i++){
                imagePaths[i] = (ImageUtils.getImageAbsolutePath(newPostActivity, images.get(i)));
            }
        }

        BmobProFile.getInstance(newPostActivity).uploadBatch(imagePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(boolean b, String[] strings, String[] strings1, BmobFile[] bmobFiles) {

                if(b){
                    //存入图片地址
                    ArrayList<String> imagelist = new ArrayList<String>();

                    for (int j = 0; j < bmobFiles.length; j++) {
                        imagelist.add(bmobFiles[j].getFileUrl(newPostActivity));
                    }

                    postlist.setImages(imagelist);
                    //继续添加帖子内容
                    postlist.save(newPostActivity, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            PostService.showDalog(newPostActivity, dialog, true);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            PostService.showDalog(newPostActivity, dialog, false);
                        }
                    });
                }

            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {
                PostService.showDalog(newPostActivity, dialog, false);
            }
        });

    }

    public static void showDalog(final BaseActivity newPostActivity ,final SweetAlertDialog dialog ,boolean isScuess){
        if(isScuess){

            dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            dialog.setContentText("发表成功喽");
            dialog.setTitleText("提示");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                   newPostActivity.finish();
                }
            });

        }else{

            dialog.setTitleText("不好啦");
            dialog.setContentText("上传失败啦!");
            dialog.showCancelButton(true);
            dialog.setCancelText("返回");
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dialog.dismiss();
                }
            });
            dialog.setConfirmText("重试");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    PostService.whatSend( (BaseActivity)MyApplication.getmContext() ,myImgUris , myPostlist);

                }
            });
            dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);

        }
    }

}
