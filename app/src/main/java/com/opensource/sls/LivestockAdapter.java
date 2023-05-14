package com.opensource.sls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LivestockAdapter extends RecyclerView.Adapter<LivestockAdapter.ViewHolder>{
    Context context;

    ArrayList<LivestockItem> items = new ArrayList<LivestockItem>();
    OnItemClickListener listener;
    OnItemLongClickListener listener2;

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(ViewHolder holder, View view, int position);
    }

    public LivestockAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.livestock_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LivestockItem item = items.get(position);
        holder.setItem(item);
        holder.setOnItemClickListener(listener);
        holder.setOnItemLongClickListener(listener2);
    }

    public void addItem(LivestockItem item) {
        items.add(item);
    }

    public void addItems(ArrayList<LivestockItem> items) {
        this.items = items;
    }

    public void deleteItem(LivestockItem item) {
        items.remove(item);
    }

    public LivestockItem getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener2) {
        this.listener2 = listener2;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView livestockName;
        TextView isPregnancy;
        OnItemClickListener listener;
        OnItemLongClickListener listener2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            isPregnancy = itemView.findViewById(R.id.is_pregnancy);
            livestockName = itemView.findViewById(R.id.livestock_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    if(listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getBindingAdapterPosition();
                    if(listener2 != null) {
                        listener2.onItemLongClick(ViewHolder.this, view, position);
                    }
                    return true;
                }
            });
        }

        public void setItem(LivestockItem item) {
            livestockName.setText(item.getName());
            if(item.is_pregnancy)
                isPregnancy.setVisibility(View.VISIBLE);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
        public void setOnItemLongClickListener(OnItemLongClickListener listener2) {
            this.listener2 = listener2;
        }
    }
}
