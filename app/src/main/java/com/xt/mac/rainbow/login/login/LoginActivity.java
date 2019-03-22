package com.xt.mac.rainbow.login.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xt.mac.rainbow.R;
import com.xt.mac.rainbow.app.MainActivity;
import com.xt.mac.rainbow.app.RainbowApplication;
import com.xt.mac.rainbow.app.SplashActivity;
import com.xt.mac.rainbow.login.login.bean.LoginBean;
import com.xt.mac.rainbow.utils.CacheUtils;
import com.xt.mac.rainbow.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class LoginActivity extends Activity {

    private static final String TAG = LoginPager.class.getSimpleName();
    private EditText ll_username;
    private EditText ll_password;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 注意： android.widget.LinearLayout cannot be cast to android.widget.EditText 说明 id对应的view写错了，不是EditView
        ll_username = findViewById(R.id.et_username);
        ll_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);

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
            // 注意：不能用 == 要用equals
            if (bean.getRespCode().equals("success")) {
                // 存用户信息
                String uid = String.valueOf(bean.getContent().getId());
                CacheUtils.saveString(this, "uidKey", uid);
                RainbowApplication.getInstance().setUserID(uid);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, bean.getRespMsg(), Toast.LENGTH_SHORT).show();
            }

        }else{
            //没有数据
        }
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
