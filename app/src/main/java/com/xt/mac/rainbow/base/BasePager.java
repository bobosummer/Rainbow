package com.xt.mac.rainbow.base;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xt.mac.rainbow.R;

public class BasePager extends AppCompatActivity {

    // 这里要改成public 否则子类用不了；
    public final Context context;

    // 视图，代表各个不同的页面
    public View rootView;

    public FrameLayout fl_content;

    /*
    * 构造方法
    * 给子类使用上下文
    * */
    public BasePager(Context context) {
        this.context = context;     // MainActivity
        rootView = initView();
    }

    /*
    * 用于初始化公共部分视图，并且初始化加载子视图的FrameLayout
    * 这里不要使用抽象方法
    * */
    private View initView() {
        View view = View.inflate(context, R.layout.base_pager, null);
        fl_content = view.findViewById(R.id.fl_content);

        return view;
    }

    /*
    * 初始化数据
    * */
    public void initData() {


    }
}
