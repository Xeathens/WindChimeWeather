package com.xeathen.windchimeweather.view.fragment;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xeathen.windchimeweather.R;
import com.xeathen.windchimeweather.service.AutoUpdateService;
import com.xeathen.windchimeweather.util.SharedPreferencesUtil;

import org.w3c.dom.Text;

import java.util.Locale;

/**
 * @author: 蛋清蛋黄
 * @date: 2019/3/4
 * @description:
 */
public class SettingsFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener{

    private SharedPreferencesUtil mSpfu;

    private Preference mAutoUpdateTime;

    private Preference mClearCache;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        mSpfu = SharedPreferencesUtil.getInstance();
        mAutoUpdateTime = findPreference(SharedPreferencesUtil.AUTO_UPDATE_TIME);
        mClearCache = findPreference(SharedPreferencesUtil.CLEAR_CACHE);

        mAutoUpdateTime.setSummary(mSpfu.getAutoUpdateTime() == 0 ? "禁止刷新" : String.format(Locale.CHINA, "每%d小时更新", mSpfu.getAutoUpdateTime()));
        mAutoUpdateTime.setOnPreferenceClickListener(this);
        //缓存清除，用文件储存时方可用
        mClearCache.setSummary("开发中");
    }


    //showDialog



    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (mAutoUpdateTime == preference){
            //dialog
            showUpdateDialog();
        }

        return true;
    }

    private void showUpdateDialog(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.dialog_update, (ViewGroup) getActivity().findViewById(R.id.dialog_update));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(dialogLayout);
        final AlertDialog alertDialog = builder.create();
        final SeekBar mSeekBar = dialogLayout.findViewById(R.id.time_seekbar);
        final TextView tvShowHour =  dialogLayout.findViewById(R.id.tv_showhour);
        TextView tvDone = dialogLayout.findViewById(R.id.done);
        mSeekBar.setMax(24);
        mSeekBar.setProgress(mSpfu.getAutoUpdateTime());
        tvShowHour.setText(String.format("每%s小时", mSeekBar.getProgress()));
        alertDialog.show();
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvShowHour.setText(String.format("每%s小时", mSeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpfu.setAutoUpdateTime(mSeekBar.getProgress());
                mAutoUpdateTime.setSummary(mSpfu.getAutoUpdateTime() == 0 ? "禁止刷新" : String.format(Locale.CHINA, "每%d小时更新", mSpfu.getAutoUpdateTime()));
                getActivity().startService(new Intent(getActivity(), AutoUpdateService.class));
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
