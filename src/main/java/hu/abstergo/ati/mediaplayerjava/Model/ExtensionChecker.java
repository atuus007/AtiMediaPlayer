/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.abstergo.ati.mediaplayerjava.Model;

/**
 *
 * @author Ati
 */
public class ExtensionChecker implements IExtensionFinder{
    public boolean isGoodExtension(String path){
        String ext=getMediaExtension(path);
        
        return (ext.contains("mp3")|| ext.contains("mp4")||ext.contains("flv")||ext.contains("wav"));
              
    }

    @Override
    public String getMediaExtension(String in) {
        String temp="";
        int pos;
        if (in.lastIndexOf(".") != -1) {
            pos = in.lastIndexOf(".");
            temp = in.substring(pos + 1, in.length());
        }
        return temp;
    }

  
}
