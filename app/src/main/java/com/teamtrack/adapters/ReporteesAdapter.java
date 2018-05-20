package com.teamtrack.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamtrack.R;
import com.teamtrack.listeners.OnItemSelectedListener;
import com.teamtrack.model.Reportees;

import java.util.List;

public class ReporteesAdapter extends RecyclerView.Adapter<ReporteesAdapter.ViewHolder> {

    private List<Reportees> reporteesList;
    private OnItemSelectedListener listener;
    private String loadedFrom;

    public ReporteesAdapter(List<Reportees> reporteesList, OnItemSelectedListener listener, String loadedFrom) {
        this.reporteesList = reporteesList;
        this.listener = listener;
        this.loadedFrom = loadedFrom;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReporteesAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reportees, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Reportees data = reporteesList.get(holder.getAdapterPosition());

        holder.lnrReportee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemSelected(holder.getAdapterPosition(),loadedFrom);
            }
        });

        holder.setValues(data);
    }

    @Override
    public int getItemCount() {
        return reporteesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvReporteeName;
        LinearLayout lnrReportee;

        public ViewHolder(View itemView) {
            super(itemView);
            tvReporteeName = itemView.findViewById(R.id.tv_reportee_name);
            lnrReportee = itemView.findViewById(R.id.lnr_reportees_list);
        }

        public void setValues(Reportees data) {
            tvReporteeName.setText(data.getEmpName());
        }

    }
}