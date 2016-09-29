package fanvu.easygoer.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.util.Base64;
import android.util.Log;

import fanvu.easygoer.config.Config;

/**
 * @author hungnd40
 * 
 */
public class RestClient {

	private ArrayList<NameValuePair> params;
	private ArrayList<NameValuePair> headers;

	private String url;

	private int responseCode;
	private String message;

	private String response;

	public String getResponse() {
		return response;
	}

	public String getErrorMessage() {
		return message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public RestClient(String url) {
		this.url = url;
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}

	public void AddParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

	public void AddHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

	public void Execute(RequestMethod method)
			throws UnsupportedEncodingException {
		switch (method) {
		case GET: {
			// add parameters
			String combinedParams = "";
			if (!params.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : params) {
					String paramString = p.getName() + "="
							+ URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
			}

			HttpGet request = new HttpGet(url + combinedParams);

			// add headers
			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}

			executeRequest(request, url);
			break;
		}
		case POST: {
			HttpPost request = new HttpPost(url);

			// add headers
			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}

			if (!params.isEmpty()) {
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type",
					"application/x-www-form-urlencoded; charset=UTF8");
			executeRequest(request, url);
			break;
		}
		}

	}

	private void executeRequest(HttpUriRequest request, String url) {
		// DefaultHttpClient httpclient = new DefaultHttpClient();

		DefaultHttpClient client = new DefaultHttpClient();

		HttpResponse httpResponse;

		try {
			client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(
					0, false));
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params,
					Config.HTTP_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, Config.HTTP_TIME_OUT);
			httpResponse = client.execute(request);

			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();

			System.out.println("////////////////////responseCode"
					+ responseCode);
			System.out.println("////////////////////message" + message);
			if (responseCode == 200) {
				HttpEntity entity = httpResponse.getEntity();

				if (entity != null) {

					InputStream instream = entity.getContent();
					response = convertStreamToString(instream);
					System.out.println("////////////////////response"
							+ response);
					// Closing the input stream will trigger
					// connection release
					instream.close();
				}
			} else {
				response = "-1";
			}
		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
			Log.e("log_tag", "Error in http connection " + e.toString());
			response = "-1";
		} catch (IOException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
			Log.e("log_tag", "Error in http connection " + e.toString());
			response = "-1";
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
			response = "-1";
		}
	}

	public void executeHTTPS(String url) {
		// DefaultHttpClient httpclient = new DefaultHttpClient();
		InputStream instream;
		try {
			instream = getInputStream(url);
			response = convertStreamToString(instream);
			System.out.println("////////////////////response" + response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private InputStream getInputStream(String urlStr) throws IOException {

		URL url = new URL(urlStr);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		try {
			// Create the SSL connection
			SSLContext sc;
			sc = SSLContext.getInstance("TLS");
			sc.init(null, null, new java.security.SecureRandom());
			conn.setSSLSocketFactory(sc.getSocketFactory());

			// Use this if you need SSL authentication
			String userpass = Config.URL_USERNAME + ":" + Config.URL_PASSWORD;
			String basicAuth = "Basic "
					+ Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
			conn.setRequestProperty("Authorization", basicAuth);

			// set Timeout and method
			conn.setReadTimeout(7000);
			conn.setConnectTimeout(7000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);

			// Add any data you wish to post here

			conn.connect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return conn.getInputStream();
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
