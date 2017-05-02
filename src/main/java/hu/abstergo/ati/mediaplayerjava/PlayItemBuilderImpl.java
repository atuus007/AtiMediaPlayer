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
public class PlayItemBuilderImpl implements PlayItemBuilder{
    private PlayItem playItem;
    public PlayItemBuilderImpl(){
        playItem=new PlayItem();
    }
    
    @Override
    public PlayItemBuilder setTitle(String title) {
        playItem.setTitle(title);
        return this;
    }

    @Override
    public PlayItemBuilder setDiscNumber(int discNumber) {
        playItem.setDiscNumber(discNumber);
        return this;
        
    }

    @Override
    public PlayItemBuilder setTracNumber(int tracNumber) {
        playItem.setTracNumber(tracNumber);
        return this;
        
    }

    @Override
    public PlayItemBuilder setArtist(String artist) {
        playItem.setArtist(artist);
        return this;
        
    }

    @Override
    public PlayItemBuilder setImg(Image img) {
        playItem.setImg(img);
        return this;
    }

    @Override
    public PlayItemBuilder setDiskNumber(String diskNumber) {
        playItem.setDiskNumber(diskNumber);
        return this;
        
    }

    @Override
    public PlayItemBuilder setYear(int year) {
        playItem.setYear(year);
        return this;
        
    }

    @Override
    public PlayItemBuilder setAlbum(String album) {
        playItem.setAlbum(album);
        return this;
    }

    @Override
    public PlayItemBuilder setComposer(String composer) {
        playItem.setComposer(composer);
        return this;
        
    }

    @Override
    public PlayItemBuilder setGenre(String genre) {
        playItem.setGenre(genre);
        return this;
    }

    @Override
    public PlayItemBuilder setLenght(String lenght) {
        playItem.setLenght(lenght);
        return this;
    }

    @Override
    public PlayItemBuilder setExtension(String extension) {
        playItem.setExtension(extension);
        return this;
    }

    @Override
    public PlayItemBuilder setUriPath(String uriPath) {
        playItem.setUriPath(uriPath);
        return this;
    }

    @Override
    public PlayItem build() {
        return playItem;
    }
    
}
