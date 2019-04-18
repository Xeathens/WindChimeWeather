package com.xeathen.windchimeweather.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.xeathen.windchimeweather.controller.ActivityCollector;

import butterknife.ButterKnife;


/**
 * @author: 蛋清蛋黄
 * @date: 2018/12/15
 * @description: BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {


    //是否显示状态栏,即全屏
    private boolean isShowState = true;
    //封装Toast
    private static Toast toast;
    //获取TAG
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        if (!isShowState) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        //设置布局
        setContentView(initLayout());
        //ButterKnife绑定Activity
        ButterKnife.bind(this);


    }

    /**
     * @param
     * @methodName: initLayout
     * @describe: 初始化布局
     */

    public abstract int initLayout();


    public void setState(boolean isShow) {
        isShowState = isShow;
    }

    /**
     * 显示长toast
     *
     * @param context,message
     */
    public void toastLong(Context context, CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(context, null, Toast.LENGTH_LONG);
            toast.setText(message);
            toast.show();
        } else {
            toast.setText(message);
            toast.show();
        }
    }

    /**
     * 显示短toast
     *
     * @param context,message
     */
    public void toastShort(Context context, CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            toast.setText(message);
            toast.show();
        } else {
            toast.setText(message);
            toast.show();
        }

    }


}
