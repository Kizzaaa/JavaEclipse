package com.kizzaa.javaeclipse.server;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryo.*;

public class mainServer extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public static Server server;
	public static Kryo kryo;
	
	public static JLabel messageLabel;
	
	public static String username = "Admin";
	public static String password = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"; //sha256 for password
	
	public static String[] news = new String[11];
	
	public static void main(String[] args) {
		
		readFile("data files/news.txt");
		
		new mainServer().setVisible(true);		
		
		startServer(54555, 54777);
	    
	    kryo = server.getKryo();
	    kryo.register(reqLogin.class);
	    kryo.register(resLogin.class);
	    kryo.register(resNews.class);
	    kryo.register(String[].class);
	    
	    server.addListener(new Listener() {
	        public void received (Connection connection, Object object) {
	        	resNews sendNews = new resNews();
	        	sendNews.news = news;
	        	connection.sendTCP(sendNews);
	        	
	           if (object instanceof reqLogin) {
	        	  reqLogin request = (reqLogin)object;
	        	  System.out.println(request.password);
	              if(request.username.toLowerCase().equals(username.toLowerCase()) && request.password.equals(password)){
	            	  messageLabel.setText(request.username + " has logged in");
	            	  
	            	  resLogin response = new resLogin();
	            	  response.success = true;
	            	  connection.sendTCP(response);
	              }else{
	            	  
	            	  messageLabel.setText(request.username + " attempted to log in");
	            	  
	            	  resLogin response = new resLogin();
	            	  response.success = false;
	            	  connection.sendTCP(response);
	              }
	           }
	        }
	     });
	}
	
	public static void readFile(String path){
		Path file = Paths.get(path);
		int i = 0;
		try (InputStream in = Files.newInputStream(file);
		    BufferedReader reader =
		      new BufferedReader(new InputStreamReader(in))) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	news[i] = line;
		        //System.out.println(line);
		    	i++;
		    }
		} catch (IOException x) {
		    System.err.println(x);
		}
	}
	
	private mainServer(){
		super("Java Eclipse Server");
		setSize(500,200);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, 500, 200);
		add(panel);
		
		JButton button = new JButton("lmao");
		button.setBounds(0, 0, 100, 40);
		panel.add(button);
		
		messageLabel = new JLabel("I'm a label in the window",SwingConstants.CENTER);
		messageLabel.setPreferredSize(new Dimension(300, 100));
		messageLabel.setBounds(140, -30, 250, 100);
		panel.add(messageLabel);
	}
	
	public static void startServer(int tcp, int udp){
		server = new Server();
	    server.start();
	    try {
			server.bind(tcp, udp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}