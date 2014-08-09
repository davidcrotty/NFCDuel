package fragments;

import singleton.RegistrationData;

import com.androidactivities.RegisterActivity;
import com.example.bluetoothmodulev4.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fragmentinterfaces.TabletOrPhone;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CredentialsFragment extends Fragment {

	private boolean mInitialCreate;
	private ViewGroup mContainer;
	private LinearLayout credView, detailsView, emailView;
	private Button proceedButton;
	// Edittexts for validation listeners
	private EditText firstNameText, lastNameText, emailAddressText, inGameText;
	private boolean firstCond;
	private TabletOrPhone callback;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		try{
			callback = (TabletOrPhone) activity;
		} catch (ClassCastException e)
		{
			e.printStackTrace();
		}
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

		setHasOptionsMenu(true);
	}

	private void animator() {
		Animation animation = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.slide_up);
		credView.startAnimation(animation);
		Animation delayAnim = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.slide_up_delay);
		emailView.startAnimation(delayAnim);

		Animation delayaAnim = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.slide_up_delay);
		detailsView.startAnimation(delayaAnim);

		proceedButton.startAnimation(delayaAnim);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		animator();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		firstCond = false;
		
		View v = inflater.inflate(R.layout.registercredentials, container,
				false);
		mContainer = container;

		// If this is the first creation of the fragment, add child fragments
		if (mInitialCreate) {
			mInitialCreate = false;

		}

		credView = (LinearLayout) v.findViewById(R.id.credsView);
		detailsView = (LinearLayout) v.findViewById(R.id.detailsView);
		emailView = (LinearLayout) v.findViewById(R.id.emailView);
		proceedButton = (Button) v.findViewById(R.id.confirmButton);

		firstNameText = (EditText) v.findViewById(R.id.firstNameField);
		lastNameText = (EditText) v.findViewById(R.id.lastNameField);
		emailAddressText = (EditText) v.findViewById(R.id.emailAddressField);
		inGameText = (EditText) v.findViewById(R.id.usernameField);

		
		
		detailsView.setAlpha(0.2f);
		for (int i = 0; i < detailsView.getChildCount(); i++) {
			View a = detailsView.getChildAt(i);
			a.setEnabled(false);
		}
		emailView.setAlpha(0.2f);
		for (int i = 0; i < emailView.getChildCount(); i++) {
			View a = emailView.getChildAt(i);
			a.setEnabled(false);
		}
		proceedButton.setAlpha(0.2f);
		proceedButton.setEnabled(false);

		// listeners needed to be called within method else Crouton notifcations
		// wont show
		// firstNameText.addTextChangedListener(this);
		firstNameText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (firstNameText.getText().toString().length() < 2) {
					Crouton.showText(getActivity(), "first name must at least 2 characters long",
							Style.ALERT, (ViewGroup) getView());
				}

			}
		});

		lastNameText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (lastNameText.hasFocus()
						&& lastNameText.getText().toString().length() < 2) {
					Crouton.showText(getActivity(), "last name must be at least 2 characters long",
							Style.ALERT, (ViewGroup) getView());

				}
				checkLength();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		emailAddressText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!emailAddressText.getText().toString().contains(".")
						&& !emailAddressText.getText().toString().contains("@")) {
					Crouton.cancelAllCroutons();
					Crouton.showText(getActivity(),
							"Email Address must contain an @ and . ",
							Style.ALERT, (ViewGroup) getView());
				}

				checkLength();
			}
		});

		inGameText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(inGameText.getText().toString().length() < 5)
				{
					Crouton.cancelAllCroutons();
					Crouton.showText(getActivity(),
							"Username must contain greater than four characters",
							Style.ALERT, (ViewGroup) getView());
				}
				checkLength();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		// lastNameText.addTextChangedListener(this);

		proceedButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RegistrationData.setEmailAddress(emailAddressText.getText().toString());
				RegistrationData.setFirstName(firstNameText.getText().toString());
				RegistrationData.setLastName(lastNameText.getText().toString());
				RegistrationData.setUsername(inGameText.getText().toString());
			 	callback.gridSelected("");
			}
		});
		// TODO Auto-generated method stub
		return v;
	}

	private void checkLength() {

		
		if (lastNameText.getText().toString().length() >= 2
				&& firstNameText.getText().toString().length() >= 2) {
			emailView.setAlpha(1.0f);
			for (int i = 0; i < emailView.getChildCount(); i++) {
				View a = emailView.getChildAt(i);
				a.setEnabled(true);
			}
			firstCond = true;
		}

		if (firstCond && inGameText.getText().toString().length() >= 2
				&& emailAddressText.getText().toString().contains(".")
				&& emailAddressText.getText().toString().contains("@")) {
			detailsView.setAlpha(1.0f);
			for (int i = 0; i < detailsView.getChildCount(); i++) {
				View a = detailsView.getChildAt(i);
				a.setEnabled(true);
				proceedButton.setAlpha(1.0f);
				proceedButton.setEnabled(true);
			}
		}

	}

}
