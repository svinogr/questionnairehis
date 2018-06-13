package info.upump.questionnairehis.model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import info.upump.questionnairehis.CheckActivity;
import info.upump.questionnairehis.R;
import info.upump.questionnairehis.entity.Interval;


public class IntervalVH extends RecyclerView.ViewHolder {
    private TextView textView, number;
    private Context context;
    private View itemView;
    private Interval interval;

    public IntervalVH(View itemView) {
        super(itemView);
        this.textView = itemView.findViewById(R.id.interval_card_text);
        this.number = itemView.findViewById(R.id.interval_card_number);
        this.context = itemView.getContext();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CheckActivity.createIntent(context, interval);
                context.startActivity(intent);
            }
        });
    }

    public void bind(Interval interval) {
        this.interval = interval;
        String text = String.format(context.getString(R.string.title_interval_holder), interval.getStart()+1, interval.getFinish());
        String numText = String.format(context.getString(R.string.interval_title_text), getAdapterPosition()+1);
        textView.setText(text);
        number.setText(numText);
    }
}
