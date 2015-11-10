package cpm.linpeng.adapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.linpeng.baidunewssearcht2.R;
import com.linpeng.domain.Photo;
import com.linpeng.netutil.GetBitmapUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 图片列表适配器
 * 注：此处将一张或两张图片分为一组（长宽比大于1.6的为一张一组）
 * 此处的图片获取是异步的
 * map 缓存了以获取图片
 * @author linpeng123l
 *
 */
public class PhotoListViewAdapter extends BaseAdapter{

	private Context context;
	private List<Photo[]> photos;
	private Map<String, SoftReference<Bitmap>> map = new HashMap<String, SoftReference<Bitmap>>();

	public PhotoListViewAdapter(Context context , List<Photo> photos) {
		super();
		if(photos==null){
			photos = new ArrayList<Photo>();
		}
		this.photos = sortPhotos(photos);
		this.context = context;
	}

	/**
	 * 图片分组，长宽比例大于1.6的单独一行显示
	 * @param sortPhotos
	 * @return
	 */
	private List<Photo[]> sortPhotos(List<Photo> sortPhotos) {
		List<Photo[]> photos = new ArrayList<Photo[]>();
		Photo[] tempPhotoArray = new Photo[2];
		int tempIndex = 0;
		for(Photo photo:sortPhotos){
			if(photo.getWidth()/photo.getHeight()>1.6){
				photos.add(new Photo[]{photo});
			}else{
				tempPhotoArray[tempIndex] = photo;
				if(tempIndex==1){
					photos.add(tempPhotoArray);
					tempIndex = 0;
					tempPhotoArray = new Photo[2];
				}else{
					tempIndex++;
				}
			}
		}
		//如果是两个则一定已经加入
		if(tempIndex!=0){
			photos.add(new Photo[]{tempPhotoArray[0]});
		}
		return photos;
	}

	
	public void addPhotos(List<Photo> newPhotos) {
		this.photos.addAll(sortPhotos(newPhotos));
	}

	public List<Photo[]> getPhotos() {
		return photos;
	}

	@Override
	public int getCount() {
		Log.i("sa", photos.size()+"");
		return photos.size();
	}
	@Override
	public Object getItem(int position) {
		return photos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = View.inflate(context, R.layout.listview_fragment_photo, null);
		} 
		Photo[] photoArray = photos.get(position);
		if(photoArray.length == 1){
			getImageViewByTag(convertView, 1).setVisibility(View.GONE);
		}

		for(int i = 0;i<photoArray.length;i++){
			ImageView imageView = getImageViewByTag(convertView, i);
			Photo photo = photoArray[i];
			imageView.setTag(photo.getPhotoUrl());
			imageView.setImageResource(R.drawable.img_temp);
			imageView.setVisibility(View.VISIBLE);

			if(map.get(photo.getPhotoUrl())!=null&&map.get(photo.getPhotoUrl()).get()!=null){
				imageView.setImageBitmap(map.get(photo.getPhotoUrl()).get());
			}else{
				MyAsyncTaskGetBitmap myAsyncTaskGetBitmap = new MyAsyncTaskGetBitmap();
				myAsyncTaskGetBitmap.imageView = imageView;
				myAsyncTaskGetBitmap.targetUrl = photo.getPhotoUrl();
				myAsyncTaskGetBitmap.execute("");
			}
		}
		//如果有两行则设置他们的weight比例
		if(photoArray.length==2){
			((LayoutParams)getImageViewByTag(convertView, 1).getLayoutParams()).weight = getWeight(photoArray);
		}
		return convertView;
	}

	/**
	 * 以第一张图片为标准获取第二张图片的所占比例
	 * @param photo
	 * @return
	 */
	public float getWeight(Photo[] photo){
		Photo photo1 = photo[0];
		Photo photo2 = photo[1];
		//根据改行的宽高比设置他们站的宽度比
		if(photo1.getWidth()!=0&&photo1.getHeight()!=0&&photo2.getWidth()!=0&&photo2.getHeight()!=0){
			float weight = (photo2.getWidth()/photo2.getHeight())/(photo1.getWidth()/photo1.getHeight());
			return weight;
		}
		return 1;
	}


	/**
	 * 通过一个tag值获取对应的iamgeview，一行图片最多2个imageview
	 * @param view 
	 * @param tag 0 1分别对应2个imageview
	 * @return
	 */
	public ImageView getImageViewByTag(View view , int tag){
		switch (tag) {
		case 0:
			return (ImageView) view.findViewById(R.id.fragment_photo_listview_image1);
		case 1:
			return (ImageView) view.findViewById(R.id.fragment_photo_listview_image2);
		default:
			return (ImageView) view.findViewById(R.id.fragment_photo_listview_image1);
		}
	}

	/**
	 * 异步获取图片并显示
	 * @author linpeng123l
	 *
	 */
	public class MyAsyncTaskGetBitmap extends AsyncTask<String, String, Bitmap>{
		String targetUrl;
		ImageView imageView ; 
		@Override
		protected Bitmap doInBackground(String... params) {
			if(targetUrl==imageView.getTag()){
				Bitmap bitmap = GetBitmapUtil.getBitmapByUrl(targetUrl);
				return bitmap;
			}
			return null;
		}
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if(bitmap!=null){
				map.put(targetUrl, new SoftReference<Bitmap>(bitmap));
				if(targetUrl.equals(imageView.getTag())){
					imageView.setImageBitmap(bitmap);
				}
			}
			super.onPostExecute(bitmap);
		}
	}
}
