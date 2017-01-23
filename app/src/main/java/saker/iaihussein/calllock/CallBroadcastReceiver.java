/**
 * 
 */
package saker.iaihussein.calllock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;

/**
 * @author IAIHussein
 * 
 */
public class CallBroadcastReceiver extends BroadcastReceiver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	Context mContext;

	Boolean mcheckedBoolean;
	String mOriginalNumber;
	Editor mEditor;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		Log.e("APP", "ACTION:" + intent.getAction());
		if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())) {
			mOriginalNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			SharedPreferences mSharedPreferences = mContext
					.getSharedPreferences(MainActivity.KEY_SHAREDPREFERENCES,
							Activity.MODE_PRIVATE);
			mEditor = mSharedPreferences.edit();
			Log.e("APP",
					"KEY_APP_ON"
							+ mSharedPreferences.getBoolean(
									MainActivity.KEY_APP_ON, false)
							+ " KEY_LOCKED"
							+ mSharedPreferences.getBoolean(
									MainActivity.KEY_LOCKED, true));
			if (!mSharedPreferences.getBoolean(MainActivity.KEY_APP_ON, false)
					&& mSharedPreferences.getBoolean(MainActivity.KEY_LOCKED,
							true)) {
				setResultData(null);
				if (mOriginalNumber.startsWith(mSharedPreferences.getString(
						MainActivity.KEY_PREFIX, MainActivity.KEY_PREFIX))) {
					mEditor.putBoolean(MainActivity.KEY_APP_ON, true);
					mEditor.commit();
					Log.e("APP", "set KEY_APP_ON");
					Intent mCallIntent = new Intent(Intent.ACTION_CALL);
					mCallIntent.setData(Uri.parse("tel:"
							+ mOriginalNumber.substring(4)));
					Log.e("APP", "KEY_APP_ON" + mOriginalNumber.substring(3));
					mCallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(mCallIntent);
				}
			} else {
				mEditor.putBoolean(MainActivity.KEY_APP_ON, false);
				mEditor.commit();
			}
			Log.e("APP", "outgoing,ringing:" + mOriginalNumber);

		}
	}
}
