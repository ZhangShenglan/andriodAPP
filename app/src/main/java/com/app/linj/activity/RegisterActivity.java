package com.app.linj.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.app.core.base.BaseActivity;
import com.app.linj.R;
import com.app.linj.bean.UserB;
import com.app.linj.ui.MainActivity;
import com.app.linj.util.HttpUtil;
import com.app.linj.util.JsonTools;

import org.apache.http.HttpEntity;
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

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.bean.BmobChatUser;

/**
 * Created by zhangshenglan on 16/4/29.
 */
public class RegisterActivity extends BaseActivity {
    Button btn_register;
    EditText et_username,et_school,et_phone,et_introduce,et_password, et_confirmPassword;
    RadioButton gender_female,gender_male;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        et_username = (EditText) findViewById(R.id.et_username);
        et_school = (EditText) findViewById(R.id.et_school);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_introduce = (EditText) findViewById(R.id.et_introduce);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirmPassword = (EditText) findViewById(R.id.et_confirmPassword);
        gender_female = (RadioButton) findViewById(R.id.gender_female);
        gender_male = (RadioButton) findViewById(R.id.gender_male);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                register();
            }
        });
    }

    private void register(){
        List<UserB> users = new ArrayList<UserB>();
        String userName = et_username.getText().toString();
        String schoolName = et_school.getText().toString();
        String phone = et_phone.getText().toString();
        String introduce = et_introduce.getText().toString();
        String password = et_password.getText().toString();
        String pwd_again = et_confirmPassword.getText().toString();
        Integer gender = 0;
        boolean female = gender_female.isChecked();
        boolean male = gender_male.isChecked();

        if (TextUtils.isEmpty(userName)) {
            ShowToast(R.string.toast_error_username_null);
            return;
        }
        if(TextUtils.isEmpty(schoolName)){
            ShowToast("学校不能为空");
            return;
        }
        if(TextUtils.isEmpty(phone)){
            ShowToast("电话不能为空");
            return;
        }
        if(TextUtils.isEmpty(introduce)){
            ShowToast("自我介绍不能为空");
            return;
        }
        if(!(female || male)){
            ShowToast("性别不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ShowToast(R.string.toast_error_password_null);
            return;
        }
        if (!pwd_again.equals(password)) {
            ShowToast(R.string.toast_error_comfirm_password);
            return;
        }
        if(female){
            gender = 1;
        }
        //http请求获取服务端数据
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

        HttpResponse httpResponse = sendPostResquestForRegister("http://10.0.2.2:8080/user/create.do",userName,schoolName,phone,introduce,password,gender);
        int reponseCode = httpResponse.getStatusLine().getStatusCode();        //获得服务器的响应码
        if (reponseCode != HttpStatus.SC_OK) {
            ShowToast("注册失败");
            return;
        }

        final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
        progress.setMessage("正在注册...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        Intent intent = new Intent(RegisterActivity.this,
                MainActivity.class);
        startActivity(intent);
        RegisterActivity.this.finish();
    }

    public static HttpResponse sendPostResquestForRegister(String path, String userName,String schoolName,String phone,String introduce,String password,Integer gender  ) {
        DefaultHttpClient httpClient = new DefaultHttpClient();        //创建一个默认的HTTP客户端
        HttpResponse httpResponse = null;
        try {

            HttpPost httpPost = new HttpPost(path + "?" + "userName="+java.net.URLEncoder.encode(userName) + "&schoolName="+ schoolName+"&phone="+phone + "&introduce=" + introduce +"&password="+password+"&gender="+gender);                           //创建一个GET方式的HttpRequest对象
            httpResponse = httpClient.execute(httpPost);               //执行TTP请求
        } catch (Exception e){
            e.printStackTrace();
        }
        return httpResponse;
    }
}
