package info.upump.questionnairehis.model;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import info.upump.questionnairehis.ImageActivity;
import info.upump.questionnairehis.R;
import info.upump.questionnairehis.entity.Answer;
import info.upump.questionnairehis.entity.Question;


public class QuestionViewHolder extends RecyclerView.ViewHolder {
    public TextView number;
    public TextView questionBody;
    public ImageView img;
    public LinearLayout linearLayoutAnswer;
    public View comDiv;
    private Context context;
    private Question question;
    private static final String imgEmpty = "имя картинки";

    public QuestionViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        linearLayoutAnswer = itemView.findViewById(R.id.list_answer);
        number = itemView.findViewById(R.id.number);
        questionBody = itemView.findViewById(R.id.question);
        img = itemView.findViewById(R.id.img);
        comDiv = itemView.findViewById(R.id.com_div);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ImageActivity.createIntent(context, question.getImg());
                context.startActivity(intent);
            }
        });
    }

    public void bind(Question question) {
        this.question = question;
        Resources resources = context.getResources();
        String numberStr = String.format(resources.getString(R.string.number_question), getAdapterPosition() + 1);
        number.setText(numberStr);

        questionBody.setText(question.getBody());
        addAnswers();
        setImg();
    }

    private void setImg() {
        RequestOptions options = new RequestOptions()
                .transforms(new RoundedCorners(50))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        String imgStr = question.getImg();
        if(!imgStr.equals(imgEmpty)){
            img.setVisibility(View.VISIBLE);
            int ident = context.getResources().getIdentifier(imgStr, "drawable", context.getPackageName());
            Glide.with(context).load(ident).apply(options).into(img);
        } else img.setVisibility(View.GONE);

    }

    private void addAnswers() {
        linearLayoutAnswer.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (Answer answer : question.getAnswers()) {
            TextView text = new CheckedTextView(context);
            switch (answer.getRight()) {
                case 1:
                    text.setLayoutParams(layoutParams);
                    text.setTypeface(null, Typeface.BOLD_ITALIC);
                    break;
                case 0:
                    text.setTypeface(null, Typeface.ITALIC);
                    break;
                case -1:
                    break;
            }
            text.setTextColor(Color.parseColor("#FF424242"));

            text.setText(String.format(context.getString(R.string.answer_text), answer.getBody()));
            linearLayoutAnswer.addView(text);
        }
    }

}
