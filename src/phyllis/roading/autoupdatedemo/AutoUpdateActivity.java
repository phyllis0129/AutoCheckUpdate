package phyllis.roading.autoupdatedemo;

import android.R.integer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AutoUpdateActivity extends Activity {
	
	private String olderVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auto_update);
		String versionURL="http://www.mumayi.com/android-120469.html";
//		try {
//			olderVersion=getPackageManager().getPackageInfo("phyllis.roading.autoupdatedemo", PackageManager.GET_CONFIGURATIONS).versionName;
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
		for(int i=0;i<50;i++)
			Log.v("i", i+"");
		Button btn=(Button)findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.v("button1", "button is clicked");
			}
		});
		olderVersion="3.00";
		
		Boolean occurTip=PreferenceManager.getDefaultSharedPreferences(this).getBoolean("neverOccurTip", false);
		GetVersionTask gvtask=new GetVersionTask(this);
		gvtask.execute(versionURL,olderVersion,occurTip);
		for(int i=0;i<50;i++)
			Log.v("i", i+"");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.auto_update, menu);
		return true;
	}

}
