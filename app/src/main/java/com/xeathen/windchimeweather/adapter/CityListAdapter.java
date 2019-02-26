package com.xeathen.windchimeweather.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xeathen.windchimeweather.R;
import com.xeathen.windchimeweather.bean.db.CityDB;
import com.xeathen.windchimeweather.bean.model.City;
import com.xeathen.windchimeweather.common.MyApplication;
import com.xeathen.windchimeweather.view.activity.WeatherActivity;

import java.util.List;

/**
 * @author: 蛋清蛋黄
 * @date: 2018/12/31
 * @description:
 */
public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

    private List<City> mCityList;

    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View cityListView;
        TextView cityName;
        TextView adminArea;
        TextView country;

        public ViewHolder(View view){
            super(view);
            cityListView = view;
            cityName = view.findViewById(R.id.search_city_name);
            adminArea = view.findViewById(R.id.search_admin_area);
            country = view.findViewById(R.id.search_country);
        }

    }
    public CityListAdapter(List<City> cityList){
        mCityList = cityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_list_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        City city = mCityList.get(position);
        holder.cityName.setText(city.getName());
        holder.adminArea.setText(city.getAdminArea());
        holder.country.setText(city.getCountry());
        holder.cityListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();
                City city = mCityList.get(position);
                CityDB cityDB = new CityDB();
                cityDB.setName(city.getName());
                cityDB.setCityId(city.getID());
                cityDB.setParentCity(city.getParentCity());
                cityDB.setAdminArea(city.getAdminArea());
                cityDB.setCountry(city.getCountry());
                cityDB.save();
//                List<CityDB> cityDBS = LitePal.findAll(CityDB.class);
//                for(CityDB cityDB1 : cityDBS){
//                    LogUtil.i("CityListAdapter", cityDB1.getCityId() + cityDB1.getName());
//                }

                //保存当前cityId
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                editor.putString("current_city_id", city.getID());
                editor.apply();
//                Toast.makeText(MyApplication.getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, WeatherActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCityList.size();
    }
}
