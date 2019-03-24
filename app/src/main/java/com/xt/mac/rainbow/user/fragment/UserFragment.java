package com.xt.mac.rainbow.user.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xt.mac.rainbow.R;
import com.xt.mac.rainbow.app.RainbowApplication;
import com.xt.mac.rainbow.base.BaseFragment;
import com.xt.mac.rainbow.login.forget.ForgetActivity;
import com.xt.mac.rainbow.login.login.LoginActivity;
import com.xt.mac.rainbow.login.login.bean.LoginBean;
import com.xt.mac.rainbow.utils.CacheUtils;

public class UserFragment extends BaseFragment {

    private Button updatePwdBtn;
    private Button exitBtn;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_user, null);
        Button updatePwdBtn = view.findViewById(R.id.updatePwdBtn);
        Button exitBtn = view.findViewById(R.id.exitBtn);

        updatePwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ForgetActivity.class);
                startActivity(intent);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserInfo();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(context, "退出成功！", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();

    }

    /*
     * 清楚缓存用户信息
     * */
    private void setUserInfo() {

        CacheUtils.saveString(context, "uidKey", "");
        RainbowApplication.getInstance().setUserID("");
    }
}
