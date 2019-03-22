package com.xt.mac.rainbow.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.xt.mac.rainbow.R;
import com.xt.mac.rainbow.login.login.LoginActivity;
import com.xt.mac.rainbow.login.login.LoginPager;
import com.xt.mac.rainbow.utils.CacheUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends Activity {

    @Bind(R.id.rl_splash_root)
    RelativeLayout rlSplashRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setFillAfter(true);

        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setFillAfter(true);

        RotateAnimation ra = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ra.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(aa);
        set.addAnimation(sa);
        set.addAnimation(ra);

        set.setDuration(2000);
        rlSplashRoot.startAnimation(set);

        // 里面是个接口，需要写个类去实现里面的方法，然后把新建的类名new进去；
        set.setAnimationListener(new myAnimationListener());
    }

    class myAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // 这里需要传上下文，因为shaprePrefence需要； 这里key其他地方也需要用，直接抽取成静态常量
            String isStartMain = CacheUtils.getString(SplashActivity.this, "uidKey");
            Intent intent;
            if (!TextUtils.isEmpty(isStartMain)) {     // 注意：TextUtils.isEmpty()是Android中判断null和长度；String 类下的 isEmpty( ) 是java下只会判断长度；
                //  != null  不能够判断长度
                // 进主页面
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }
            else {
                // 进入登录页面
                // 注意 ： 这两个参数都必须是Activity ，不然无法运行startActivity();
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);

            // 关闭splash页面
            finish();

//            Toast.makeText(SplashActivity.this, "动画已结束", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
