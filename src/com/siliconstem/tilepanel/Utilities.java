/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siliconstem.tilepanel;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

/**
 *
 * @author HP ENVY
 */
public class Utilities {
    public static String[] loadCSVToArray(URL url){
        String separator=",";
        BufferedReader br=null;
        String[] tileNames=null;
        try {
            String line="";
            StringBuilder completeFile=new StringBuilder();
            File file = new File(url.getFile());
            br = new BufferedReader(new FileReader(file)); 
            while ((line = br.readLine()) != null) {
                completeFile.append(line+separator);
            }
            tileNames = completeFile.toString().split(separator);


	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
        return tileNames;
    }
    public static BufferedImage loadBufferedImage(URL url){
        BufferedImage tiles=null;
        try{
            tiles = ImageIO.read(url);
        }
        catch(Exception e){
            System.err.println("Error upening URL:"+e.getMessage());
        }
        return tiles;
    }
    
      public static void pause(int s){
        try{
            if(s>500){
                TimeUnit.MILLISECONDS.sleep(s);  
            }
            else{
                TimeUnit.SECONDS.sleep(s);
            } 
                
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
