package info.upump.questionnairehis.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import info.upump.questionnairehis.R;
import info.upump.questionnairehis.entity.Interval;
import info.upump.questionnairehis.model.IntervalVH;


public class IntervalAdapter extends RecyclerView.Adapter<IntervalVH> {
    private List<Interval> intervals;

    public IntervalAdapter(List<Interval> intervals) {
        this.intervals = intervals;
    }

    @NonNull
    @Override
    public IntervalVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.interval_card_item, parent, false);
        return new IntervalVH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull IntervalVH holder, int position) {
        holder.bind(intervals.get(position));
    }

    @Override
    public int getItemCount() {
        return intervals.size();
    }
}
