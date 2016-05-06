package com.app.linj.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.linj.Application.CustomApplcation;
import com.app.linj.R;
import com.app.linj.activity.HuifulistActivity;
import com.app.linj.activity.SetMyInfoActivity;
import com.app.linj.bean.Comment;
import com.app.linj.bean.QiangYu;
import com.app.linj.bean.UserBean;
import com.app.linj.util.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobUser;

/**
 * Created by zhangshenglan on 16/5/6.
 */
public class CommentAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private Comment comment;
    private List<Comment> dataList;
    private String louzhu;
    private View contentView;
    private LinearLayout pshouchang;
    private LinearLayout phuifu;
    private ImageView pdelete;
    BmobPushManager bmobPushManager;
    private List<Comment> arg1;
    private QiangYu qiangYu;

    public CommentAdapter(Context context, List<Comment> comments,
                          String louzhu, QiangYu qiangYu) {
        mContext = context;
        dataList = comments;
        mInflater = LayoutInflater.from(mContext);
        this.louzhu = louzhu;
        this.qiangYu = qiangYu;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_commentzhu, null);
            holder.comment_logo = (ImageView) convertView
                    .findViewById(R.id.comment_logo);
            holder.btn_gd = (ImageView) convertView.findViewById(R.id.btn_gd);

            holder.comment_louzhu = (ImageView) convertView
                    .findViewById(R.id.comment_louzhu);
            holder.comment_name = (TextView) convertView
                    .findViewById(R.id.comment_name);
            holder.comment_time = (TextView) convertView
                    .findViewById(R.id.comment_time);
            holder.comment_text = (TextView) convertView
                    .findViewById(R.id.comment_text);
            holder.cposition = (TextView) convertView
                    .findViewById(R.id.cposition);
            holder.cview = (View) convertView.findViewById(R.id.cview);
            holder.diyige = (TextView) convertView.findViewById(R.id.diyige);
            holder.dierge = (TextView) convertView.findViewById(R.id.dierge);
            holder.gengduo = (TextView) convertView.findViewById(R.id.gengduo);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        comment = dataList.get(position);
        final UserBean user = comment.getUser();
        holder.diyige.setVisibility(View.GONE);
        holder.dierge.setVisibility(View.GONE);
        holder.gengduo.setVisibility(View.GONE);
        holder.cview.setVisibility(View.GONE);
        Log.d("CommentActivity", "楼主：" + louzhu);

        if(comment.getUser()!=null && comment.getUser().getUsername()!=null){
            Log.d("CommentActivity","其他："+ comment.getUser().getUsername());
        }

        if(comment.getUser()!= null && comment.getUser().getUsername()!=null){

            if (comment.getUser().getUsername().equals(louzhu)) {
                holder.comment_louzhu.setVisibility(View.VISIBLE);
            } else {
                holder.comment_louzhu.setVisibility(View.GONE);
            }
        }
        String avatarUrl="";
        String a="";
        if(comment.getUser()!=null && comment.getUser().getAvatar()!=null){
            a = comment.getUser().getAvatar();
        }

        if (a.isEmpty()) {
            holder.comment_logo.setImageResource(R.drawable.photo);
        } else {
            if(comment.getUser()!= null && comment.getUser().getAvatar()!=null){
                avatarUrl = comment.getUser().getAvatar();

            }
            if(!avatarUrl.isEmpty()){
                ImageLoader.getInstance()
                        .displayImage(
                                avatarUrl,
                                holder.comment_logo,
                                CustomApplcation.getInstance().getOptions(
                                        R.drawable.photo),
                                new SimpleImageLoadingListener() {

                                    @Override
                                    public void onLoadingComplete(String imageUri,
                                                                  View view, Bitmap loadedImage) {
                                        // TODO Auto-generated method stub
                                        super.onLoadingComplete(imageUri, view,
                                                loadedImage);
                                    }

                                });
            }

        }
        holder.comment_logo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SetMyInfoActivity.class);
                intent.putExtra("userdata", user);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.in_anim,
                        R.anim.out_anim);
            }
        });

        int l = position + 1;


                holder.cposition.setText("第" + l + "楼");
        if(comment.getUser()!=null && comment.getUser().getUsername()!=null){
            final String uidname  = comment.getUser().getUsername();
            holder.comment_name.setText(uidname);
        }
        else {
            final String uidname = "";
            holder.comment_name.setText(uidname);
        }

        final String uidname = holder.comment_name.getText().toString();

        holder.comment_text.setText(comment.getCommentContent()!= null ? comment.getCommentContent() : "");
        if(comment.getCreatedAt()!=null){
            long stringToLong = TimeUtil.stringToLong(comment.getCreatedAt(),
                    "yyyy-MM-dd HH:mm:ss");
            String ztime = TimeUtil.getDescriptionTimeFromTimestamp(stringToLong);
            holder.comment_time.setText(ztime);
        }


        if (!TextUtils.isEmpty(comment.getDiyitext())) {
            holder.cview.setVisibility(View.VISIBLE);
            holder.diyige.setVisibility(View.VISIBLE);
            holder.diyige.setText(comment.getDiyitext());
            if (!TextUtils.isEmpty(comment.getDiertext())) {
                holder.dierge.setText(comment.getDiertext());
                holder.dierge.setVisibility(View.VISIBLE);
                holder.gengduo.setVisibility(View.VISIBLE);
            }
        }

        holder.btn_gd.setOnClickListener(new View.OnClickListener() {

            private Boolean sfdelete;

            @Override
            public void onClick(View v) {
                int l = position;
                UserBean dquser = BmobUser.getCurrentUser(mContext, UserBean.class);

                if(dquser.getUsername()!=null){
                    if(dquser.getUsername().equals(user.getUsername())==true || user.getUsername().equals(louzhu)==true){
                        sfdelete = true;
                    }else {
                        sfdelete = false;
                    }
                }

                showPopupWindow(v, dataList.get(l), uidname, l,sfdelete);
            }
        });

        holder.gengduo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int l = position;
                Intent intent = new Intent(mContext, HuifulistActivity.class);
                intent.putExtra("data", dataList.get(l));
                intent.putExtra("lzname", louzhu);
                intent.putExtra("lzqy", qiangYu);

                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.in_anim,
                        R.anim.out_anim);

            }
        });
        holder.dierge.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int l = position;
                Intent intent = new Intent(mContext, HuifulistActivity.class);
                intent.putExtra("data", dataList.get(l));
                intent.putExtra("lzname", louzhu);
                intent.putExtra("lzqy", qiangYu);

                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.in_anim,
                        R.anim.out_anim);

            }
        });
        holder.diyige.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int l = position;
                Intent intent = new Intent(mContext, HuifulistActivity.class);
                intent.putExtra("data", dataList.get(l));
                intent.putExtra("lzname", louzhu);

                intent.putExtra("lzqy", qiangYu);

                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.in_anim,
                        R.anim.out_anim);

            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView comment_logo;
        TextView comment_name;
        ImageView comment_louzhu;
        TextView comment_time;
        TextView comment_text;
        TextView cposition;

        ImageView btn_gd;

        View cview;
        TextView diyige;
        TextView dierge;
        TextView gengduo;
    }

    private void showPopupWindow(View v, final Comment cdata, final String uid,
                                 final int l, final Boolean sfdelete) {
        contentView = mInflater.inflate(R.layout.popupwlayout, null);
        pshouchang = (LinearLayout) contentView.findViewById(R.id.pshouchang);
        phuifu = (LinearLayout) contentView.findViewById(R.id.phuifu);
        pdelete = (ImageView) contentView.findViewById(R.id.pdelete);
        pshouchang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "收藏", Toast.LENGTH_SHORT).show();
            }
        });

        phuifu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HuifulistActivity.class);
                intent.putExtra("data", cdata);
                intent.putExtra("lzname", louzhu);
                intent.putExtra("lzqy", qiangYu);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.in_anim,
                        R.anim.out_anim);
            }
        });
        pdelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//				if(sfdelete==true){
//
//				Comment cmomdelete = new Comment();
//				cmomdelete.setObjectId(cdata.getObjectId());
//				cmomdelete.delete(mContext, new DeleteListener() {
//
//					@Override
//					public void onSuccess() {
//						dataList.remove(cdata);
//						notifyDataSetChanged();
//					}
//
//					@Override
//					public void onFailure(int arg0, String arg1) {
//
//					}
//				});
//				}else {
                Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
//				}
            }
        });
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.LEFT | Gravity.TOP, 95,
                location[1] - 5);

    }

    public void getlist(Comment comment) {
        dataList.add(comment);
    }

    public void getlistadd(List<Comment> data) {
        dataList.addAll(data);
    }
}

// Toast.makeText(mContext, "回复", Toast.LENGTH_SHORT).show();
// bmobPushManager = new BmobPushManager(mContext);
//
// BmobQuery<BmobInstallation> query =
// BmobInstallation.getQuery();
// // 并添加条件为设备类型属于android
// query.addWhereEqualTo("uid",uid);
// // 设置推送条件给bmobPushManager对象。
// bmobPushManager.setQuery(query);
// // 设置推送消息，服务端会根据上面的查询条件，来进行推送这条消息
// bmobPushManager.pushMessage("hflist");

