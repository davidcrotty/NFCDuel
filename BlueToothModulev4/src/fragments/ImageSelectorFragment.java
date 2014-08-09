package fragments;


import com.example.bluetoothmodulev4.R;
import fragmentinterfaces.FragmentSelect;
import fragmentinterfaces.TabletOrPhone;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImageSelectorFragment extends Fragment {

	private ViewGroup mContainer, mListLayout;
	private Boolean mInitialCreate;
	public static String data;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
			mInitialCreate = false;


		}
		// Otherwise, default state
		else {
			mInitialCreate = true;

		
		}
//
//		// Set that this fragment has a menu
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Points to the fragment for the gridview 
		View v = inflater.inflate(R.layout.fragment_image_selector, container, false);
		mContainer = container;
		
		// If this is the first creation of the fragment, add child fragments
				if (mInitialCreate) {
					mInitialCreate = false;

					// Prepare a transaction to add fragments to this fragment
					FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

					// Add the list fragment to this fragment's layout
					mListLayout = (ViewGroup) v.findViewById(R.id.fragment_image_selector_list_container);
					if (mListLayout != null) {
						

						// Add the fragment to the this fragment's container layout
						ImageListFragment imageListFragment = new ImageListFragment();
						fragmentTransaction.replace(mListLayout.getId(), imageListFragment, ImageListFragment.class.getName());
					
					}
					
					fragmentTransaction.commit();
				}
				
				
		// TODO Auto-generated method stub
		return v;
	}

	
	public void replaceFragment() {
		// TODO Auto-generated method stub
		
		FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
		
		//refresh child fragment
		getChildFragmentManager().beginTransaction().remove(getChildFragmentManager().findFragmentById(R.id.fragment_image_selector_list_container)).commit();
		ImageListFragment imageListFragment = new ImageListFragment();
		fragmentTransaction.replace(mListLayout.getId(), imageListFragment, ImageListFragment.class.getName());
		fragmentTransaction.commit();
		
		
	}

	

	

	
	
}
