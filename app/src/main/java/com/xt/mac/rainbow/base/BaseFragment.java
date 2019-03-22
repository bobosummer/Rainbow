package com.xt.mac.rainbow.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
* 注意：继承 v4版本的Fragment，否则会有其他问题；
* */
public abstract class BaseFragment extends Fragment {
    //外面继承基类的时候会用到上下文；  这里的上下文为Fragment，也是谁继承了就是谁，比如MainActivity继承了，这里的activity就是MainActivity；目的是后面要填充上下文布局显示元素；
    public Activity context;

    /*
     * 当Fragment被创建的时候回调这个方法；
     * 你可以在其中初始化除了view之外的东西。
     * */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取上下文
        context = getActivity();
    }

    /*
     * 当视图被创建的时候回调这个方法  -- 这个先执行  创建视图
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        //这里是基类，因此让子类创建就行了；返回个抽象方法来创建；
        // 目的是让子类实现自己的视图，达到自己特有的效果；
        return initView();
    }

    // 1、因为抽象方法，不需要写方法体，让子类去实现；2、一个类中有抽象方法，这个类必须为抽象类；
    // 注意：写成抽象方法，是强制子类必须实现，不写抽象，子类可实现也可不实现；
    public abstract View initView();

    /*
     * 当Activity被创建之后回调这个方法  --- 后执行   请求数据，绑定视图；
     * */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /*
     * 1、如果子页面没有数据，联网请求数据，并且绑定到initView初始化视图上；
     * 2、有数据，直接绑定到initView初始化视图上；
     * */
    // 这里必须改成public，默认private子类是无法重写的方法的；
    public void initData() {

    }
}
