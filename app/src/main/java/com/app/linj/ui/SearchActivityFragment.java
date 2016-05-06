package com.app.linj.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.app.core.base.RefreshBaseFragment;
import com.app.core.utils.FootUpdate;
import com.app.linj.R;
import com.app.linj.bean.Activity;
import com.app.linj.bean.UserB;
import com.app.linj.util.HttpUtil;
import com.app.linj.util.JsonTools;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 活动搜索fragment
 * Created by zhangshenglan on 16/4/13.
 */
public class SearchActivityFragment extends RefreshBaseFragment implements FootUpdate.LoadMore {
    @Bind(R.id.listView)
    JazzyListView listView;

    private int count = 1;
    private List<Activity> activities = new ArrayList<>();


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
        listView.setAdapter(new SearchActicityAdapter());
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
                listView.setAdapter(new SearchActicityAdapter());
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

    protected class SearchActicityAdapter extends BaseAdapter {

        private int index = 0;

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int position) {
            return "ss";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.fragment_search_activity, null);
            final EditText editText = (EditText) convertView.findViewById(R.id.search_et_input);
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = position;
                    }
                    return false;
                }
            });
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setHint("");
                }
            });
            editText.clearFocus();
            if (index != -1 && index == position) {
                // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
                editText.requestFocus();
            }
            editText.setSelection(editText.getText().length());

            final EditText search_et_input = (EditText) convertView.findViewById(R.id.search_et_input);
            final Button button1 = (Button) convertView.findViewById(R.id.la);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search_et_input.setText(button1.getText());
                }
            });

            final Button button2 = (Button) convertView.findViewById(R.id.li);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search_et_input.setText(button2.getText());
                }
            });
            final Button button3 = (Button) convertView.findViewById(R.id.dao);
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search_et_input.setText(button3.getText());
                }
            });

            final Button button4 = (Button) convertView.findViewById(R.id.shang);
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search_et_input.setText(button4.getText());
                }
            });

            Button search_btn_back = (Button) convertView.findViewById(R.id.search_btn_back);
            search_btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //http请求获取服务端数据
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                            .detectDiskReads().detectDiskWrites().detectNetwork()
                            .penaltyLog().build());
                    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                            .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                            .penaltyLog().penaltyDeath().build());
                    String keyword = search_et_input.getText().toString();
                    try {


                        JSONObject jsonObject = sendPostResquest(keyword);

                        String rows = jsonObject.get("rows").toString();
                        //把字符串里面的[]截取掉
                        rows = rows.substring(1, rows.length() - 1);
                        //rows里有多个对象时要分隔
                        String[] rowStrs = rows.split("\\}" + ",");
                        for (String r : rowStrs) {
                            Activity activity = new Activity();
                            //json格式把后面一个大括号补上
                            if (!r.contains("}")) {
                                r = r + "}";
                            }
                            JSONObject rowsJson = new JSONObject(r);
                            activity = JsonTools.convertActivity(rowsJson);
                            activities.add(activity);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction tx = fm.beginTransaction();
                    ActivityFragment activity = new ActivityFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("activities", (ArrayList<Activity>) activities);
                    activity.setArguments(bundle);
                    tx.replace(R.id.container, activity, "Three");
                    tx.addToBackStack(null);
                    tx.commit();
                }
            });
            return convertView;
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

    public static JSONObject sendPostResquest(String keyword) {
        JSONObject result = null;
        String strResult;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();        //创建一个默认的HTTP客户端
            HttpPost httpPost = new HttpPost();
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setURI(new URI("http://10.0.2.2:8080/activity/list.do"));
            //httpPost,通过urlParam添加请求参数
            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();

            //中文内容，通过URLDecoder.decode转码
            urlParam.add(new BasicNameValuePair("keyword", URLDecoder.decode(keyword, "utf-8")));
            httpPost.setEntity(new UrlEncodedFormEntity(urlParam, HTTP.UTF_8));


            HttpResponse httpResponse = httpClient.execute(httpPost);               //执行GET方式的HTTP请求
            int reponseCode = httpResponse.getStatusLine().getStatusCode();        //获得服务器的响应码
            if (reponseCode == HttpStatus.SC_OK) {
                strResult = EntityUtils.toString(httpResponse.getEntity());
                result = new JSONObject(strResult);

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
