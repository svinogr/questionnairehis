package info.upump.questionnairehis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import info.upump.questionnairehis.adapter.QuestionAdapter;
import info.upump.questionnairehis.db.MyLoader;
import info.upump.questionnairehis.entity.Question;

/**
 * Created by explo on 11.10.2017.
 */

public class CaptainFragment extends Fragment implements AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<List<Question>> {
    private List<Question> list = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private SearchView searchView;
    protected String CATEGORY = "капитан";
    public static String TAG = "cap";
    LinearLayoutManager linearLayoutManager;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 100) {
                try {

                    int number = Integer.parseInt((String) msg.obj);
                    if (number > recyclerView.getAdapter().getItemCount()) {
                        number = recyclerView.getAdapter().getItemCount();
                    }
                    if (number < 1) {
                        number = 1;
                    }
                    linearLayoutManager.scrollToPositionWithOffset(number - 1, 0);

                } catch (NumberFormatException e) {
                    questionAdapter.filter(((String) msg.obj).trim());

                } catch (IndexOutOfBoundsException e) {
                    recyclerView.stopScroll();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_serch_category, container, false);

        searchView = root.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handler.removeMessages(100);
                handler.sendMessageDelayed(handler.obtainMessage(100, newText), 250);
                return true;
            }
        });

        progressBar = root.findViewById(R.id.progressSearchCategory);
        progressBar.setVisibility(progressBar.VISIBLE);

       linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView = root.findViewById(R.id.listQuestionSearchCategory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        questionAdapter = new QuestionAdapter(getActivity(), list);
        recyclerView.setAdapter(questionAdapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.putString("category", CATEGORY);
        getLoaderManager().initLoader(1, bundle, this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Question question = (Question) adapterView.getItemAtPosition(i);
    }


    @Override
    public Loader<List<Question>> onCreateLoader(int id, Bundle args) {
        return new MyLoader(getContext(), args.getString("category"));
    }

    @Override
    public void onLoadFinished(Loader<List<Question>> loader, List<Question> data) {
        list.clear();
        list.addAll(data);
        questionAdapter.notifyDataSetChanged();
        progressBar.setVisibility(progressBar.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Question>> loader) {

    }

    public String getTAG() {
        return TAG;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getActivity().setTitle(getString(R.string.title_cap_fr));
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.mailto:
                intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_mail)});
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.mail_subject));
                intent.putExtra(Intent.EXTRA_TEXT, "");
                //email.setType("message/rfc822");
                intent.setType("plain/text");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                break;
            case R.id.checkit:
                intent = ChooseInterval.createIntent(getContext(),CATEGORY);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
