package com.quotepro.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetIpmac {

	public String getIpaddress() {
		String Ipaddress = "";
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			Ipaddress = ip.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return Ipaddress;
	}

	public static String getClientMACAddress(String clientIp) {
//		String str = "";
		String macAddress = "";
		/*
		 * try { Process p = Runtime.getRuntime().exec("nbtstat -A " + clientIp);
		 * InputStreamReader ir = new InputStreamReader(p.getInputStream());
		 * LineNumberReader input = new LineNumberReader(ir); for (int i = 1; i < 100;
		 * i++) { str = input.readLine(); if (str != null) { if
		 * (str.indexOf("MAC Address") > 1) { macAddress =
		 * str.substring(str.indexOf("MAC Address") + 14, str.length()); break; } } } }
		 * catch (IOException e) { e.printStackTrace(System.out); }
		 */
		return macAddress;
	}

	public String getRemotemacaddress() {
//		InetAddress ip;
		StringBuilder sb = new StringBuilder("");
		/*
		 * try { ip = InetAddress.getLocalHost(); NetworkInterface network =
		 * NetworkInterface.getByInetAddress(ip); byte[] mac =
		 * network.getHardwareAddress(); if (null != mac) { for (int i = 0; i <
		 * mac.length; i++) { sb.append(String.format("%02X%s", mac[i], (i < mac.length
		 * - 1) ? "-" : "")); } } } catch (UnknownHostException e) {
		 * e.printStackTrace(); } catch (SocketException e) { e.printStackTrace(); }
		 * 
		 * if (sb.toString().trim().length() == 0) { sb.append("..."); }
		 */

		return sb.toString();
	}
}


