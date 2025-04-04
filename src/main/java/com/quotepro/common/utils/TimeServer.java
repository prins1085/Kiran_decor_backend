package com.quotepro.common.utils;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManagerFactory;

public class TimeServer extends Thread {
	public static final Logger loger = LoggerFactory.getLogger(TimeServer.class);
	private ServerSocket serverSocket;
	private int port = 8888;
	private boolean running = false;
	
	private EntityManagerFactory emf;
	
	public TimeServer(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public void startServer() throws Throwable {
		serverSocket = new ServerSocket(port);
		serverSocket.setReuseAddress(true);
		this.start();
	}

	public void stopServer() {
		running = false;
		try {
			if (serverSocket != null || !serverSocket.isClosed()) {
				serverSocket.close();
			}
		} catch (Throwable ex) {
			loger.error("Time Socket Close : " + ex);
		}
		this.interrupt();
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			try {
				// Call accept() to receive the next connection
				Socket socket = serverSocket.accept();
				// Pass the socket to the RequestHandler thread for processing
				RequestHandler requestHandler = new RequestHandler(socket,emf);
				requestHandler.start();
			} catch (Throwable e) {
				loger.error("Time Request Handle : " + e);
			}
		}
	}
}
