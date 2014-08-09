 package adapters;

import com.example.bluetoothmodulev4.R;

import gamedata.Stats;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StatAdapt extends BaseAdapter{

    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    private Context cont;
    Stats tempValues=null;
    int i=0;
	
    public StatAdapt(Activity a, ArrayList d, Resources resLocal, Context cont)
    {
    	this.activity = a;
    	this.data = d;
    	this.res = resLocal;
    	this.cont = cont;
    	
    	inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(data.size()<=0)
		{
			return 1;
		}else
		{
			return data.size();
		}

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder; 
		
		if(convertView == null)
		{
			
			view = inflater.inflate(R.layout.stat_view, null);
			
			holder = new ViewHolder();
			holder.statFunction = (ProgressBar)view.findViewById(R.id.statRating);
			holder.statImage = (ImageView)view.findViewById(R.id.statImage);
			holder.statName = (TextView)view.findViewById(R.id.statName);
			holder.statNumber = (TextView)view.findViewById(R.id.statNumber);
			
			view.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)view.getTag();
		}
		
		if(data.size()<=0)
		{
			holder.statName.setText("no data");
		}
		else
		{
			tempValues = null;
			tempValues = (Stats) data.get(position);
			
			holder.statImage.setImageResource(res.getIdentifier(tempValues.getStatImage(),"drawable",this.cont.getPackageName()));
			holder.statName.setText(""+tempValues.getStatName());
			holder.statNumber.setText(""+tempValues.getStatWeight());
			
			//set progress bar
			holder.statFunction.setProgress(tempValues.getStatValue());
			
			Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_down);
	        view.startAnimation(animation);
		}
		
		return view;
	}
	
	

}
