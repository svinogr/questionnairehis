package info.upump.questionnairehis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import info.upump.questionnairehis.adapter.IntervalAdapter;
import info.upump.questionnairehis.db.QuestionDAO;
import info.upump.questionnairehis.entity.Interval;

public class ChooseInterval extends AppCompatActivity {
    private String category;
    private static final String CATEGORY = "cat";
    private RecyclerView recyclerView;
    private List<Interval> intervalList = new ArrayList<>();
    private IntervalAdapter intervalAdapter;
    private final static int intervalCards = 25;


    public static Intent createIntent(Context context, String category) {
        Intent intent = new Intent(context, ChooseInterval.class);
        intent.putExtra(CATEGORY, category);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_interval);
        setTitle(getString(R.string.title_choose_questions));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.choose_activity_rec_view);
        category = getIntent().getStringExtra(CATEGORY);

        initIntervals();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        intervalAdapter = new IntervalAdapter(intervalList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(intervalAdapter);
    }

    private void initIntervals() {
        QuestionDAO questionDAO = new QuestionDAO(this);
        int count = questionDAO.getCount();

        double countCardsD = (double) count / intervalCards;
        int one = (int) countCardsD;
        double two = countCardsD -one;
        int countCards;
        if( two == 0 ){
            countCards = one;
        } else  countCards = one+1;
        System.out.println(countCardsD+" "+one+" "+ two+ " "+ countCards);

        Interval interval;
        for (int i = 0; i < countCards; i++) {
            interval = new Interval();
            interval.setCategory(category);
            interval.setStart((i * intervalCards));
            if (count <= interval.getStart() + intervalCards) {
                int finish = count;
                interval.setFinish(finish);
            } else interval.setFinish(interval.getStart() + intervalCards);
            intervalList.add(interval);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
