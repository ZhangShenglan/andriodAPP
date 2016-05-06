package com.app.linj.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.linj.R;
import com.app.linj.bean.TieBaName;
import com.app.linj.bean.UserBean;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhangshenglan on 16/5/6.
 */
public class BanamelistActivity  extends Activity {
    private UserBean user;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balistactivity);
        user = (UserBean) getIntent().getSerializableExtra("listdata");
        listView = (ListView) findViewById(R.id.balistview);

        BmobQuery<TieBaName> query = new BmobQuery<TieBaName>();
        query.addWhereRelatedTo("usertbname", new BmobPointer(user));
        query.findObjects(BanamelistActivity.this,
                new FindListener<TieBaName>() {

                    @Override
                    public void onSuccess(List<TieBaName> arg0) {
                        String[] arrays = new String[arg0.size()];
                        for (int i = 0; i < arg0.size(); i++) {
                            arrays[i] = arg0.get(i).getName();
                        }
                        listView.setAdapter(new ArrayAdapter<String>(
                                BanamelistActivity.this,
                                android.R.layout.simple_list_item_1, arrays));
                    }

                    @Override
                    public void onError(int arg0, String arg1) {

                    }
                });

    }
}
