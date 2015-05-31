package com.kizzaa.javaeclipse.server;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryo.*;
import com.kizzaa.javaeclipse.server.sClasses.*;

public class mainServer extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public static Server server;
	public static Kryo kryo;
	
	public static JLabel messageLabel;
	
	public static String[] news = new String[11];
	
	public static void main(String[] args) {
		
		readFile("data files/news.txt");
		
		new mainServer().setVisible(true);		
		
		startServer(54555, 54777);
	    
	    kryo = server.getKryo();
	    kryo.register(reqLogin.class);
	    kryo.register(resLogin.class);
	    kryo.register(reqRegister.class);
	    kryo.register(resRegister.class);
	    kryo.register(resNews.class);
	    kryo.register(String[].class);
	    
	    server.addListener(new Listener() {
	        public void received (Connection connection, Object object) {
	        	/*resNews sendNews = new resNews();
	        	sendNews.news = news;
	        	connection.sendTCP(sendNews);*/
	        	
	           if (object instanceof reqLogin) {
	        	  reqLogin request = (reqLogin)object;
	        	  resLogin response = new resLogin();
	        	  String user = request.username.toLowerCase();
	        	  File f = new File("data files/users/" + user + "/" + user + ".txt");  
	        	  if(f.exists() && !f.isDirectory()) {
	        		  try(BufferedReader br = new BufferedReader(new FileReader("data files/users/" + user + "/" + user + ".txt"))) {
	        		        String line = br.readLine();

	        		        if(line.equals(sha256(user + request.password + user))){
	        		        	response.success = true;
	        		        	messageLabel.setText(request.username + " logged in");
	        		        }else{
	        		        	response.success = false;
	        		        }
	        		        
	        		    } catch (IOException e) {
	        		    	response.success = false;
	        		    	messageLabel.setText(request.username + " attempted to log in");
							e.printStackTrace();
						}
	        	  }else{
	        		  response.success = false;
	        		  messageLabel.setText(request.username + " attempted to log in");
	        	  }
	        	  connection.sendTCP(response);
	           }
	           
	           if (object instanceof reqRegister) {
	        	   reqRegister request = (reqRegister)object;
	        	   
	        	   resRegister response = new resRegister();
	        	   String user = request.username.toLowerCase();
	        	   File f = new File("data files/users/" + user + "/" + user + ".txt");  
	        	   if(f.exists() && !f.isDirectory()) {
	        		   response.success = false;
	        	   }else{
	        		   PrintWriter writer;
	        		   try {
	        			   File f1 = new File("data files/users/" + user + "/" + user + ".txt");
	        			   f1.getParentFile().mkdirs(); 
	        			   f1.createNewFile();
	        			   
							writer = new PrintWriter("data files/users/" + user + "/" + user + ".txt", "UTF-8");
							writer.println(sha256(user + request.password + user));
			        		writer.close();
			        		response.success = true;
						} catch (FileNotFoundException | UnsupportedEncodingException e) {
							response.success = false;
							e.printStackTrace();
						} catch (IOException e) {
							response.success = false;
							e.printStackTrace();
						}
	        	   }
	        	   connection.sendTCP(response);
	           }
	        }
	     });
	}
	
	public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

        return hexString.toString();
        } catch(Exception ex){
        	throw new RuntimeException(ex);
        }
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