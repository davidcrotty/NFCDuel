package fragments;

import com.example.bluetoothmodulev4.R;

import fragmentinterfaces.FragmentSelect;
import fragmentinterfaces.TabletOrPhone;
import adapters.ImageAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageListFragment extends Fragment {

	private boolean mInitialCreate;
	private TextView mListView;
	private FragmentSelect selector;
	private TextView timerView;

	private ImageAdapter imgad;
	static final String[] CHAMPIONS = new String[] { "Warrior", "Paladin",
			"Wizard" };
	static final Integer[] IMAGES = new Integer[] { R.drawable.warrioriconv2,
			R.drawable.paladiniconv2, R.drawable.wizardiconv2 };

	GridView gridView;
	long currentTime; // time for timer
	TabletOrPhone mHandler;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub

		// Check if parent fragment (if there is one) implements the image
		// selection interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof FragmentSelect) {
			selector = (FragmentSelect) parentFragment;
		}
		// Otherwise, check if parent activity implements the image
		// selection interface
		else if (activity != null && activity instanceof FragmentSelect) {
			selector = (FragmentSelect) activity;
		}

		try {
			mHandler = (TabletOrPhone) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Track if this is the initial creation of the fragment
		if (savedInstanceState != null) {
			mInitialCreate = false;
			currentTime = savedInstanceState.getLong("TimerValue");
		} else {
			mInitialCreate = true;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		// Inflate the fragment main view in the container provided
		View v = inflater.inflate(R.layout.fragment_image_list, container,
				false);

		imgad = new ImageAdapter(getActivity(), CHAMPIONS, IMAGES);

		gridView = (GridView) v.findViewById(R.id.gridview_Gallery);
		timerView = (TextView) v.findViewById(R.id.timerView);

		gridView.setAdapter(imgad);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub

				TextView temp;
				temp = (TextView) v.findViewById(R.id.gridItemLabel);

				if (position == 0) {
					// save instance state here, pass to next activity
					// with this one, save instanced state and if != null on
					// create set it here

					imgad.setSelected(position, R.drawable.warrioricontickedv2);
					imgad.setSelected(1, R.drawable.paladiniconv2);
					imgad.setSelected(2, R.drawable.wizardiconv2);
					gridView.setAdapter(imgad);
					imgad.notifyDataSetChanged();
					// getActivity().recreate();
					if (selector != null) {

						selector.selectedItem(position, temp.getText()
								.toString());

					}

					mHandler.gridSelected(temp.getText().toString());

				} else if (position == 1) {

					imgad.setSelected(position, R.drawable.paladinicontickedv2);
					imgad.setSelected(0, R.drawable.warrioriconv2);
					imgad.setSelected(2, R.drawable.wizardiconv2);
					gridView.setAdapter(imgad);
					imgad.notifyDataSetChanged();

					if (selector != null) {

						selector.selectedItem(position, temp.getText()
								.toString());
					}

					mHandler.gridSelected(temp.getText().toString());

				} else if (position == 2) {
					imgad.setSelected(position, R.drawable.wizardicontickedv2);
					imgad.setSelected(0, R.drawable.warrioriconv2);
					imgad.setSelected(1, R.drawable.paladiniconv2);
					gridView.setAdapter(imgad);
					imgad.notifyDataSetChanged();

					if (selector != null) {

						selector.selectedItem(position, temp.getText()
								.toString());
					}

					mHandler.gridSelected(temp.getText().toString());
				} else {

				}

			}
		});

		if (mInitialCreate) {
			mInitialCreate = false;
		}

		return v;
	}
	



}
