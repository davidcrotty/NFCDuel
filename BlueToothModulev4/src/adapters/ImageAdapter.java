package adapters;


import com.example.bluetoothmodulev4.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter{

	private Context context;
	private String[] championValues;
	private Integer[] imageids;
	
	public ImageAdapter(Context context,String[] championValues,Integer[] imageids)
	{
		this.context = context;
		this.championValues = championValues;
		this.imageids = imageids;
	}
	
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return championValues.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setSelected(int pos, Integer resource)
	{
			this.imageids[pos] = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View gridView;
	 
			if (convertView == null) {
	 
				gridView = new View(context);
	 
				// get layout from imageset.xml
				gridView = inflater.inflate(R.layout.imageset, null);
	 
				// set value into textview
				TextView textView = (TextView) gridView
						.findViewById(R.id.gridItemLabel);
				textView.setText(championValues[position]);
	 
				// set image based on selected text
				ImageView imageView = (ImageView) gridView
						.findViewById(R.id.gridItemImage);
	 
				String mobile = championValues[position];
	 
				if (mobile.equals("Warrior")) {
					
					imageView.setImageResource(imageids[position]);
					//imageView.setImageResource(R.drawable.warrior_icon);
				} else if (mobile.equals("Paladin")) {
					
					imageView.setImageResource(imageids[position]);
				} else if (mobile.equals("Wizard")) {
					
					imageView.setImageResource(imageids[position]);
				} else {
					imageView.setImageResource(R.drawable.ic_launcher);
				}
	 
			} else {
				gridView = (View) convertView;
			}
	 
			return gridView;
	}
	

}
