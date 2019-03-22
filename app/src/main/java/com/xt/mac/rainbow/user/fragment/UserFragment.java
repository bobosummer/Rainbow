package com.xt.mac.rainbow.user.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.xt.mac.rainbow.app.RainbowApplication;
import com.xt.mac.rainbow.base.BaseFragment;

public class UserFragment extends BaseFragment {
    private TextView textView;

    @Override
    public View initView() {

        textView = new TextView(context);
        textView.setTextSize(23);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();

        textView.setText("个人中心" + RainbowApplication.getInstance().getUserID());
    }
}
