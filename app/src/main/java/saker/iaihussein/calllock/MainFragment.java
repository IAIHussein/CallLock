/**
 * 
 */
package saker.iaihussein.calllock;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

/**
 * @author IAIhussein
 * 
 */
public class MainFragment extends Fragment {
	View rootView;
	Switch mLockSwitch;
	EditText mPrefixEditText, mNumberEditText;
	Button mCallButton, mChangeButton, mContactsButton;
	Editor mEditor;
	SharedPreferences mSharedPreferences;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_main, container, false);

		mLockSwitch = (Switch) rootView.findViewById(R.id.switch_lock);
		mCallButton = (Button) rootView.findViewById(R.id.btn_call);
		mChangeButton = (Button) rootView.findViewById(R.id.btn_change_prefix);
		mContactsButton = (Button) rootView.findViewById(R.id.btn_contacts);
		mNumberEditText = (EditText) rootView.findViewById(R.id.etxt_number);
		mPrefixEditText = (EditText) rootView.findViewById(R.id.etxt_prefix);

		mSharedPreferences = getActivity().getSharedPreferences(
				MainActivity.KEY_SHAREDPREFERENCES, Activity.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();

		mPrefixEditText.setText(mSharedPreferences.getString(
				MainActivity.KEY_PREFIX, "0000"));

		mLockSwitch.setChecked(mSharedPreferences.getBoolean(
				MainActivity.KEY_LOCKED, true));
		mLockSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mEditor.putBoolean(MainActivity.KEY_LOCKED, isChecked);
				mEditor.commit();
			}
		});

		mContactsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// user BoD suggests using
				// Intent.ACTION_PICK
				// instead of
				// .ACTION_GET_CONTENT to avoid the
				// chooser
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				// BoD con't: CONTENT_TYPE instead
				// of
				// CONTENT_ITEM_TYPE
				intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
				startActivityForResult(intent, 1);

			}
		});

		mChangeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEditor.putString(MainActivity.KEY_PREFIX, mPrefixEditText
						.getText().toString());
				mEditor.commit();
				Toast.makeText(getActivity(), R.string.changed,
						Toast.LENGTH_SHORT).show();
			}
		});
		mCallButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEditor.putBoolean(MainActivity.KEY_APP_ON, true);
				mEditor.commit();
				Intent mCallIntent = new Intent(Intent.ACTION_CALL);
				mCallIntent.setData(Uri.parse("tel:"
						+ mNumberEditText.getText().toString()));
				mCallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mCallIntent);

			}
		});

		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			Uri uri = data.getData();

			if (uri != null) {
				Cursor c = null;
				try {
					c = getActivity()
							.getContentResolver()
							.query(uri,
									new String[] {
											ContactsContract.CommonDataKinds.Phone.NUMBER,
											ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },
									null, null, null);

					if (c != null && c.moveToFirst()) {
						String content = c.getString(0);
						// String note = c.getString(1);
						mNumberEditText.setText(content);
					}
				} finally {
					if (c != null) {
						c.close();
					}
				}
			}
		}
	}
}
