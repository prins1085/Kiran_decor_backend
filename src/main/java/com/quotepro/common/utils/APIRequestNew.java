package com.quotepro.common.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * TIGO <br/>
 * This Class contains All Methods for connect to Tigo Server
 */
public class APIRequestNew {

	// private static final org.apache.log4j.Logger loger =
	// org.apache.log4j.Logger.getLogger(APIRequest.class);
	private String USER_AGENT = "Mozilla/5.0";

	// private static final org.apache.log4j.Logger loger =
	// org.apache.log4j.Logger.getLogger(APIRequestNew.class);
	// HTTP POST request
	public String sendRequest(String url, LinkedHashMap values) {
		if (url.startsWith("https://")) {
			return sendHttpsRequest(url, values);
		} else {
			return sendHttpRequest(url, values);
		}
	}

	public String sendRequest(String url) {
		if (url.startsWith("https://")) {
			return sendHttpsRequest(url);
		} else {
			return sendHttpRequest(url);
		}
	}

	// HTTP POST request
	public String sendHttpsRequest(String url, LinkedHashMap values) {
		String response = "";
		try {

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}
			} };
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String string, SSLSession ssls) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(10000);
			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = "";

			if (values != null) {
				Set keySet = values.keySet();
				Object[] keys = keySet.toArray();
				for (int i = 0; i < values.size(); i++) {
					urlParameters += keys[i].toString() + "=" + values.get(keys[i]) + "&";
				}
			}
			urlParameters = urlParameters.substring(0, urlParameters.length() - 1);
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'POST' request to URL : " + url);
//            System.out.println("Post parameters : " + urlParameters);
//            System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer responseBuffer = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				responseBuffer.append(inputLine);
			}
			in.close();
			response = responseBuffer.toString();

		} catch (Throwable t) {
			// loger.error("Tigo API Error : " + url + " : " + t);
			response = "Error1 : " + t;
		}

		// print result
		return response;

	}

	// HTTP POST request
	public String sendHttpsRequest(String url) {
		String response = "";
		try {

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}
			} };
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String string, SSLSession ssls) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			URL obj = new URL(url);

			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(10000);
			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'POST' request to URL : " + url);
//            System.out.println("Post parameters : " + urlParameters);
//            System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer responseBuffer = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				responseBuffer.append(inputLine);
			}
			in.close();
			response = responseBuffer.toString();

		} catch (Throwable t) {
			// loger.error("API Error : " + url + " : " + t);
			response = "Error1 : " + t;
		}

		// print result
		return response;

	}

	public String sendHttpsRequestGET(String url) {
		String response = "";
		try {

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}
			} };
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String string, SSLSession ssls) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			URL obj = new URL(url);

			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(10000);
			// add reuqest header
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'POST' request to URL : " + url);
//            System.out.println("Post parameters : " + urlParameters);
//            System.out.println("Response Code : " + responseCode);
			InputStreamReader ins;
			if (responseCode >= 200 && responseCode < 400) {
				ins = new InputStreamReader(con.getInputStream());
			} else {
				ins = new InputStreamReader(con.getErrorStream());
			}
			BufferedReader in = new BufferedReader(ins);
			String inputLine;
			StringBuffer responseBuffer = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				responseBuffer.append(inputLine);
			}
			in.close();
			response = responseBuffer.toString();
//            if (prop.getProperty("APILOG").equals("1")) {
//           //     loger.info("API Response : " + response);
//            }
		} catch (Throwable t) {
			System.out.println("API Error : " + url + " : " + t);
			response = "Error1 : " + t;
		}

		// print result
		return response;

	}

	// HTTP POST request
	public String sendHttpRequest(String url, LinkedHashMap values) {
		String response = "";
		try {

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(10000);
			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = "";

			if (values != null) {
				Set keySet = values.keySet();
				Object[] keys = keySet.toArray();
				for (int i = 0; i < values.size(); i++) {
					urlParameters += keys[i].toString() + "=" + values.get(keys[i]) + "&";
				}
			}
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'POST' request to URL : " + url);
//            System.out.println("Post parameters : " + urlParameters);
//            System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String inputLine;
			StringBuffer responseBuffer = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				responseBuffer.append(inputLine);
			}
			in.close();
			response = responseBuffer.toString();

		} catch (Throwable t) {
			// loger.error("API Error : " + url + " : " + t);
			response = "Error1 : " + t;
		}
		// print result
		return response;

	}

	public String sendHttpRequestJSon(String url, LinkedHashMap values) {
		String response = "";
		try {

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(10000);
			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Content-Type", "application/json");

			String urlParameters = "{";

			if (values != null) {
				Set keySet = values.keySet();
				Object[] keys = keySet.toArray();
				for (int i = 0; i < values.size(); i++) {
					urlParameters += "\"" + keys[i].toString() + "\":\"" + values.get(keys[i]) + "\",";
				}
			}
			urlParameters = urlParameters.substring(0, urlParameters.length() - 1) + "}";
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'POST' request to URL : " + url);
//            System.out.println("Post parameters : " + urlParameters);
//            System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String inputLine;
			StringBuffer responseBuffer = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				responseBuffer.append(inputLine);
			}
			in.close();
			response = responseBuffer.toString();

		} catch (Throwable t) {
			// loger.error("API Error : " + url + " : " + t);
			response = "Error1 : " + t;
		}
		// print result
		return response;

	}

	// HTTP POST request
	public String sendHttpRequest(String url) {
		String response = "";
		try {

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(10000);
			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String inputLine;
			StringBuffer responseBuffer = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				responseBuffer.append(inputLine);
			}
			in.close();
			response = responseBuffer.toString();

		} catch (Throwable t) {
			// loger.error("API Error : " + url + " : " + t);
			response = "Error1 : " + t;
		}
		// print result
		return response;

	}

	public String sendHttpRequestGET(String url) {
		String response = "";
		try {

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(10000);
			// add reuqest header
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String inputLine;
			StringBuffer responseBuffer = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				responseBuffer.append(inputLine);
			}
			in.close();
			response = responseBuffer.toString();

		} catch (Throwable t) {
			// loger.error("API Error : " + url + " : " + t);
			response = "Error1 : " + t;
		}
		// print result
		return response;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String sendNotification(String message, String tocken, String dtype) {
		String response = "";
		try {
			LinkedHashMap param = new LinkedHashMap();
			String url = "http://dev.impliessolution.com:8080/emsws/login/SendNotification";
			param.put("msg", message);
			param.put("token", tocken);
			param.put("dtype", dtype);
			response = sendRequest(url, param);
		} catch (Throwable t) {
			response = "Error1 : " + t;
		}
		return response;

	}

}
