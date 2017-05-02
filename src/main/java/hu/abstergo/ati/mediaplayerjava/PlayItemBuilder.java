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
public interface PlayItemBuilder {
    

    public PlayItemBuilder setTitle(String title); 

    public PlayItemBuilder setDiscNumber(int discNumber);

    public PlayItemBuilder setTracNumber(int tracNumber); 

    public PlayItemBuilder setArtist(String artist);

    public PlayItemBuilder setImg(Image img);

    public PlayItemBuilder setDiskNumber(String diskNumber);

    public PlayItemBuilder setYear(int year);

    public PlayItemBuilder setAlbum(String album);

    public PlayItemBuilder setComposer(String composer);

    public PlayItemBuilder setGenre(String genre);

    public PlayItemBuilder setLenght(String lenght);

    public PlayItemBuilder setExtension(String extension);

    public PlayItemBuilder setUriPath(String uriPath);
    
    PlayItem build();
}
