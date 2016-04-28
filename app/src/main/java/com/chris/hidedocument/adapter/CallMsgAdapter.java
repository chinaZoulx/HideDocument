package com.chris.hidedocument.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chris.hidedocument.R;
import com.chris.hidedocument.listener.OnItemClickListener;
import com.chris.hidedocument.model.CallMsgBean;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by zoulx on 2016/4/8.
 */
public class CallMsgAdapter extends RecyclerView.Adapter<CallMsgAdapter.CallMsgViewHolder> {

    List<CallMsgBean> callMsgBeanList;
    private OnItemClickListener onItemClickListener;

    public CallMsgAdapter(List<CallMsgBean> callMsgBeanLIst) {
        this.callMsgBeanList = callMsgBeanLIst;
    }

    @Override
    public CallMsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_msg, parent, false);
        CallMsgViewHolder holder = new CallMsgViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CallMsgViewHolder holder, final int position) {
        CallMsgBean bean = callMsgBeanList.get(position);
        holder.person.setText(bean.getPerson());
        holder.address.setText(bean.getAddress());
        holder.date.setText(bean.getDate());
        if ("1".equalsIgnoreCase(bean.getType())) {//来电
            holder.type.setText("来电");
        } else if ("2".equalsIgnoreCase(bean.getType())) {//已拔
            holder.type.setText("已拔");
        } else if ("3".equalsIgnoreCase(bean.getType())) {//未接
            holder.type.setText("未接");
        }
        holder.isCheck.setChecked(bean.isCheck());
        holder.isCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onClick(v,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return callMsgBeanList.size();
    }

    public void setData(List<CallMsgBean> callMsgBeanList) {
        if (callMsgBeanList != null) {
            this.callMsgBeanList = callMsgBeanList;
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    public class CallMsgViewHolder extends RecyclerView.ViewHolder {

        private TextView person;
        private TextView address;
        private TextView type;
        private TextView date;
        private CheckBox isCheck;

        public CallMsgViewHolder(View itemView) {
            super(itemView);
            person = (TextView) itemView.findViewById(R.id.callMsgPerson);
            address = (TextView) itemView.findViewById(R.id.callMsgAddress);
            type = (TextView) itemView.findViewById(R.id.callMsgType);
            date = (TextView) itemView.findViewById(R.id.callMsgDate);
            isCheck= (CheckBox) itemView.findViewById(R.id.isCheck);
        }
    }
}
