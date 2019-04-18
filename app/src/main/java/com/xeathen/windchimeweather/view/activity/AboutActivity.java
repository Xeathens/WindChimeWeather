package com.xeathen.windchimeweather.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xeathen.lib.utils.LogUtil;
import com.xeathen.windchimeweather.R;
import com.xeathen.windchimeweather.custom.CustomUpdateParser;
import com.xeathen.windchimeweather.util.EnvUtil;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.utils.UpdateUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bt_roast)
    Button btRoast;

    @BindView(R.id.bt_upgrade)
    Button btUpgrade;

    @BindView(R.id.bt_donate)
    Button btDonate;

    @BindView(R.id.bt_share)
    Button btShare;

    @BindView(R.id.about_version)
    TextView aboutVersion;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        ViewGroup.MarginLayoutParams toolLayoutParams = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
        toolLayoutParams.height = EnvUtil.getStatusBarHeight() + EnvUtil.getActionBarSize(this);
        toolbar.setLayoutParams(toolLayoutParams);
        toolbar.setPadding(0, EnvUtil.getStatusBarHeight(), 0, 0);
        toolbar.requestLayout();
        toolbar.setTitle("更多");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
//        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        aboutVersion.setText("当前版本: " + UpdateUtils.getVersionName(this));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);

        }

    }

    @OnClick(R.id.bt_donate)
    public void showDonationDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.dialog_donation, (ViewGroup) findViewById(R.id.dialog_donation));
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(dialogLayout);
        final AlertDialog alertDialog = builder.create();

        final ImageView donation = dialogLayout.findViewById(R.id.img_donation);
        alertDialog.show();
        Window dialogWindow = alertDialog.getWindow();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = (int) (display.getWidth() * 0.7);
        params.height = (int) (display.getWidth() * 0.7);
        dialogWindow.setAttributes(params);
        donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    @OnClick(R.id.bt_roast)
    public void showRoastDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.dialog_roast, (ViewGroup) findViewById(R.id.dialog_roast));
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(dialogLayout);
        final AlertDialog alertDialog = builder.create();
        final EditText roastName = dialogLayout.findViewById(R.id.edit_roast_name);
        final EditText roastContent = dialogLayout.findViewById(R.id.edit_roast_content);
        final TextView send = dialogLayout.findViewById(R.id.bt_send_roast);
        alertDialog.show();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("https://sc.ftqq.com/SCU47634T634711707d1c42b713158d4f50db6f515ca1f41231b42.send?text=" + roastContent.getText().toString()).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        LogUtil.i("onResponse", "Success");
                    }
                });
                alertDialog.dismiss();
                toastShort(AboutActivity.this, "吐槽成功");
            }
        });
    }

    @OnClick(R.id.bt_share)
    public void showShareDialog(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.dialog_share, (ViewGroup) findViewById(R.id.dialog_share));
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(dialogLayout);
        final AlertDialog alertDialog = builder.create();
        final ImageView share = dialogLayout.findViewById(R.id.img_share);
        alertDialog.show();
        Window dialogWindow = alertDialog.getWindow();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = (int) (display.getWidth() * 0.7);
        params.height = (int) (display.getWidth() * 0.7);
        dialogWindow.setAttributes(params);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @OnClick(R.id.bt_upgrade)
    public void upgradeApk(){
        XUpdate.newBuild(this)
                .updateUrl("http://47.100.235.194:8080/update/checkVersion")
                .updateParser(new CustomUpdateParser())
                .update();
    }



    @Override
    public int initLayout() {
        return R.layout.activity_about;
    }

}
