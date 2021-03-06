package info.guardianproject.pixelknot.utils;

import info.guardianproject.pixelknot.Constants;
import info.guardianproject.pixelknot.PixelKnotActivity;
import info.guardianproject.pixelknot.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class PixelKnotNotification implements PixelKnotNotificationListener {
	Activity a;
	Intent resume_intent;
	
	NotificationManager notification_manager;
	NotificationCompat.Builder notification;
	
	int num_steps = 0;
	int step = -1;
	
	public PixelKnotNotification(Activity a, String mode_string) {
		this.a = a;
		
		// resume intent is default (go back to activity);
		resume_intent = new Intent(this.a, PixelKnotActivity.class)
			.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)
			.setAction(Intent.ACTION_MAIN)
			.addCategory(Intent.CATEGORY_LAUNCHER);
		
		notification_manager = (NotificationManager) a.getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new NotificationCompat.Builder(a)
			.setContentTitle(a.getString(R.string.app_name))
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentText(mode_string);
	}
	
	public void init(int num_steps) {
		this.num_steps = num_steps;
		// set progress
		
		// set resume intent bundle with default bundle
		PendingIntent content_intent = PendingIntent.getActivity(a, Constants.Source.NOTIFICATION, 
				resume_intent, 0);
		
		notification.setContentIntent(content_intent);
		post();
	}

	@Override
	public void update(int additional_steps) {
		num_steps += additional_steps;
		// set progress
		post();
	}
	
	@Override
	public void post(String with_message) {
		notification.setContentText(with_message);
		post();
	}

	@Override
	public void post() {
		notification_manager.notify(Constants.Source.NOTIFICATION, notification.getNotification());
	}
	
	public void fail(String with_message, boolean recoverable) {
		fail(with_message);
		if(!recoverable) {
			notification.setDefaults(0);
			post();
		}
	}

	@Override
	public void fail(String with_message) {
		finish(with_message);
	}

	@Override
	public void finish() {
		notification.setAutoCancel(true);
		notification.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
		post();
	}

	@Override
	public void finish(String result_text) {
		notification.setContentText(result_text);
		finish();
	}
}
