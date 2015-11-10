package com.linpeng.fragment;

import java.util.List;

import com.linpeng.baidunewssearcht2.R;
import com.linpeng.domain.Question;
import com.linpeng.netservice.GetQuestionService;

import cpm.linpeng.adapter.QuestionListViewAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class FragmentQuestion extends Fragment implements OnScrollListener,OnItemClickListener{

	private Activity activity;
	private ListView listView;
	private QuestionListViewAdapter adapter ;
	private boolean isLoading = true;
	private int page = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_question, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		activity = getActivity();
		listView = (ListView)activity.findViewById(R.id.fragment_question_listview); 
		adapter = new QuestionListViewAdapter(activity, null);
		listView.addFooterView(View.inflate(activity, R.layout.foot, null));
		listView.setAdapter(adapter);
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
		new MyAsnycTaskGetQuestion().execute(0);
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 异步获取百度知道列表类
	 * @author linpeng123l
	 *
	 */
	public class MyAsnycTaskGetQuestion extends AsyncTask<Integer, String, List<Question>>{
		@Override
		protected List<Question> doInBackground(Integer... pages) {
			List<Question> questions = GetQuestionService.getQuestionsByPage("谷歌",pages[0]);
			return questions;
		}
		@Override
		protected void onPostExecute(List<Question> questions) {
			isLoading = false;
			if(questions!=null&&questions.size()>0){
				adapter.addQuestions(questions);
				page++;
				adapter.notifyDataSetChanged();
			}
			super.onPostExecute(questions);
		}
		
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(totalItemCount<=firstVisibleItem+visibleItemCount+1&&!isLoading){
			isLoading = true;
			new MyAsnycTaskGetQuestion().execute(page);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(activity, "你点击了item", Toast.LENGTH_SHORT).show();
	}
}
