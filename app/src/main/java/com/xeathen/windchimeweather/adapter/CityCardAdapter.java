package com.xeathen.windchimeweather.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xeathen.windchimeweather.R;
import com.xeathen.windchimeweather.bean.db.CityDB;
import com.xeathen.windchimeweather.common.MyApplication;
import com.xeathen.windchimeweather.util.WeatherDrawableUtil;
import com.xeathen.windchimeweather.view.activity.CityActivity;
import com.xeathen.windchimeweather.view.activity.WeatherActivity;

import org.litepal.LitePal;

import java.util.List;

/**
 * @author: 蛋清蛋黄
 * @date: 2019/1/20
 * @description:
 */
public class CityCardAdapter extends RecyclerView.Adapter<CityCardAdapter.ViewHolder> {

    private CityActivity mCityActivityContext;

    private Context mContext;

    private List<CityDB> mCityList;


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView cardName;
        TextView cardCountry;
        Button delete;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            cardName = view.findViewById(R.id.card_name);
            cardCountry = view.findViewById(R.id.card_country);
            delete = view.findViewById(R.id.card_delete);
        }

//        public void setDeleteGone() {
//            if (delete.getVisibility() == View.GONE) {
//                delete.setVisibility(View.VISIBLE);
//            } else if (delete.getVisibility() == View.VISIBLE) {
//                delete.setVisibility(View.GONE);
//
//            }
//        }

    }

    public CityCardAdapter(CityActivity cityActivityContext, List<CityDB> cityList) {
        mCityList = cityList;
        mCityActivityContext = cityActivityContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.city_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final CityDB cityDB = mCityList.get(position);
        holder.cardName.setText(cityDB.getName());
        holder.cardCountry.setText(cityDB.getAdminArea());
        if (cityDB.isGps()) {
            if (Build.VERSION.SDK_INT >= 21) {
                Drawable drawable = MyApplication.getContext().getDrawable(R.drawable.ic_location);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.cardName.setCompoundDrawables(null, null, drawable, null);
            }
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变current
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                editor.putString("current_city_id", cityDB.getCityId());
                editor.apply();
                Intent intent = new Intent(mCityActivityContext, WeatherActivity.class);
                mCityActivityContext.startActivity(intent);

            }
        };
        holder.cardView.setOnClickListener(listener);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String currentCityId = prefs.getString("current_city_id", null);
                if (mCityList.size() > 1) { //不允许删除仅剩的城市
                    if (!cityDB.getCityId().equals(currentCityId)) { //不允许删除当前选择城市

                        LitePal.delete(cityDB.getClass(), cityDB.getId());
                        mCityList = LitePal.findAll(CityDB.class);
                        notifyDataSetChanged();
                        Toast.makeText(MyApplication.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                        //删除缓存
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                        editor.remove("json_weather_basic_" + cityDB.getCityId());
                        editor.remove("json_weather_air_" + cityDB.getCityId());
                        editor.apply();
                    } else {
                        //Dialog
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                        dialog.setTitle("警告");
                        dialog.setMessage("不能删除当前城市");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                    }
                } else {
                    //Dialog
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("警告");
                    dialog.setMessage("不能删除仅剩的城市");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return mCityList.size();
    }
}
