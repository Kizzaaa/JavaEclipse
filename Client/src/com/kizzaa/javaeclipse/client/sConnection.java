package com.kizzaa.javaeclipse.client;

import java.io.IOException;
import java.security.MessageDigest;

import javax.swing.JOptionPane;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.kizzaa.javaeclipse.client.cClasses.*;

public class sConnection {
	
	public static Client client;
	public static Boolean connected = false;

	public sConnection(){
		
	}
	
	public static void connect(String ip, int tcp, int udp){
		
		client = new Client();
	    client.start();
	    try {
			client.connect(5000, ip, tcp, udp);
			connected = true;
		} catch (IOException e) {
			connected = false;
			System.out.println("Could not connect to the server");
			//e.printStackTrace();
		}
	    
	    Kryo kryo = client.getKryo();
	    kryo.register(reqLogin.class);
	    kryo.register(resLogin.class);
	    kryo.register(reqRegister.class);
	    kryo.register(resRegister.class);
	    kryo.register(resNews.class);
	    kryo.register(String[].class);
	    
	    client.addListener(new Listener() {
	    	public void received (Connection connection, Object object) {
		           if (object instanceof resNews) {
		        	  resNews response = (resNews)object;
		              Menu.sNews = response.news;
		           }
		        }
	    });
	    
	}
	
	public static void login(String username, String password){
	    
	    reqLogin request = new reqLogin();
	    request.username = username;
	    
	    request.password = sha256(password);
	    
	    client.sendTCP(request);
	    
	    client.addListener(new Listener() {
	        public void received (Connection connection, Object object) {
	           if (object instanceof resLogin) {
	        	   resLogin response = (resLogin)object;
	        	   if(response.success){
	        		   JOptionPane.showMessageDialog(null, "Login Successful", "Information", JOptionPane.INFORMATION_MESSAGE);
	        	   }else{
	        		   JOptionPane.showMessageDialog(null, "Could not log in", "Information", JOptionPane.INFORMATION_MESSAGE);
	        	   }
	           }
	        }
	     });
	}
	
	public static void register(String username, String password) {
		reqRegister request = new reqRegister();
		request.username = username;
		request.password = sha256(password);
		
		client.sendTCP(request);
		
		client.addListener(new Listener(){
			public void received(Connection connection, Object object){
				if (object instanceof resRegister) {
					resRegister response = (resRegister)object;
					if(response.success){
						Menu.registered = true;
						JOptionPane.showMessageDialog(null, "Registration Successful", "Information", JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(null, "Could not register.", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
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
}
