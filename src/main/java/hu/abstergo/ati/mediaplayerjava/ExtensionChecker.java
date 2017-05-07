/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.abstergo.ati.mediaplayerjava;

/**
 *
 * @author Ati
 */
public class ExtensionChecker {
    public static boolean isGoodExtension(String path){
        String temp;
        int pos = 0;
        if (path.lastIndexOf(".") != -1) {
            pos = path.lastIndexOf(".");
        }
        temp = path.substring(pos + 1, path.length());
        if(temp.equals("mp3")|| temp.equals("mp4")){
            return true;
        }else {
            return false;
        }
        
    }
}
