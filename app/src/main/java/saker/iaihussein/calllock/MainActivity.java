package saker.iaihussein.calllock;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author IAIhussein
 * 
 */
public class MainActivity extends ActionBarActivity {
	public final static String KEY_SHAREDPREFERENCES = "SharedPreferences",
			KEY_USERNAME = "IAIUserName", KEY_PASSWORD = "IAIPassword",
			KEY_LOCKED = "IAILocked", KEY_PREFIX = "IAIPrefix",
			KEY_APP_ON = "APP_ON", KEY_CHECK = "Check";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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
		View rootView;
		TextView mIntroductionTextView, mErrorTextView;
		EditText mUsernameEditText, mPasswordEditText;
		Button mEnterButton;

		SharedPreferences mSharedPreferences;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_placeholder,
					container, false);
			mIntroductionTextView = (TextView) rootView
					.findViewById(R.id.txtintroduction);
			mErrorTextView = (TextView) rootView.findViewById(R.id.txterror);
			mEnterButton = (Button) rootView.findViewById(R.id.btnenter);
			mSharedPreferences = getActivity().getSharedPreferences(
					KEY_SHAREDPREFERENCES, MODE_PRIVATE);
			if (mSharedPreferences.getString(KEY_USERNAME, KEY_USERNAME)
					.equalsIgnoreCase(KEY_USERNAME)
					&& mSharedPreferences.getString(KEY_PASSWORD, KEY_PASSWORD)
							.equals(KEY_PASSWORD)) {
				mIntroductionTextView.setVisibility(View.VISIBLE);
			}
			mEnterButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mUsernameEditText = (EditText) rootView
							.findViewById(R.id.txtusername);
					mPasswordEditText = (EditText) rootView
							.findViewById(R.id.txtpassword);
					Editor mEditor = mSharedPreferences.edit();
					if (mIntroductionTextView.getVisibility() == View.VISIBLE) {

						mEditor.putString(KEY_USERNAME, mUsernameEditText
								.getText().toString());
						mEditor.putString(KEY_PASSWORD, mPasswordEditText
								.getText().toString());
						mEditor.putBoolean(KEY_APP_ON, true);
						mEditor.putString(MainActivity.KEY_PREFIX, "0000");
						mEditor.commit();
						getFragmentManager().beginTransaction()
								.replace(R.id.container, new MainFragment())
								.commit();
					} else if (mSharedPreferences.getString(KEY_USERNAME,
							KEY_USERNAME).equalsIgnoreCase(
							mUsernameEditText.getText().toString())
							&& mSharedPreferences.getString(KEY_PASSWORD,
									KEY_PASSWORD).equals(
									mPasswordEditText.getText().toString())) {
						mEditor.putBoolean(KEY_APP_ON, true);
						mEditor.commit();
						getFragmentManager().beginTransaction()
								.replace(R.id.container, new MainFragment())
								.commit();
					} else {
						mErrorTextView.setVisibility(View.VISIBLE);
					}
				}
			});
			return rootView;
		}
	}
}
