package com.chris.hidedocument.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chris.hidedocument.R;
import com.chris.hidedocument.listener.OnItemClickListener;
import com.chris.hidedocument.model.FileInfoBean;
import com.chris.hidedocument.model.SmsBean;
import com.chris.hidedocument.tools.HideUtils;

import java.util.List;

/**
 * Created by zoulx on 2016/3/30.
 */
public class SmsRecyclerAdapter extends RecyclerView.Adapter<SmsRecyclerAdapter.SmsRecyClerViewHolder> {

    private List<SmsBean> mMainDataList;
    private OnItemClickListener mOnItemClickListener;
    private int thumbnailSize = 80;//缩略大小

    public SmsRecyclerAdapter(List<SmsBean> mainDataList) {
        this.mMainDataList = mainDataList;
    }

    @Override
    public SmsRecyClerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms, parent, false);
        SmsRecyClerViewHolder viewHolder = new SmsRecyClerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SmsRecyClerViewHolder holder, final int position) {
        SmsBean bean = mMainDataList.get(position);
        holder.isCheck.setChecked(bean.isCheck());
        holder.isCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(v, position);
                }
            }
        });
        holder.person.setText(mMainDataList.get(position).getSmsPerson());
        holder.address.setText(mMainDataList.get(position).getSmsAddress());
        holder.date.setText(mMainDataList.get(position).getSmsDate());
        holder.body.setText(mMainDataList.get(position).getSmsBody());
        if(mMainDataList.get(position).getSmsType().equalsIgnoreCase("1")){
            holder.type.setText("收");
        }else{
            holder.type.setText("发");
        }

    }

    @Override
    public int getItemCount() {
        return this.mMainDataList.size();
    }

    public class SmsRecyClerViewHolder extends RecyclerView.ViewHolder {
        private TextView person;//名称
        private TextView address;//号码地址
        private TextView date;//发送日期
        private TextView body;//内容
        private TextView type;//发送状态1：收到 2：发送
        private CheckBox isCheck;

        public SmsRecyClerViewHolder(View itemView) {
            super(itemView);
            person= (TextView) itemView.findViewById(R.id.smsPerson);
            address= (TextView) itemView.findViewById(R.id.smsAddress);
            isCheck= (CheckBox) itemView.findViewById(R.id.isCheck);
            date= (TextView) itemView.findViewById(R.id.smsDate);
            body= (TextView) itemView.findViewById(R.id.smsBody);
            type= (TextView) itemView.findViewById(R.id.smsType);
        }
    }

    public void setData(List<SmsBean> smsBeanList) {
        if (smsBeanList != null) {
            this.mMainDataList = smsBeanList;
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener != null) {
            this.mOnItemClickListener = onItemClickListener;
        }
    }
}
