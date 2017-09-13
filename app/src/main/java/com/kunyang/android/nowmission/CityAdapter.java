package com.kunyang.android.nowmission;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kunyang.android.nowmission.db.City;
import com.kunyang.android.nowmission.db.Country;

import java.util.List;

/**
 * Created by 坤阳 on 2017/9/12.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> implements View.OnClickListener {

    private OnItemClickListener mOnItemClickListener=null;
    private Context mContext;
    private List<String> mCityList;

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener!=null){
            mOnItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;

        public ViewHolder(View view){
            super(view);
            cityName=(TextView)view.findViewById(R.id.city_name);
        }
    }

    public CityAdapter(List<String> cityList){
        mCityList=cityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.city_item,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String city=mCityList.get(position);
        holder.cityName.setText(city.toString());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mCityList.size();
    }

    public static interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

}
