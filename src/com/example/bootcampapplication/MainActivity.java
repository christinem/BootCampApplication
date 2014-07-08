package com.example.bootcampapplication;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;



public class MainActivity extends Activity {

	//public static InputStream json;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
		new JsonAsync(this).execute();
		
       
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	/**
	 * Async Task for Grabbing Json Dara
	 * @author Christine
	 *
	 */
	private static class JsonAsync extends AsyncTask<Void, Void, Void> {
		
		public String json;
		public Activity activity;
		public ImageSearch search;
		private ListView titles;
		//public Result result;
		
		public JsonAsync(Activity activity) {
			this.activity = activity;
		}
		
		@Override
		protected Void doInBackground(Void...params) {
			try {
				URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=fuzzy%20monkey%27");
				InputStream stream = (InputStream) url.getContent();
				json = convertStreamToString(stream);
				
				Gson gson = new Gson();
				
				search = gson.fromJson(json, ImageSearch.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			
		}
		
		@Override
		protected void onPostExecute(Void result) {
			    final int N = search.responseData.results.length;
			    final String[] urls = new String[N];
			    final String[] names = new String[N];
		        final TextView[] myTextViews = new TextView[N]; // create an empty array;
		        
		        // add all urls into their own array
		        for (int i = 0; i < N; i++) {
		        	urls[i] = search.responseData.results[i].url;
		        }
		        
		     // add all filenames into their own array
		        for (int i = 0; i < N; i++) {
		        	names[i] = search.responseData.results[i].titleNoFormatting;
		        }
	
	
		        ImageAdapter itemsAdapter = 
		        	    new ImageAdapter(activity, R.layout.list_single, urls, names);
		        
		        titles = (ListView) activity.findViewById(R.id.titles);
				
				titles.setAdapter(itemsAdapter);

		       
			super.onPostExecute(result);
		}
		
		public static String convertStreamToString(java.io.InputStream is) {
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
	}
	

}
