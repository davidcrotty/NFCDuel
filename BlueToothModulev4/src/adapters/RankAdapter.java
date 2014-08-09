package adapters;

import java.util.ArrayList;

import com.example.bluetoothmodulev4.R;

import datasets.UserProfile;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RankAdapter extends BaseAdapter{

	private ArrayList<UserProfile> items = new ArrayList<UserProfile>();
	private LayoutInflater inflater;
	private boolean animationPlayed;
	
	public RankAdapter(Context context, ArrayList<UserProfile> items, boolean animationPlayed)
	{ 
		inflater = LayoutInflater.from(context);
		this.items = items;
		this.animationPlayed = animationPlayed;
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}
	
	@Override
	public UserProfile getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return items.get(position).getDrawable();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View v = convertView;
		ImageView picture;
		TextView name;
		
		if(v == null)
		{
			v = inflater.inflate(R.layout.profilegridadapter, parent, false);
			v.setTag(R.id.picture, v.findFocus());
			v.setTag(R.id.text, v.findFocus());
		}
		
		picture = (ImageView)v.findViewById(R.id.picture);
		name = (TextView)v.findViewById(R.id.text);
		
		UserProfile p = (UserProfile)getItem(position);
		
		picture.setImageResource(p.getDrawable());
		name.setText(p.getUsername());
		
//		Item item = (Item)getItem(position);
//		
//		picture.setImageResource(item.drawableId);
//		name.setText(item.name);
		
		
		
		if(this.animationPlayed)
		{
		TranslateAnimation transAnim = new TranslateAnimation(TranslateAnimation.ABSOLUTE,3000,TranslateAnimation.ABSOLUTE, 0,TranslateAnimation.ABSOLUTE, 3000,TranslateAnimation.ABSOLUTE,0);
		transAnim.setDuration(position*600);
		transAnim.setInterpolator(new AccelerateInterpolator());
		v.startAnimation(transAnim);
		}
		//setAnimation with offset of time based on position
		
		return v;
	}
}
