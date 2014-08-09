package networkcomms;

import interfaces.ErrorHandler;
import interfaces.OnTaskCompleted;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class WebRequester extends AsyncTask<URI, Void, String> {

	private URI webRequest;
	private String host;
	private OnTaskCompleted listener;
	private ErrorHandler errorCallback;
	private int queryType; // type of query, 0 = a login request, 1 = a profile
							// get request, 2 = perform a POST

	private ProgressDialog dialog; // for loading dialog

	public WebRequester(URI webRequest, String host, OnTaskCompleted listener,
			ErrorHandler errorCallback, int queryType, Activity context) {
		// this.webRequest = webRequest;
		// this.host = host;
		this.listener = listener;
		this.errorCallback = errorCallback;
		this.queryType = queryType;

		dialog = new ProgressDialog(context);
	}

	@Override
	protected void onPreExecute() {

		String message = "";

		if (queryType == 1 || queryType == 0) {
			message = "Retreiving profile";

		}
		this.dialog.setMessage(message);
		this.dialog.show();

		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(URI... urls) {

		String result = "";

		if (this.queryType == 0 || this.queryType == 1) {
			// set params for timeout
			int timeoutConnection = 3000;
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					timeoutConnection);

			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpGet httpGet = new HttpGet(urls[0]);

			try {

				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					result = builder.toString(); // response data
				} else {
					Log.e("Getter", "Failed to download file");
				}

			} catch (ClientProtocolException a) {
				errorCallback.catchError();
			} catch (IOException e) {
				errorCallback.catchError();
			}
		} else if (this.queryType == 2) {
			int timeoutConnection = 3000;
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					timeoutConnection);

			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpPost httpPost = new HttpPost(urls[0]);

			try {

				HttpResponse response = client.execute(httpPost);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					result = builder.toString(); // response data
				} else {
					Log.e("Getter", "Failed to download file");
				}

			} catch (ClientProtocolException a) {
				errorCallback.catchError();
			} catch (IOException e) {
				errorCallback.catchError();
			}
		}

		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		this.dialog.dismiss();
		// callback
		if (queryType == 0) {
			listener.onProtocolCodeReceive(result);
			// looking for errorcode
		} else if (queryType == 1) {
			listener.onTaskCompleted(result);
		}else if(queryType == 2)
		{
			listener.onTaskCompleted(result);
		}

	}

}
