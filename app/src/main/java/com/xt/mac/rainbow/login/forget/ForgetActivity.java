package com.xt.mac.rainbow.login.forget;

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
import com.xt.mac.rainbow.login.forget.bean.ForgetBean;
import com.xt.mac.rainbow.login.login.LoginActivity;
import com.xt.mac.rainbow.login.login.bean.LoginBean;
import com.xt.mac.rainbow.utils.CacheUtils;
import com.xt.mac.rainbow.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class ForgetActivity extends AppCompatActivity {

    private static final String TAG = ForgetActivity.class.getSimpleName();
    private EditText oldPwd;
    private EditText newPwd;
    private EditText newPwd2;
    private Button sureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        initView();
    }

    private void initView() {
        oldPwd = findViewById(R.id.et_oldpassword);
        newPwd = findViewById(R.id.et_newpassword);
        newPwd2 = findViewById(R.id.et_newpassword2);
        sureBtn = findViewById(R.id.btn_sure);

        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPwd.getText().equals(newPwd2.getText())) {
                    getDataFromNet();
                }
                else  {
                    Toast.makeText(ForgetActivity.this, "新密码与确认密码不一致！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
     * 请求网络
     * */
    private void getDataFromNet() {
        String url = Constants.get_queryupdatePwdApp + "?appUserId=" + RainbowApplication.getInstance().getUserID() + "&oldPwd=" + oldPwd.getText() + "&newPwd=" + newPwd.getText();
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

                        Log.e(TAG,"请求失败==" + e.getMessage());
                    }

                    /**
                     * 当联网成功的时候回调
                     * @param response 请求成功的数据
                     * @param id
                     */
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG,"请求成功==" + response);
                        //解析数据
                        processData(response);
                    }

                });
    }

    /*
     * 解析数据
     * */
    private void processData(String json) {
        ForgetBean bean = parsedJson(json);
        if(bean != null){
            // 注意：不能用 == 要用equals
            if (bean.getRespCode().equals("success")) {

                Toast.makeText(this, bean.getRespMsg(), Toast.LENGTH_SHORT).show();

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
    private ForgetBean parsedJson(String json) {
        Gson gson = new Gson();
        ForgetBean bean = gson.fromJson(json, ForgetBean.class);
        return bean;
    }
}
