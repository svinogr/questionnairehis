package info.upump.questionnairehis.db;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import info.upump.questionnairehis.entity.Question;


/**
 * Created by explo on 09.10.2017.
 */

public class MyLoader extends AsyncTaskLoader<List<Question>> {
    private QuestionDAO questionDAO;
    private AnswerDAO answerDAO;
    private String category;
    private Cursor cursor;
    private Context context;

    public MyLoader(Context context) {
        super(context);
        questionDAO = new QuestionDAO(context);
        answerDAO = new AnswerDAO(context);
    }


    public MyLoader(Context context, String category) {
        this(context);
        this.category = category;

    }

    @Override
    public List<Question> loadInBackground() {
        QuestionDAO questionDAO = new QuestionDAO(context);
        List<Question> list =  questionDAO.getQuestions(category);
        return list;
    }

    private String stringToUpperCase(String s) {
        return s != null && s.length() != 0 ? s.substring(0, 1).toUpperCase() + s.substring(1) : null;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();

    }

    @Override
    public void deliverResult(List<Question> data) {
        super.deliverResult(data);
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }
}
