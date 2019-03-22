package com.xt.mac.rainbow.login.login;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xt.mac.rainbow.R;
import com.xt.mac.rainbow.base.BasePager;
import com.xt.mac.rainbow.login.login.bean.LoginBean;
import com.xt.mac.rainbow.utils.CacheUtils;
import com.xt.mac.rainbow.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

public class LoginPager extends BasePager {
    private static final String TAG = LoginPager.class.getSimpleName();
    private EditText ll_username;
    private EditText ll_password;
    private EditText btn_login;

    public LoginPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        View view = View.inflate(context, R.layout.activity_login, null);
        ll_username = view.findViewById(R.id.ll_username);
        ll_password = view.findViewById(R.id.ll_password);
        btn_login = view.findViewById(R.id.btn_login);
        fl_content.addView(view);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 请求网络
                getDataFromNet();
            }
        });
    }

    private void getDataFromNet() {
        String url = Constants.get_queryloginApp + "?userName=" + ll_username.getText() + "&password=" + ll_password.getText();
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    /**
                     * 当请求失败的时候回调
                     * @param call
                     * @param e
                     * @param id
                     */
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        Log.e(TAG,"首页请求失败=="+e.getMessage());
                    }

                    /**
                     * 当联网成功的时候回调
                     * @param response 请求成功的数据
                     * @param id
                     */
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG,"首页请求成功=="+response);
                        //解析数据
                        processData(response);
                    }


                });
    }

    private void processData(String json) {
        LoginBean bean = parsedJson(json);
        if(bean != null){
            if (bean.getRespCode() == "success") {
//                Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
            }

        }else{
            //没有数据
        }
        Log.e(TAG,"解析成功=="+bean.getRespCode());
    }

    /*
     * 解析json数据
     * */
    private LoginBean parsedJson(String json) {
        Gson gson = new Gson();
        LoginBean bean = gson.fromJson(json, LoginBean.class);
        return bean;
    }
}
