package com.clfsjkj.govcar.index;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.clfsjkj.govcar.R;

import java.util.ArrayList;
import java.util.List;


public class FunctionAdapter extends RecyclerView.Adapter {

    private List<FunctionItem> data = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private Boolean isEdit = false;

    public FunctionAdapter(Context context, @NonNull List<FunctionItem> data) {
        this.context = context;
        if (data != null) {
            this.data = data;
        }
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (0 == viewType) {
            holder = new TitleViewHolder(inflater.inflate(R.layout.layout_function_text, parent, false));
        } else {
            holder = new FunctionViewHolder(inflater.inflate(R.layout.layout_grid_item, parent, false));
        }
        return holder;
    }

    //----------------------------------------------------------自己加的点击事件------------------------------------------------------------------
    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    public void setEdit(Boolean isEdit){
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }
//----------------------------------------------------------自己加的点击事件------------------------------------------------------------------

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,final int position) {
        if (0 == getItemViewType(position)) {
            TitleViewHolder holder = (TitleViewHolder) viewHolder;
            holder.text.setText(data.get(position).name);
        } else {
            final int index = position;
            FunctionViewHolder holder = (FunctionViewHolder) viewHolder;
            FunctionItem fi = data.get(position);
            setImage(fi.imageUrl,holder.iv);
            holder.text.setText(fi.name);
           if (isEdit){
               holder.btn.setVisibility(View.VISIBLE);
           }else {
               holder.btn.setVisibility(View.GONE);
           }

            holder.btn.setImageResource(fi.isSelect ? R.drawable.ic_block_selected : R.drawable.ic_block_add);
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FunctionItem f = data.get(index);
                    if (!f.isSelect) {
                        if (listener != null) {
                            if (listener.add(f)) {
                                f.isSelect = true;
                                notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }

//----------------------------------------------------------自己加的点击事件------------------------------------------------------------------
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(view, position);
                }
            });
        }

//----------------------------------------------------------自己加的点击事件------------------------------------------------------------------
    }

    public void setImage(String url, ImageView iv) {
        try {
            int rid = context.getResources().getIdentifier(url,"drawable",context.getPackageName());
            iv.setImageResource(rid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).isTitle ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        public TitleViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    private class FunctionViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv, btn;
        private TextView text;

        public FunctionViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            text = (TextView) itemView.findViewById(R.id.text);
            btn = (ImageView) itemView.findViewById(R.id.btn);
        }
    }

    public interface OnItemAddListener {
        boolean add(FunctionItem item);
    }

    private OnItemAddListener listener;

    public void setOnItemAddListener(OnItemAddListener listener) {
        this.listener = listener;
    }



}