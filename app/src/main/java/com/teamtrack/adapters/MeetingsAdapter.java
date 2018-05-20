package com.teamtrack.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamtrack.R;
import com.teamtrack.database.tables.Meetings;
import com.teamtrack.listeners.OnItemSelectedListener;

import java.util.List;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.ViewHolder> {

    private List<Meetings> scheduleList;
    private OnItemSelectedListener listener;
    private String from;

    public MeetingsAdapter(List<Meetings> scheduleList, OnItemSelectedListener listener, String from) {
        this.scheduleList = scheduleList;
        this.listener = listener;
        this.from = from;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeetingsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedules, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Meetings data = scheduleList.get(holder.getAdapterPosition());

        if (from.equalsIgnoreCase("SALES")) {
            holder.visibleAdminView(View.GONE);
        } else {
            if (data.getStatus().equalsIgnoreCase("Completed")) {
                holder.visibleAdminView(View.VISIBLE);
            }
        }
        holder.setValues(data);

        holder.lnrAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemSelected(holder.getAdapterPosition(), "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCustomerName, tvDescription, tvLocation, tvRemarks, tvSalesLocation, tvSalesTime;
        LinearLayout lnrAppointment;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvRemarks = itemView.findViewById(R.id.tv_remarks);
            tvSalesLocation = itemView.findViewById(R.id.tv_location_sales);
            tvSalesTime = itemView.findViewById(R.id.tv_location_sales_time);
            lnrAppointment = itemView.findViewById(R.id.lnr_schedule_list);
        }

        @SuppressWarnings("deprecation")
        public void setValues(Meetings data) {
            tvCustomerName.setText(data.getCustomerName());
            tvDescription.setText(data.getDescription());
            tvLocation.setText(data.getLocation());

            if (from.equalsIgnoreCase("SALES")) {
                if (data.getStatus().equalsIgnoreCase("Completed")) {
                    lnrAppointment.setEnabled(false);
                    lnrAppointment.setClickable(false);
                    lnrAppointment.setAlpha(0.5f);
                }
            } else {
                tvRemarks.setText(data.getRemarks());
                tvSalesLocation.setText(data.getSales_location());
                tvSalesTime.setText(data.getSales_location_time());
                tvRemarks.setAlpha(0.5f);
                tvSalesLocation.setAlpha(0.5f);
                tvSalesTime.setAlpha(0.5f);
            }

        }

        private void visibleAdminView(int visibility) {
            tvRemarks.setVisibility(visibility);
            tvSalesTime.setVisibility(visibility);
            tvSalesLocation.setVisibility(visibility);
        }
    }
}