package fragments;

import java.net.URI;

import org.json.JSONObject;

import interfaces.ErrorHandler;
import interfaces.OnTaskCompleted;
import networkcomms.WebRequester;
import singleton.LoginData;
import singleton.RegistrationData;
import singleton.SessionData;

import com.androidactivities.LoginActivity;
import com.androidactivities.MainActivity;
import com.androidactivities.RegisterActivity;
import com.example.bluetoothmodulev4.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import fragmentinterfaces.ViewEnabler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SecurityFragment extends Fragment implements OnTaskCompleted, ErrorHandler{

	private boolean mInitialCreate;
	private ViewGroup mContainer;
	private static LinearLayout securityView;
	private Button proceedButton;
	private EditText passwordField, passwordConfirm;
	private ErrorHandler errorHandler;
	private OnTaskCompleted completedRequest;

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

			try {
				errorHandler = (ErrorHandler) this;
				completedRequest = (OnTaskCompleted) this;
			} catch (ClassCastException e) {

			}
		}

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.registersecurity, container, false);
		mContainer = container;

		// If this is the first creation of the fragment, add child fragments
		if (mInitialCreate) {
			mInitialCreate = false;

		}

		securityView = (LinearLayout) v.findViewById(R.id.securityView);
		proceedButton = (Button) v.findViewById(R.id.confirmButton);
		passwordField = (EditText) v.findViewById(R.id.passwordField);
		passwordConfirm = (EditText) v.findViewById(R.id.confirmPasswordField);

		if (!RegistrationData.isWasFragment()) {
			securityView.setAlpha(0.2f);
			for (int i = 0; i < securityView.getChildCount(); i++) {
				View a = securityView.getChildAt(i);
				a.setEnabled(false);
			}
		}
		proceedButton.setAlpha(0.2f);
		proceedButton.setEnabled(false);

		passwordField.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub

				if (passwordField.getText().toString().length() < 5) {
					Crouton.showText(getActivity(),
							"Password must be greater than 4 characters",
							Style.ALERT, (ViewGroup) getView());
				}
			}
		});

		passwordConfirm.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (passwordConfirm.getText().toString()
						.compareTo(passwordField.getText().toString()) == 0
						&& passwordConfirm.getText().toString().length() > 4) {
					Crouton.cancelAllCroutons();
					Crouton.showText(getActivity(), "Passwords match!",
							Style.CONFIRM, (ViewGroup) getView());
					proceedButton.setAlpha(1.0f);
					proceedButton.setEnabled(true);
				} else {
					Crouton.showText(getActivity(), "Passwords do not match",
							Style.ALERT, (ViewGroup) getView());
				}
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

		proceedButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				RegistrationData
						.setPassword(passwordField.getText().toString());

				// remember to blank everything after sending
				WebRequester postRequest = new WebRequester(null, "",
						completedRequest, errorHandler, 2, getActivity());

				URI myUri = URI.create("http://"
						+ SessionData.getWebServiceIp()
						+ ":20000/Home/register?username="
						+ RegistrationData.getUsername() + "&password="
						+ RegistrationData.getPassword() + "&emailaddress="
						+ RegistrationData.getEmailAddress() + "&firstname="
						+ RegistrationData.getFirstName() + "&lastname="
						+ RegistrationData.getLastName());

				postRequest.execute(myUri);

			}
		});

		return v;
	}

	private void animator() {
		Animation animation = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.slide_up);
		securityView.startAnimation(animation);

		Animation delayAnim = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.slide_up_delay);

		proceedButton.startAnimation(delayAnim);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		animator();
	}

	public static void enableLayout() {
		// TODO Auto-generated method stub
		securityView.setAlpha(1.0f);
		for (int i = 0; i < securityView.getChildCount(); i++) {
			View a = securityView.getChildAt(i);
			a.setEnabled(true);
		}
	}

	@Override
	public void catchError() {
		// TODO Auto-generated method stub
		Crouton.showText(getActivity(), "Unable to connect to server", Style.ALERT);
	}

	@Override
	public void onTaskCompleted(String result) {
		// TODO Auto-generated method stub
		
		try{
			JSONObject temp = new JSONObject(result);
			
			if(temp.getString("_Error").compareTo("INVALID")==0)
			{
				Crouton.showText(getActivity(), "Username is already taken, please try another", Style.ALERT);
				//not created
			} else if (temp.getString("_Error").compareTo("VALID")==0)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		        builder.setMessage("Welcome: "+ RegistrationData.getFirstName() + " " + RegistrationData.getLastName() + "\n you are now logged in")
		               .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       
		                	   Intent main = new Intent(getActivity().getApplicationContext(),
		       						MainActivity.class);
		       				startActivity(main);
		                	   
		                   }
		               });
		               
		        // Create the AlertDialog object and return it
		        builder.create().show();
		        
		        LoginData.setUsername(RegistrationData.getUsername());
			    LoginData.setFirstname(RegistrationData.getFirstName());
			    LoginData.setLastname(RegistrationData.getLastName());
			    LoginData.setEmailAddress(RegistrationData.getEmailAddress());
			    LoginData.setWins(0);
			    LoginData.setLosses(0);
			    LoginData.setRank("newbie");
			    LoginData.setExp(0);
		        //Login data will persist!!!
			}
			
			} catch (Exception e)
			{
				
			}
		
	}

	@Override
	public void onProtocolCodeReceive(String result) {
		// TODO Auto-generated method stub
		
		
		
	}

	@Override
	public void onSent(boolean ok) {
		// TODO Auto-generated method stub
		
	}

}
