package com.chris.hidedocument.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chris.hidedocument.R;
import com.chris.hidedocument.listener.OnItemClickListener;
import com.chris.hidedocument.model.FileInfoBean;
import com.chris.hidedocument.tools.ImageUtils;

import java.util.List;


/**
 * Created by zoulx on 2016/3/21.
 */
public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.MainRecyClerViewHolder> {

    private List<FileInfoBean> fileInfoBeanList;
    private OnItemClickListener mOnItemClickListener;
    private int thumbnailSize = 80;//缩略大小

    public ImageRecyclerAdapter(List<FileInfoBean> fileInfoBeanList) {
        this.fileInfoBeanList = fileInfoBeanList;
    }

    @Override
    public MainRecyClerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        MainRecyClerViewHolder viewHolder = new MainRecyClerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainRecyClerViewHolder holder, final int position) {
        FileInfoBean bean = fileInfoBeanList.get(position);
        holder.isCheck.setChecked(bean.isCheck());
        holder.isCheck.setClickable(!bean.isShow());
        holder.isCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(v, position);
            }
        });
        holder.clover.setImageBitmap(ImageUtils.decodeSampledBitmapFromPath(bean.getFilePath(), thumbnailSize, thumbnailSize));
        holder.filePath.setText(bean.getFilePath());
        holder.fileName.setText(bean.getFileName());
        holder.fileSize.setText(bean.getFileInfo());
    }

    @Override
    public int getItemCount() {
        return this.fileInfoBeanList.size();
    }

    public class MainRecyClerViewHolder extends RecyclerView.ViewHolder {
        private ImageView clover;
        private TextView fileName;
        private TextView fileSize;
        private TextView filePath;
        private CheckBox isCheck;
        private RelativeLayout itemBorder;

        public MainRecyClerViewHolder(View itemView) {
            super(itemView);
            clover = (ImageView) itemView.findViewById(R.id.clover);
            isCheck = (CheckBox) itemView.findViewById(R.id.isCheck);
            itemBorder = (RelativeLayout) itemView.findViewById(R.id.mainItemBorder);
            fileName= (TextView) itemView.findViewById(R.id.fileName);
            fileSize= (TextView) itemView.findViewById(R.id.fileSize);
            filePath= (TextView) itemView.findViewById(R.id.filePath);
        }
    }

    public void setData(List<FileInfoBean> fileInfoBeanList) {
        if (fileInfoBeanList != null) {
            this.fileInfoBeanList = fileInfoBeanList;
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener != null) {
            this.mOnItemClickListener = onItemClickListener;
        }
    }
}
