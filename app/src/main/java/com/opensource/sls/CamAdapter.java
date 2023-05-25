package com.opensource.sls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CamAdapter extends RecyclerView.Adapter<CamAdapter.ViewHolder>{
    Context context;

    ArrayList<CamItem> items = new ArrayList<CamItem>();
    CamAdapter.OnItemClickListener listener;
    CamAdapter.OnItemLongClickListener listener2;

    public interface OnItemClickListener {
        void onItemClick(CamAdapter.ViewHolder holder, View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(CamAdapter.ViewHolder holder, View view, int position);
    }

    public CamAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public CamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.cam_item, parent, false);
        return new CamAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CamAdapter.ViewHolder holder, int position) {
        CamItem item = items.get(position);
        holder.setItem(item);
        holder.setOnItemClickListener(listener);
        holder.setOnItemLongClickListener(listener2);
    }

    public void addItem(CamItem item) {
        items.add(item);
    }

    public void addItems(ArrayList<CamItem> items) {
        this.items = items;
    }

    public void deleteItem(CamItem item) {
        items.remove(item);
    }

    public void deleteAll() {
        items.clear();
    }

    public CamItem getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(CamAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(CamAdapter.OnItemLongClickListener listener2) {
        this.listener2 = listener2;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        CamAdapter.OnItemClickListener listener;
        CamAdapter.OnItemLongClickListener listener2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    if(listener != null) {
                        listener.onItemClick(CamAdapter.ViewHolder.this, view, position);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getBindingAdapterPosition();
                    if(listener2 != null) {
                        listener2.onItemLongClick(CamAdapter.ViewHolder.this, view, position);
                    }
                    return true;
                }
            });
        }

        public void setItem(CamItem item) {
            //camName.setText(item.getName());
        }

        public void setOnItemClickListener(CamAdapter.OnItemClickListener listener) {
            this.listener = listener;
        }
        public void setOnItemLongClickListener(CamAdapter.OnItemLongClickListener listener2) {
            this.listener2 = listener2;
        }
    }
}
