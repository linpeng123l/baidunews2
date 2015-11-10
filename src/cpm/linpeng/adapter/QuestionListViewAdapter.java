package cpm.linpeng.adapter;

import java.util.ArrayList;
import java.util.List;

import com.linpeng.baidunewssearcht2.R;
import com.linpeng.domain.Question;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuestionListViewAdapter extends BaseAdapter{

	private List<Question> questions;
	private Context context;

	public QuestionListViewAdapter( Context context,List<Question> questions) {
		super();
		this.questions = questions;
		if(questions==null){
			this.questions = new ArrayList<Question>();
		}
		this.context = context;
	}

	public void addQuestions(List<Question> newQuestions){
		this.questions.addAll(newQuestions);
	}

	public List<Question> getQuestions() {
		return questions;
	}

	@Override
	public int getCount() {
		return questions.size();
	}

	@Override
	public Object getItem(int position) {
		return questions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Question question = questions.get(position);
		if(convertView == null){
			convertView = View.inflate(context, R.layout.listview_fragment_question, null);
		}
		getTextViewByViewAndId(convertView, R.id.fragment_question_listview_title).setText(getSpannableStringBuilderByEm(question.getTitle()));
		getTextViewByViewAndId(convertView, R.id.fragment_question_listview_details).setText(getSpannableStringBuilderByEm(question.getDetails()));
		getTextViewByViewAndId(convertView, R.id.fragment_question_listview_answer_name).setText(question.getAnswerName());
		getTextViewByViewAndId(convertView, R.id.fragment_question_listview_date).setText(question.getDate());
		getTextViewByViewAndId(convertView, R.id.fragment_question_listview_agree_num).setText(question.getAgreeNum());

		View daRenView = convertView.findViewById(R.id.fragment_question_listview_image_is_da_ren);
		if(question.isDaRen()){
			daRenView.setVisibility(View.VISIBLE);
		}else{
			daRenView.setVisibility(View.GONE);
		}
		return convertView;
	}

	/**
	 * 通过一个包含字符串<em>...</em>的字符串返回一个不包含<em></em>的SpannableStringBuilder,且<em>内的字符设置颜色为绿色
	 * @param str 给定字符串
	 * @return
	 */
	public SpannableStringBuilder getSpannableStringBuilderByEm(String str){
		SpannableStringBuilder builder = new SpannableStringBuilder(str);
		ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#27A002"));
		while(builder.toString().contains("<em>")){
			if(builder.toString().indexOf("</em>")>builder.toString().indexOf("<em>")){
				builder.setSpan(foregroundColorSpan, builder.toString().indexOf("<em>"), builder.toString().indexOf("</em>"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			builder.delete(builder.toString().indexOf("<em>"), builder.toString().indexOf("<em>")+4);
			builder.delete(builder.toString().indexOf("</em>"), builder.toString().indexOf("</em>")+5);
		}
		return builder;
	}

	public TextView getTextViewByViewAndId(View view,int id){
		return (TextView)view.findViewById(id);
	}
}
