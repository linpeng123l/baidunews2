package com.linpeng.netservice;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.linpeng.domain.Postbar;
import com.linpeng.util.FileUtil;
import com.linpeng.util.StringUtil;

public class GetPostbarService extends NetService{

	private final static String postbarUrl = "http://tieba.baidu.com/f?ie=utf-8&mo_device=1";

	/**
	 * 获取贴吧列表集合(从百度贴吧获取数据)
	 * 注：由于未找到json数据接口，此处直接解析html代码
	 * @param postBarName 贴吧名称
	 * @param page 页码
	 * @return 贴吧集合
	 */
	public static List<Postbar> getPostbarsByPage(String postBarName,int page){
		List<Postbar> postbars = new ArrayList<Postbar>();
		try{
			String kw = StringUtil.changeEncoding(postBarName, "ISO-8859-1");
			Document document = getDocumentByUrl(postbarUrl+"&kw="+kw+"&pn="+page*50);
			for(Element element:document.select(".tl_shadow").select(".j_common")){
				Postbar postbar = new Postbar();
				postbar.setTitle(element.select(".ti_title").get(0).text());
				if(element.select(".ti_abs").size()>0){
				    postbar.setDetails(element.select(".ti_abs").get(0).text());
				}
				postbar.setAuthor(element.select(".ti_author").get(0).text());
				postbar.setDate(element.select(".ti_time").get(0).text());
				postbar.setReplyNum(element.select(".btn_icon").get(0).text());
				postbar.setUrl(element.absUrl("href"));
				List<String> photoUrls = new ArrayList<String>();
				for(Element photoEle : element.select(".medias_item")){
					photoUrls.add(photoEle.child(0).absUrl("data-url").replace("amp;", ""));
				}
				postbar.setPhotoUrls(photoUrls);
				postbars.add(postbar);
			}
		}catch(Exception e){
			FileUtil.addFile(e.getStackTrace().toString());
			e.printStackTrace();
		}
		return postbars;
	}

}
