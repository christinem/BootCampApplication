package com.example.bootcampapplication;

import java.io.IOException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends ArrayAdapter<String> {
	private String[] imageURLArray;
	private LayoutInflater inflater;
	private String[] nameArray;

	public ImageAdapter(Context context, int textViewResourceId,
			String[] imageArray, String[] names) {
		super(context, textViewResourceId, imageArray);
		// TODO Auto-generated constructor stub

		inflater = ((Activity)context).getLayoutInflater();
		imageURLArray = imageArray;
		nameArray = names;
	}

	private static class ViewHolder {
		ImageView imageView;
		public String imageURL;
		public Object bitmap;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.list_single, null);

			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView)convertView.findViewById(R.id.img);
			convertView.setTag(viewHolder);
		}
        String imageURL = imageURLArray[position];
		viewHolder = (ViewHolder)convertView.getTag();
		viewHolder.imageURL = imageURLArray[position];
		new DownloadAsyncTask().execute(viewHolder);
		TextView txt = (TextView)convertView.findViewById(R.id.textView1);
		txt.setText(nameArray[position]);
		return convertView;
	}
	
	private class DownloadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

		@Override
		protected ViewHolder doInBackground(ViewHolder... params) {
			// TODO Auto-generated method stub
			//load image directly
			ViewHolder viewHolder = params[0];
			try {
				URL imageURL = new URL(viewHolder.imageURL);
				viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream());
			} catch (IOException e) {
				// TODO: handle exception
				Log.e("error", "Downloading Image Failed");
				viewHolder.bitmap = null;
			}
			
			return viewHolder;
		}
		
		@Override
		protected void onPostExecute(ViewHolder result) {
			// TODO Auto-generated method stub
			if (result.bitmap == null) {
				result.imageView.setImageResource(R.drawable.ic_launcher);
			} else {
				result.imageView.setImageBitmap((Bitmap) result.bitmap);
			}
		}
	}
}