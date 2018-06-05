package info.upump.questionnairehis;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import info.upump.questionnairehis.db.QuestionDAO;
import info.upump.questionnairehis.entity.Answer;
import info.upump.questionnairehis.entity.Interval;
import info.upump.questionnairehis.entity.Question;

public class CheckActivity extends AppCompatActivity {

    private TextView goodAnswerText, badAnswerText, questionText;
    private ImageView img;
    private RadioGroup answersGroup;
    private LinearLayout answerLiner;
    private List<Question> questionList = new ArrayList<>();
    private List<Answer> currentAnswerList = new ArrayList<>();
    private int number;
    private int good;
    private int bad;
    private static final String CATEGORY = "cat";
    private static final String START = "start";
    private static final String FINISH = "finish";
    private static final String imgEmpty = "имя картинки";
    private Interval interval;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 100) {

                checkAnswer((Integer) msg.obj);
            }
        }
    };

    public static Intent createIntent(Context context, Interval interval) {
        Intent intent = new Intent(context, CheckActivity.class);
        intent.putExtra(START, interval.getStart());
        intent.putExtra(FINISH, interval.getFinish());
        intent.putExtra(CATEGORY, interval.getCategory());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        interval = new Interval();
        interval.setCategory(getIntent().getStringExtra(CATEGORY));
        interval.setStart(getIntent().getIntExtra(START, 1));
        interval.setFinish(getIntent().getIntExtra(FINISH, 26));
        setTitle(String.format(getString(R.string.title_interval_holder), interval.getStart(), interval.getFinish()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        goodAnswerText = findViewById(R.id.check_activity_good_text_view);
        badAnswerText = findViewById(R.id.check_activity_bad_text_view);
        questionText = findViewById(R.id.check_activity_question_text_view);
        answersGroup = findViewById(R.id.check_activity_answers_group);
        img = findViewById(R.id.check_activity_img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ImageActivity.createIntent(getApplicationContext(), questionList.get(number).getImg());
                startActivity(intent);
            }
        });

        answersGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                handler.removeMessages(100);
                handler.sendMessageDelayed(handler.obtainMessage(100, checkedId), 500);

            }
        });
        initQuestions();
        start();
    }

    private void checkAnswer(int checkedId) {
        Answer answer = currentAnswerList.get(checkedId);
        String s;
        if (answer.getRight() == 1) {
            goodAnswerText.setText(String.valueOf(++good));
            s = getString(R.string.toast_right);
        } else {
            badAnswerText.setText(String.valueOf(++bad));
            s = getString(R.string.toast_false);
        }
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        if (number < questionList.size() - 1) {
            start();
        } else startResult();

    }

    private void startResult() {
        final String title = getString(R.string.title_result);
        String message = String.format(getString(R.string.title_count_result), goodAnswerText.getText().toString());
        String button1String = getString(R.string.title_finish);
        String button2String = getString(R.string.title_restart);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);  // заголовок
        builder.setMessage(message); // сообщение
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                exit();
            }
        });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                reset();
                start();
            }
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void reset() {
        initQuestions();
    }

    private void start() {
        number++;
        Question question = questionList.get(number);
        questionText.setText(question.getBody());
        setImage();
        currentAnswerList = question.getAnswers();

        createBoxes();
    }

    private void createBoxes() {
        answersGroup.removeAllViews();
        int id = 0;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 15, 0, 0);
        for (Answer a : currentAnswerList) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setLayoutParams(lp);
            radioButton.setText(a.getBody());
            radioButton.setPadding(4, 4, 4, 4);
            radioButton.setTextColor(getResources().getColor(R.color.cardview_dark_background));
            radioButton.setId(id);
            answersGroup.addView(radioButton);
            id++;
        }
    }

    private void setImage() {
        System.out.println(1);
        RequestOptions options = new RequestOptions()
                .transforms(new RoundedCorners(50))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        String imgStr = questionList.get(number).getImg();
        System.out.println(imgStr);
        if(!imgStr.equals(imgEmpty)){
            img.setVisibility(View.VISIBLE);
            int ident = getResources().getIdentifier(imgStr, "drawable", getPackageName());
            Glide.with(this).load(ident).apply(options).into(img);
        } else img.setVisibility(View.GONE);

    }

    private void initQuestions() {
        good = 0;
        bad = 0;
        number = -1;
        badAnswerText.setText(String.valueOf(bad));
        goodAnswerText.setText(String.valueOf(good));
        QuestionDAO questionDAO = new QuestionDAO(this);
        questionList = questionDAO.getQuestionsInterval(interval);
    }

    private void exit() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            exit();
        }
        return super.onOptionsItemSelected(item);
    }
}
