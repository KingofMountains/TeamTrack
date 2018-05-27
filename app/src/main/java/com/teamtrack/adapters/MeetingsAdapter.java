package com.teamtrack.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamtrack.R;
import com.teamtrack.listeners.OnItemSelectedListener;
import com.teamtrack.model.Meetings;

import java.util.List;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.ViewHolder> {

    private List<Meetings> scheduleList;
    private OnItemSelectedListener listener;

    public MeetingsAdapter(List<Meetings> scheduleList, OnItemSelectedListener listener) {
        this.scheduleList = scheduleList;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeetingsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedules, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Meetings data = scheduleList.get(holder.getAdapterPosition());

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

        TextView tvCustomerName, tvDescription, tvLocation, tvDate;
        LinearLayout lnrAppointment;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvDate = itemView.findViewById(R.id.tv_date);
            lnrAppointment = itemView.findViewById(R.id.lnr_schedule_list);
        }

        @SuppressWarnings("deprecation")
        public void setValues(Meetings data) {
            tvCustomerName.setText(data.getCustomerName());
            tvDescription.setText(data.getDescription());
            tvLocation.setText(data.getCustomerLocationName());
            tvDate.setText(data.getScheduledDate());
        }
    }
}