package cn.ddshcool.services.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.ddshcool.main.R;


public class StatusGridImgsAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> thumbnail_images;
	private ImageLoader imageLoader;

	public StatusGridImgsAdapter(Context context, ArrayList<String> thumbnail_images) {
		this.context = context;
		this.thumbnail_images = thumbnail_images;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return thumbnail_images.size();
	}

	@Override
	public String getItem(int position) {
		return thumbnail_images.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_grid_image, null);
			holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		GridView gv = (GridView) parent;
		int horizontalSpacing = gv.getHorizontalSpacing();
		int numColumns = gv.getNumColumns();
		int itemWidth = (gv.getWidth() - (numColumns-1) * horizontalSpacing
				- gv.getPaddingLeft() - gv.getPaddingRight()) / numColumns;

		LayoutParams params = new LayoutParams(itemWidth, itemWidth);
		holder.iv_image.setLayoutParams(params);

		imageLoader.displayImage(getItem(position), holder.iv_image);
		
		return convertView;
	}

	public static class ViewHolder {
		public ImageView iv_image;
	}

}
