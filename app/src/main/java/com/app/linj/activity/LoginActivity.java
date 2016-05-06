package com.app.linj.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.core.base.BaseActivity;
import com.app.linj.Config;
import com.app.linj.R;
import com.app.linj.bean.UserB;
import com.app.linj.bean.UserBean;
import com.app.linj.ui.MainActivity;
import com.app.linj.util.BmobConstants;
import com.app.linj.util.HttpUtil;
import com.app.linj.util.JsonTools;
import com.app.linj.view.DialogTips;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.BmobChat;
import cn.bmob.v3.BmobInstallation;


/**
 * Created by zhangshenglan on 16/4/29.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    EditText et_username, et_password;
    Button btn_login;
    TextView btn_register;
    private MyBroadcastReceiver receiver = new MyBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BmobChat.DEBUG_MODE = true;
        // BmobIM SDK初始化--只需要这一段代码即可完成初始化
        // 请到Bmob官网(http://www.bmob.cn/)申请ApplicationId,具体地址:http://docs.bmob.cn/android/faststart/index.html?menukey=fast_start&key=start_android
        BmobChat.getInstance(this).init(Config.applicationId);
        // 开启定位
//        initLocClient();
        init();
        // 注册退出广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(BmobConstants.ACTION_REGISTER_SUCCESS_FINISH);
        registerReceiver(receiver, filter);
        // showNotice();
    }

    public void showNotice() {
        DialogTips dialog = new DialogTips(this, "提示", getResources()
                .getString(R.string.show_notice), "确定", true, true);
        // 设置成功事件
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {

            }
        });
        // 显示确认对话框
        dialog.show();
        dialog = null;
    }

    private void init() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (TextView) findViewById(R.id.btn_register);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LoginActivity.this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_register) {
            Intent intent = new Intent(LoginActivity.this,
                    RegisterActivity.class);
            overridePendingTransition(R.anim.in_anim, R.anim.out_anim);
            startActivity(intent);
        }
            // CustomApplcation.getInstance().logout();s

            login();
        }


    private void login() {
        List<UserB> users = new ArrayList<UserB>();
        final String name = et_username.getText().toString();
        String password = et_password.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ShowToast(R.string.toast_error_username_null);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ShowToast(R.string.toast_error_password_null);
            return;
        }

        //http请求获取服务端数据
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        JSONObject jsonObject = HttpUtil.sendPostResquest("http://10.0.2.2:8080/user/get.do?userName="+name);
        try{
            String rows = jsonObject.get("rows").toString();
            //把字符串里面的[]截取掉
            rows = rows.substring(1,rows.length()-1);
            //rows里有多个对象时要分隔
            String[] rowStrs = rows.split("\\}" + ",");
            for(String r : rowStrs){
                UserB userB = new UserB();
                //json格式把后面一个大括号补上
                if(!r.contains("}")){
                    r = r + "}";
                }
                JSONObject rowsJson = new JSONObject(r);
                userB = JsonTools.convertUserB(rowsJson);
                users.add(userB);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(users.size() == 0){
            ShowToast("用户不存在");
            return;
        }
        if(!password.equals(users.get(0).getPassword())){
            ShowToast("密码不正确");
            return;
        }

        final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("正在登陆...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        UserBean user = new UserBean();
        user.setUsername(name);
        user.setPassword(password);
        user.setDeviceType("android");
        user.setInstallId(BmobInstallation.getInstallationId(this));

                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
