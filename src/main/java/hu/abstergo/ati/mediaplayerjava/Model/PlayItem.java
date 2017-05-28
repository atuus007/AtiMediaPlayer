/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.abstergo.ati.mediaplayerjava.Model;

import java.util.Date;
import javafx.scene.image.Image;

/**
 *
 * @author Ati
 */
public class PlayItem {


    private String title;
    private String lenght;
    private String extension;
    private String uriPath;
 
    public PlayItem(String title, String lenght, String extension, String uriPath) {
        this.title = title;
        this.lenght = lenght;
        this.extension = extension;
        this.uriPath = uriPath;
       
    }

    public String getTitle() {
        return title;
    }
    
    public String getLenght() {
        return lenght;
    }

    public String getExtension() {
        return extension;
    }

    public String getUriPath() {
        return uriPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLenght(String lenght) {
        this.lenght = lenght;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setUriPath(String uriPath) {
        this.uriPath = uriPath;
    }

    @Override
    public String toString() {
        return "" + title + " "+lenght;
    }

    
    

}
