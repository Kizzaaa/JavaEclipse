package com.kizzaa.javaeclipse.client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class fileManage {
	
	public fileManage(){
		
	}

	public void write(String path, String output){
		String s = output;
	    byte data[] = s.getBytes();
	    Path p = Paths.get(path);

	    try (OutputStream out = new BufferedOutputStream(
	      Files.newOutputStream(p))) {
	      out.write(data, 0, data.length);
	    } catch (IOException x) {
	      System.err.println(x);
	    }
	}
	
	public static String[] read(String path){
		Path file = Paths.get(path);
		String[] o = new String[20];
		try (InputStream in = Files.newInputStream(file);
		    BufferedReader reader =
		      new BufferedReader(new InputStreamReader(in))) {
		    String line = null;
		    int i = 0;
		    while ((line = reader.readLine()) != null) {
		        o[i] = line;
		        i++;
		    }
		} catch (IOException x) {
		    System.err.println(x);
		}
		return o;
	}
}
