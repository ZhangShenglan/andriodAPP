package com.app.linj.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.app.core.base.RefreshBaseFragment;
import com.app.core.utils.FootUpdate;
import com.app.linj.R;
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
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangshenglan on 16/4/17.
 */
public class ActivityAddFragment extends RefreshBaseFragment implements FootUpdate.LoadMore,DatePickerDialog.OnDateSetListener   {
    @Bind(R.id.listView)
    JazzyListView listView;

    private int count = 1;
    private Button button_person;
    private Button button_captain;
    private Button time_piker;

    private EditText add_et_title;
    private EditText startPlace_et_input;
    private EditText endPlace_et_input;
    private EditText personNum_et_input;
    private EditText activity_content_et_input;

    private String title;
    private String beginTime;
    private String beginPlace;
    private String endPlace;
    private String totalNum;
    private String captainId;
    private String images;
    private String introduce;


    private Button btn_apply;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_activity, null);
        btn_apply = (Button) view.findViewById(R.id.btn_apply);
        btn_apply.setText("确认添加");
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initRefreshLayout();
        listView.setTransitionEffect(new UpSlideInEffect());
        listView.setAdapter(new ActivityAddAdapter());
        mFootUpdate.init(listView, mInflater, this);

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //http请求获取服务端数据
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads().detectDiskWrites().detectNetwork()
                        .penaltyLog().build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                        .penaltyLog().penaltyDeath().build());
                try {
                    title = add_et_title.getText().toString();
                    beginPlace = startPlace_et_input.getText().toString();
                    endPlace = endPlace_et_input.getText().toString();
                    totalNum = personNum_et_input.getText().toString();
                    introduce = activity_content_et_input.getText().toString();
                    captainId = "1";
                    JSONObject jsonObject = sendPostResquest(title, beginTime, beginPlace, endPlace, totalNum, captainId, "", introduce);
                    if(jsonObject.get("statusCode").equals(200)){
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction tx = fm.beginTransaction();
                        ActivityFragment activity = new ActivityFragment();
                        tx.replace(R.id.container, activity, "Three");
                        tx.commit();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_fragment_maopao, menu);
//        super.onCreateOptionsMenu(menu, inflater);
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
                listView.setAdapter(new ActivityAddAdapter());
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

    protected class ActivityAddAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.fragment_add_activity, null);
            button_person = (Button) convertView.findViewById(R.id.button_person);
            button_captain = (Button)convertView.findViewById(R.id.button_captain);
            time_piker = (Button)convertView.findViewById(R.id.time_piker);
            add_et_title = (EditText)convertView.findViewById(R.id.add_et_title);
            startPlace_et_input = (EditText) convertView.findViewById(R.id.startPlace_et_input);
            endPlace_et_input = (EditText) convertView.findViewById(R.id.endPlace_et_input);
            personNum_et_input = (EditText) convertView.findViewById(R.id.personNum_et_input);
            activity_content_et_input = (EditText)convertView.findViewById(R.id.activity_content_et_input);

            time_piker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerFragemnt();
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

    private void showDatePickerFragemnt(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear = monthOfYear +1;
        time_piker.setText(year + "年" + monthOfYear + "月" + dayOfMonth + "日");
        beginTime = year + "-" + monthOfYear + "-" +dayOfMonth;
    }

    public static JSONObject sendPostResquest(String title,String beginTime,String beginPlace,String endPlace,String totalNum,String captainId,String images,String introduce) {
        JSONObject result = null;
        String strResult;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();        //创建一个默认的HTTP客户端
            HttpPost httpPost = new HttpPost();
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setURI(new URI("http://10.0.2.2:8080/activity/create.do"));
            //httpPost,通过urlParam添加请求参数
            List<NameValuePair> urlParam = new ArrayList<NameValuePair>();

            //中文内容，通过URLDecoder.decode转码
            if(title != null && !"".equals(title)){
                urlParam.add(new BasicNameValuePair("title", URLDecoder.decode(title, "utf-8")));
            }
            if(beginTime != null && !"".equals(beginTime)){
                urlParam.add(new BasicNameValuePair("beginTime", URLDecoder.decode(beginTime, "utf-8")));
            }
            if(beginPlace != null && !"".equals(beginPlace)){
                urlParam.add(new BasicNameValuePair("beginPlace", URLDecoder.decode(beginPlace, "utf-8")));
            }
            if(endPlace != null && !"".equals(endPlace)){
                urlParam.add(new BasicNameValuePair("endPlace", URLDecoder.decode(endPlace, "utf-8")));
            }
            if(totalNum != null&& !"".equals(totalNum)){
                urlParam.add(new BasicNameValuePair("totalNum", totalNum.toString()));
            }
            if(captainId != null&& !"".equals(captainId)){
                urlParam.add(new BasicNameValuePair("captainId", captainId.toString()));
            }
            if(images != null && !"".equals(images)){
                urlParam.add(new BasicNameValuePair("images", URLDecoder.decode(images, "utf-8")));
            }
            if(introduce!= null && !"".equals(introduce)){
                urlParam.add(new BasicNameValuePair("introduce", URLDecoder.decode(introduce, "utf-8")));
            }
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
