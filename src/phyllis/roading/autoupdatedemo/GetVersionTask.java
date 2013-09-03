package phyllis.roading.autoupdatedemo;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

public class GetVersionTask extends AsyncTask<Object, Integer, String> {

	private Boolean occurTip;
	private String olderVersion;
	private Context mContext;
	private DownloadTask downloadTask;
	// 返回的安装包url
	private String apkUrl = "http://down.mumayi.com/120469";

	public GetVersionTask(Context context) {
		this.mContext = context;
	}

	@Override
	protected String doInBackground(Object... params) {
		olderVersion = (String) params[1];
		occurTip = (Boolean) params[2];
		String result = "";
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet((String) params[0]);
		try {
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = filterHtml(EntityUtils.toString(response.getEntity()));
				Log.v("version", result);

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.v("error1", e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			Log.v("error2", e.toString());
		}
		return result;

	}

	@Override
	protected void onPostExecute(String result) {
		if (!olderVersion.equals(result) && !occurTip) {
			Log.v("olderversion", olderVersion);

			Log.v("equals", "检测到新版本");
			View v = LayoutInflater.from(mContext).inflate(
					R.layout.dialogcontent, null);
			final CheckBox neverOccur = (CheckBox) v.findViewById(R.id.neverTip);
			AlertDialog.Builder builder = new Builder(mContext);
			builder.setTitle("软件版本更新");
			builder.setMessage("有最新的软件包哦，亲快下载吧~");
			builder.setView(v);
			// builder.setInverseBackgroundForced(true);
			builder.setPositiveButton("立即下载", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					downloadTask = new DownloadTask(mContext);
					downloadTask.showDownloadDialog();
				}
			});
			builder.setNegativeButton("稍后再说", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(neverOccur.isChecked())
						setNeverOccur();
					dialog.dismiss();
				}

			});
			builder.show();
		}
		super.onPostExecute(result);
	}

	private String filterHtml(String source) {
		Pattern p = Pattern.compile("(\\d\\.\\d*)_");
		Matcher match = p.matcher(source);
		StringBuffer version = new StringBuffer();
		while (match.find()) {
			String domain = "";
			domain = match.group(1);
			version.append(domain);
		}
		Log.v("filterHtml", "succeed");

		return version.toString();
	}
	
	private void setNeverOccur() {
		PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean("neverOccurTip", true).commit();
	}

}
