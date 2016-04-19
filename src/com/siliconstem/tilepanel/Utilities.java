/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siliconstem.tilepanel;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author HP ENVY
 */
public class Utilities {
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
