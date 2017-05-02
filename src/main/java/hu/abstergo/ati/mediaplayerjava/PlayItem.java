/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.abstergo.ati.mediaplayerjava;

import javafx.scene.image.Image;

/**
 *
 * @author Ati
 */
public class PlayItem {


    private String title;
    private int discNumber;
    private int tracNumber;
    private String artist;
    private Image img;
    private String diskNumber;
    private int year;
    private String album;
    private String composer;
    private String genre;
    private String lenght;
    private String extension;
    private String uriPath;

    public PlayItem(){
        
    }

    public String getTitle() {
        return title;
    }

    public int getDiscNumber() {
        return discNumber;
    }

    public int getTracNumber() {
        return tracNumber;
    }

    public String getArtist() {
        return artist;
    }

    public Image getImg() {
        return img;
    }

    public String getDiskNumber() {
        return diskNumber;
    }

    public int getYear() {
        return year;
    }

    public String getAlbum() {
        return album;
    }

    public String getComposer() {
        return composer;
    }

    public String getGenre() {
        return genre;
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

    public void setDiscNumber(int discNumber) {
        this.discNumber = discNumber;
    }

    public void setTracNumber(int tracNumber) {
        this.tracNumber = tracNumber;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public void setDiskNumber(String diskNumber) {
        this.diskNumber = diskNumber;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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
    

}
