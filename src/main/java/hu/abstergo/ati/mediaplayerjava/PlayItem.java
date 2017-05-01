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
public class PlayItem {
    private String title;
    private String author;
    private String genre;
    private String extension;

    public PlayItem(String title, String author, String genre, String extension) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.extension = extension;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getExtension() {
        return extension;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return "PlayItem{" + "title=" + title + ", author=" + author + ", genre=" + genre + ", extension=" + extension + '}';
    }
    
    
}
