package com.xt.mac.rainbow.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmjrxy.school.R;
import com.mmjrxy.school.util.MmPermissions;
import com.mmjrxy.school.util.StatusBarUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import java.util.List;


/**
 * Activity 抽象类，子项目基类要实现{@link #initRootView},初始化titlebar各控件
 */
public abstract class BaseActivity extends AppCompatActivity implements OnClickListener, MmPermissions.PermissionCallbacks  {

    public final String TAG = getClass().getSimpleName();

    public static final int RESULT_FAIL = -2;
    public static final String MATA_KEY_LOGIN = "LOGIN";

    protected boolean mHasTitleButtonLeft = true;
    protected boolean mHasTitleButtonRight = false;

    protected LinearLayout mMainLayout;
    protected TextView mTitlebarTitle;
    /**
     * titlebar 由上方按钮
     **/
    protected View mTitlebarLayoutRight;
    protected ImageView mTitlebarBtnRight;
    protected TextView mTitlebarTvRight;
    /**
     * titlebar 左上方按钮
     **/
    protected View mTitlebarLayoutLeft;
    protected ImageView mTitlebarBtnLeft;
    protected TextView mTitlebarTvLeft;

    public BaseActivity mCurActivity;
    /**
     * 传到当前Activity的intent
     */
    protected Intent mIntent;


    /**
     * 暂存目标intent，登录后重新发起
     */
    private Intent targetAfterLoginIntent;
    private int targetAfterLoginreQuestCode = -1;
    private Bundle targetAfterLoginOptions;

    protected Button ButtonSearch;

    /**
     * jsinterface
     */
    public static double VERSON = 1.0;
    public static String LOG_TAG = "jsinterface";
    public String last_error_str = "";

//    protected WebView       webview;
//    public    PluginManager pluginManager;

    /**
     * 子类实现自定义基础视图
     */
    protected abstract void initRootView();

    /**
     * 初始化视图，若使用基类定义的标题栏，则使用{@link #setMainContentView(int)}，
     * 自定义时使用 {@link #setContentView(int)}
     */
    protected abstract void initView();

    /**
     * 数据初始化，
     * 可以发起网络数据请求
     */
    protected void initData() {
    }

    /**
     * 监听器初始化
     * 适合设置onclick，Textwatcher等
     */
    protected void initListener() {
    }

    /**
     * 创建加载框
     */
    protected abstract Dialog getLoadingDialog();

    /**
     * 刷新数据
     * 建议网络请求放此处，initData方法里直接调用此方法
     */
    public void refreshData() {
    }

    public BaseActivity getCurActivity() {
        return mCurActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        SchoolApplication.getInstance().getAllActivitys().add(this);
        mCurActivity = this;
        // 设置状态栏一体化
        if (MmPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            setTranslucentStatus();
        } else {
            MmPermissions.addCallback(this);
            MmPermissions.requestPermissions(this, this.getString(R.string.tip_permission_external_storage), 666, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        mIntent = getIntent();
        //主要调用顺序
        initRootView();
        initView();
        initListener();
        initData();

    }


    /**
     * 是否开启右滑动返回
     *
     * @return
     */
    protected boolean isSwipeBack() {
        return true;
    }

    /**
     * 是否设置状态栏一体化
     *
     * @return
     */
    public boolean isSetTranslucentStatus() {
        return true;
    }

    /**
     * show log to LogCat
     *
     * @param msg
     */
    public void log(String msg) {

    }

    public void log_error(String msg) {
        last_error_str = msg;
    }

    public void log_warn(String msg) {

    }

    public void toast(String s) {
        showNormalToast(s);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @TargetApi(19)
    protected void setTranslucentStatus() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
            tintManager.setStatusBarAlpha(0);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
//
        if (Build.VERSION.SDK_INT >= 21) {//5.0 全透明实现   //modify by xn 6.0谷歌亲儿子
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        if (StatusBarUtil.isFlyme()) {
            StatusBarUtil.INSTANCE.FlymeSetStatusBarLightMode(getWindow(), true);
        } else if (StatusBarUtil.isMIUI()) {
            StatusBarUtil.MIUISetStatusBarLightMode(getWindow(), true);
        } else if (StatusBarUtil.isOppo()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StatusBarUtil.INSTANCE.normalSet(getCurActivity());
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StatusBarUtil.INSTANCE.normalSet(getCurActivity());
            }
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * <b>NOTE:</b> replaced by {@link #setMainContentView(View)} method so that you don't need to include titlebar in contentView.
     * Or you should include R.layout.common_title_bar layout, override {@link #onCreate(Bundle)} and call {@link #initRootView()} to init tiilebar
     *
     * @param view
     */
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    /**
     * <b>NOTE:</b> replaced by {@link #setMainContentView(int)} method so that you don't need to include titlebar in contentView.
     * Or you should include R.layout.common_title_bar layout, override {@link #onCreate(Bundle)} and call {@link #initRootView()} to init tiilebar
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    /**
     * <b>NOTE:</b> replace {@link #setContentView(int)} method so that you don't need to include titlebar in contentView.
     * Or you should include R.layout.common_title_bar layout, override {@link #onCreate(Bundle)} and call {@link #initRootView()} to init tiilebar
     *
     * @param resourceId
     */
    public void setMainContentView(int resourceId) {
        initRootView();
        getLayoutInflater().inflate(resourceId, mMainLayout);
    }

    /**
     * <b>NOTE:</b> replace {@link #setMainContentView(View)} method so that you don't need to include titlebar in contentView.
     * Or you should include R.layout.common_title_bar layout, override {@link #onCreate(Bundle)} and call {@link #initRootView()} to init tiilebar
     *
     * @param view
     */
    public void setMainContentView(View view) {
        initRootView();
        mMainLayout.addView(view);
    }

    /**
     * 刷新titlebar布局
     */
    public void refreshTitleBar() {
        if (mTitlebarLayoutRight != null) {
            if (mHasTitleButtonRight) {
                mTitlebarLayoutRight.setVisibility(View.VISIBLE);
            } else {
                mTitlebarLayoutRight.setVisibility(View.GONE);
            }
        }
        if (mTitlebarBtnLeft != null) {
            if (mHasTitleButtonLeft) {
                mTitlebarLayoutLeft.setVisibility(View.VISIBLE);
            } else {
                mTitlebarLayoutLeft.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void setTitle(CharSequence title) {
        initTitle(title.toString());
    }


    @Override
    public void setTitle(int id) {
        initTitle(getResources().getString(id));
    }

    public void initTitle(int id) {
        initTitle(getResources().getString(id));
    }

    public void initTitle(CharSequence titleStr) {
        if (mTitlebarTitle != null) {
            mTitlebarTitle.setText(titleStr);
        }
        //兼容自定义titlebar
        if (mTitlebarTitle != null) {
            mTitlebarTitle.setText(titleStr);
        }
        // 返回按钮
        if (mTitlebarBtnLeft != null) {
            mTitlebarBtnLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    back();
                }
            });
        }
    }

    public void back() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onClick(View v) {
    }

    public void showNormalToast(String msg) {
    }

    public void showLoadingDialog() {
        if (mCurActivity == null || mCurActivity.isFinishing()) {
            return;
        }
        if (getLoadingDialog() != null && !getLoadingDialog().isShowing()) {
            getLoadingDialog().show();
        }
    }

    public void closeLoadingDialog() {
        if (mCurActivity == null || mCurActivity.isFinishing()) {
            return;
        }
        if (getLoadingDialog() != null && getLoadingDialog().isShowing()) {
            getLoadingDialog().dismiss();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
       /* if (SpUtils.getBoolean(AudioWindow.AUDIO_STATE, false) && !TextUtils.isEmpty(SpUtils.getString(MaConstant.videoName, ""))) {
            View view = LayoutInflater.from(this).inflate(R.layout.audio_window_layout, null);
            if (mCurActivity instanceof MainActivity) {
                SchoolApplication.getToast().setView(view, DeviceUtil.getWindowWidth(this), DensityUtil.dp2px(SchoolApplication.getInstance(), 50), true);
                SchoolApplication.getToast().setGravity(Gravity.BOTTOM, 0, DensityUtil.dp2px(SchoolApplication.getInstance(), 48));
            } else {
                SchoolApplication.getToast().setView(view, DeviceUtil.getWindowWidth(this), DensityUtil.dp2px(SchoolApplication.getInstance(), 50), false);
                SchoolApplication.getToast().setGravity(Gravity.BOTTOM, 0, 0);
            }
            SchoolApplication.getToast().setView(view);
            if (mCurActivity instanceof MainActivity) {
                SchoolApplication.getToast().show();
            } else {
                SchoolApplication.getToast().hide();
            }
        }*/
    }

    protected void onResumeNew() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SchoolApplication.getInstance().getAllActivitys().remove(this);
        closeLoadingDialog();
    }

    @SuppressWarnings("unchecked")
    protected <T> T findView(int id) {
        T t = (T) findViewById(id);
        return t;
    }

    @Override
    public void onBackPressed() {
        boolean canConsume = true;
        @SuppressLint("RestrictedApi") List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment instanceof BaseFragment && ((BaseFragment) fragment).onBackPressed() && canConsume) {
                    canConsume = false;
                }
            }
        }
        if (canConsume) {
            super.onBackPressed();
        }
    }

    /**
     * 更改背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }


    //判断应用是否在后台运行
    public boolean isBackground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (TextUtils.equals(appProcess.processName, getPackageName())) {
                boolean isBackground = (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE);
                boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
                return isBackground || isLockedState;
            }
        }
        return false;
    }


//    @Override
//    public void startActivityForResult(Intent intent, int requestCode) {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
//            super.startActivityForResult(intent,requestCode);
//        }else {
//            ComponentName componentName = intent.getComponent();
//            if (componentName != null && PackageUtil.isActivityNeedLogin(componentName)){
//                targetAfterLoginIntent = intent;
//                targetAfterLoginreQuestCode = requestCode;
//                showLogin();
//            }else {
//                super.startActivityForResult(intent, requestCode);
//            }
//        }
//    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        MmPermissions.removeCallback(this);
        if (MmPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            setTranslucentStatus();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        MmPermissions.removeCallback(this);
    }

    @Override
    public void onPermissionsAllGranted() {
        MmPermissions.removeCallback(this);
    }

    /**
     * 登录返回后继续跳转
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*      if (requestCode == BaseConstant.INTENT_REQUEST_CODE.LOGIN && resultCode == RESULT_OK ) {
            refreshData();
            if(!isFinishing() && targetAfterLoginIntent != null){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    super.startActivityForResult(targetAfterLoginIntent,targetAfterLoginreQuestCode,targetAfterLoginOptions);
                }else {
                    super.startActivityForResult(targetAfterLoginIntent,targetAfterLoginreQuestCode);
                }
                targetAfterLoginIntent = null;
                targetAfterLoginreQuestCode = -1;
                targetAfterLoginOptions = null;
            }
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, true);
    }

    public void replaceFragment(Fragment fragment, boolean isAddToBackStack) {
        try {
            String tag = getClass().getSimpleName();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(android.R.id.content, fragment, tag);
            transaction.setTransition(FragmentTransaction.TRANSIT_NONE);//设置动画效果
            if (isAddToBackStack) {
                transaction.addToBackStack(tag);
            }
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace(); //java.lang.IllegalStateException: Activity has been destroyed
        }
    }

    public void replaceFragment(int layoutId, Fragment fragment, boolean isAddToBackStack) {
        try {
            String tag = getClass().getSimpleName();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(layoutId, fragment, tag);
            transaction.setTransition(FragmentTransaction.TRANSIT_NONE);//设置动画效果
            if (isAddToBackStack) {
                transaction.addToBackStack(tag);
            }
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace(); //java.lang.IllegalStateException: Activity has been destroyed
        }
    }

    public void addFragment(Fragment fragment, boolean isAddToBackStack) {
        addFragment(fragment, isAddToBackStack, true);
    }

    public void addFragment(Fragment fragment, boolean isAddToBackStack, boolean isShowAnim) {
        if (!fragment.isAdded()) {
            String tag = getClass().getSimpleName();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (isShowAnim) {
                transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out, R.anim.slide_right_in, R.anim.slide_right_out);
            }
            transaction.add(android.R.id.content, fragment, tag);
            if (isAddToBackStack) {
                transaction.addToBackStack(tag);
            }
            transaction.commitAllowingStateLoss();
        }
    }


    public boolean isAvailable() {
        return !isFinishing() && !isDestroyed() && getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED);
    }

    public void runOnMainThread(Runnable runnable) {
        if (isAvailable() && null != runnable)
            runOnUiThread(runnable);
    }
}
