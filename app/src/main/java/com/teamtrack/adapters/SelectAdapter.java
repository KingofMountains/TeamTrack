package com.teamtrack.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamtrack.R;
import com.teamtrack.listeners.OnItemSelect;
import com.teamtrack.listeners.OnItemSelectedListener;

import java.util.List;

public class SelectAdapter<T> extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {

    private List<? extends T> selectList;
    private OnItemSelectedListener listener;

    public SelectAdapter(List<T> selectList, OnItemSelectedListener listener) {
        this.selectList = selectList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select,
                parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.lnrSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemSelected(holder.getAdapterPosition(), "");
            }
        });

        holder.setValues((OnItemSelect) selectList.get(position));
    }

    @Override
    public int getItemCount() {
        return selectList.size();
    }

    public class ViewHolder<T> extends RecyclerView.ViewHolder {

        TextView tvName;
        LinearLayout lnrSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            lnrSelect = itemView.findViewById(R.id.lnr_select_list);
        }

        public void setValues(OnItemSelect data) {
            tvName.setText(data.getName());
        }
    }
}