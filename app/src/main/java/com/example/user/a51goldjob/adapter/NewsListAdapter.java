package com.example.user.a51goldjob.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.bean.News;
import com.lidroid.xutils.BitmapUtils;

/**
 * 金融快讯列表适配器
 * @author yeq
 *
 */
public class NewsListAdapter extends BaseAdapter {

	private List<News> newsList;
	private LayoutInflater inflater;
	private String sourceLang;
	private String authorLang;

	private BitmapUtils bitmapUtils;
	private BitmapLoadCallBackAdapters.CornnerBitmapLoadCallBack cornnerBitmapLoadCallBack = new BitmapLoadCallBackAdapters.CornnerBitmapLoadCallBack();


	public NewsListAdapter(Context context, List<News> newsList) {
		this.newsList = newsList;
		this.inflater = LayoutInflater.from(context);
		this.bitmapUtils = new BitmapUtils(context, context.getExternalCacheDir().getAbsolutePath());
		this.sourceLang = context.getResources().getString(R.string.source);
		this.authorLang = context.getResources().getString(R.string.author);
	}

	public int getCount() {
		return newsList.size();
	}

	public Object getItem(int position) {
		return newsList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_news_list_item, null);
		}

		ImageView photourlImageView = (ImageView) convertView.findViewById(R.id.news_list_item_photourl_imageview);
		TextView previewText = (TextView) convertView.findViewById(R.id.news_list_item_description_txt);
		TextView titleTxt = (TextView) convertView.findViewById(R.id.news_list_item_title_txt);
		TextView pubdateText = (TextView) convertView.findViewById(R.id.news_list_item_time_txt);
		TextView authorText = (TextView) convertView.findViewById(R.id.news_list_item_author_txt);
		TextView sourceText = (TextView) convertView.findViewById(R.id.news_list_item_source_txt);

		News news = newsList.get(position);

		// 加载图片
		photourlImageView.setImageBitmap(null);
		bitmapUtils.display(photourlImageView, news.getPhotourl(), cornnerBitmapLoadCallBack); // 异步加载图片并带有图片本地缓存
		
		// 资讯标题
		titleTxt.setText(news.getTitle());

		// 资讯内容
		previewText.setText(news.getPreviewText());

		// 发布日期
		pubdateText.setText(news.getPubdate());

		// 来源
		sourceText.setText(String.format("%s%s", sourceLang, news.getSource()));

		// 编辑
		authorText.setText(String.format("%s%s", authorLang, news.getAuthor()));

		return convertView;
	}
}
