package info.upump.questionnairehis.filter;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import info.upump.questionnairehis.adapter.QuestionAdapter;
import info.upump.questionnairehis.entity.Question;


/**
 * Created by explo on 15.10.2017.
 */

public class CategoryFilter extends Filter {
    private final List<Question> inList;
    private List<Question> outList;
    private QuestionAdapter questionAdapter;


    public CategoryFilter(List<Question> inList, QuestionAdapter questionAdapter) {
        this.inList = inList;
        this.outList = new ArrayList<>();
        this.questionAdapter = questionAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        outList = new ArrayList<>();
        System.out.println(constraint);
        FilterResults filterResults = new FilterResults();
        if (constraint.toString().trim().equals("")) {
            outList = inList;
        } else {
            System.out.println(inList.size());
            System.out.println(outList.size());
            System.out.println(constraint);

            for (Question question : inList) {
                if (question.getBody().toLowerCase().contains(constraint.toString().toLowerCase())) {
              //  if (question.getBody().toLowerCase().contains(s.toLowerCase())) {
                    outList.add(question);

                }
                System.out.println(inList.size());
                System.out.println(outList.size());

            }
        }
        filterResults.values = outList;
        filterResults.count = outList.size();

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        //  questionAdapter.setList(null);

        questionAdapter.setList(outList);
        questionAdapter.notifyDataSetChanged();
    }
}
