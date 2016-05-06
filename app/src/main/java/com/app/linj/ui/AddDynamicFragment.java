package com.app.linj.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.core.base.RefreshBaseFragment;
import com.app.core.utils.FootUpdate;
import com.app.linj.R;
import com.app.linj.util.Bimp;
import com.app.linj.util.FileUtils;
import com.app.linj.util.ImageItem;
import com.app.linj.util.PublicWay;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangshenglan on 16/4/21.
 */
public class AddDynamicFragment extends RefreshBaseFragment implements FootUpdate.LoadMore {

    @Bind(R.id.listView)
    JazzyListView listView;

    private int count = 1;

    private AddDynamicFragmentAdapter adapter;
    private Uri photoUri ; //得到清晰的图片


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initRefreshLayout();
        listView.setTransitionEffect(new UpSlideInEffect());
        listView.setAdapter(new AddDynamicFragmentAdapter());
        mFootUpdate.init(listView, mInflater, this);
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = super.onOptionsItemSelected(item);
        if (handled) {
            return true;
        }
        int itemId_ = item.getItemId();
        if (itemId_ == R.id.action_search) {
            return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                count = 1;
                listView.setAdapter(new AddDynamicFragmentAdapter());
                setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void loadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                count += 10;
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        }, 2000);
    }

    private class AddDynamicFragmentAdapter extends BaseAdapter {
        private int index = 0;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }
        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 9) {
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
//                       ViewHolder holder = null;
//            if (convertView == null) {
//                convertView = mInflater.inflate(R.layout.item_published_grida,
//                        parent, false);
//                holder = new ViewHolder();
//                holder.image = (ImageView) convertView
//                        .findViewById(R.id.item_grida_image);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            if (position == Bimp.tempSelectBitmap.size()) {
//                holder.image.setImageBitmap(BitmapFactory.decodeResource(
//                        getResources(), R.drawable.icon_addpic_unfocused));
//                if (position == 9) {
//                    holder.image.setVisibility(View.GONE);
//                }
//            } else {
//                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
//            }

            convertView = mInflater.inflate(R.layout.activity_selectimg, null);
            final EditText editText = (EditText)convertView.findViewById(R.id.word_message);
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = position;
                    }
                    return false;
                }
            });
//            editText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    editText.setHint("");
//                }
//            });
            editText.clearFocus();
            if (index != -1 && index == position) {
                // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
                editText.requestFocus();
            }
            editText.setSelection(editText .getText().length());
            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

//    protected void onRestart() {
//        adapter.update();
//        super.onRestart();
//    }

    private static final int TAKE_PICTURE = 0x000001;

    public void photo() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            //Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            ContentValues values = new ContentValues();
            // TODO 待考证
            photoUri = this.getActivity().getApplicationContext().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            //准备intent，并 指定 新 照片 的文件名（photoUri）
            System.out.println("-------------------------" + photoUri.toString());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            //启动拍照的窗体。并注册 回调处理。
            startActivityForResult(intent, TAKE_PICTURE);
            // startActivityForResult(openCameraIntent, TAKE_PICTURE);
        } else {
            Toast.makeText(this.getActivity(), "没有内存卡不能拍照", Toast.LENGTH_LONG).show();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                // TODO 待考证
                ContentResolver cr = this.getActivity().getApplicationContext().getContentResolver();

                Cursor cursor = cr.query(photoUri, null, null, null, null);
                Bitmap bm = null;
                if (cursor != null) {
                    if (cursor.moveToNext()) {
                        String path = cursor.getString(1);
                        //String path = photoUri.toString();
                        //System.out.println("+++++++++++++++++++++++++="+path);
					/*BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					bm = BitmapFactory.decodeFile(path, options);
					int xScale ;
					if(options.outWidth>options.outHeight){
						 xScale = options.outHeight / 160;
					}else{
						xScale = options.outWidth / 160;
					}
					options.inSampleSize = xScale;
					options.inJustDecodeBounds = false;*/
                        bm = BitmapFactory.decodeFile(path);
                    }
                }
                cursor.close();
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == getActivity().RESULT_OK) {
                    System.out.println("添加照片");
                    String fileName = String.valueOf(System.currentTimeMillis());
                    //Bitmap bm = (Bitmap) data.getExtras().get("data");
                    //FileUtils.saveBitmap(bm, fileName);

                    FileUtils.saveBitmap(bm, fileName);
                    //为了得到照片的路径
                    String SDPATH = Environment.getExternalStorageDirectory()
                            + "/Photo_LJ/";
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    takePhoto.setImagePath(SDPATH + fileName + ".JPEG");
                    takePhoto.setImageName(fileName + ".JPEG");
                    Bimp.tempSelectBitmap.add(takePhoto);
                    System.out.println("里面有多少张图片" + Bimp.tempSelectBitmap.size());
                }
                bm = null;
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            for (int i = 0; i < PublicWay.activityList.size(); i++) {
                if (null != PublicWay.activityList.get(i)) {
                    PublicWay.activityList.get(i).finish();
                }
            }
            System.exit(0);
        }
        return true;
    }
}
// listview 向上滑才有动画
class UpSlideInEffect extends SlideInEffect {
    @Override
    public void initView(View item, int position, int scrollDirection) {
        if (scrollDirection > 0) {
            super.initView(item, position, scrollDirection);
        }
    }

    @Override
    public void setupAnimation(View item, int position, int scrollDirection, ViewPropertyAnimator animator) {
        if (scrollDirection > 0) {
            super.setupAnimation(item, position, scrollDirection, animator);
        }
    }
}



