package com.xt.mac.rainbow.home.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xt.mac.rainbow.app.RainbowApplication;
import com.xt.mac.rainbow.base.BaseFragment;

public class HomeFragment extends BaseFragment {
    private TextView textView;

    @Override
    public View initView() {

//        View view = View.inflate(context, R.layout.fragment_home, null);
//        rvHome = (RecyclerView) view.findViewById(R.id.rv_home);
//        ib_top = (ImageView) view.findViewById(R.id.ib_top);
//        tv_search_home = (TextView) view.findViewById(R.id.tv_search_home);
//        tv_message_home = (TextView) view.findViewById(R.id.tv_message_home);

        //设置点击事件
//        initListener();

//        return view;

        /*
         * 注意：Attempt to invoke virtual method 'void android.widget.TextView.setTextSize(float)' on a null object reference
         * 这个问题是因为 我没有实例化TextView，就直接赋值了； 空对象
         * */
        textView = new TextView(context);
        textView.setTextSize(23);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }


    /*private void initListener() {
        //置顶的监听
        ib_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //回到顶部
                rvHome.scrollToPosition(0);
            }
        });
        //搜素的监听
        tv_search_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "搜索",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //消息的监听
        tv_message_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "进入消息中心",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    @Override
    public void initData() {
        super.initData();

        String userID = RainbowApplication.getInstance().getUserID();
        textView.setText("主页" + userID);
        RainbowApplication.getInstance().setUserID("10");
    }
}
