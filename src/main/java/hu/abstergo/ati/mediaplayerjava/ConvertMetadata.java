/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.abstergo.ati.mediaplayerjava;

import javafx.collections.ObservableMap;

/**
 *
 * @author Ati
 */
public class ConvertMetadata {
  private String title;
  
  public ConvertMetadata(ObservableMap<String, Object> mediaMetadata){
      if(mediaMetadata.get("title")==null){
          this.title =null;
      }else{
          this.title=mediaMetadata.get("title").toString();
      }
      
  }  
  public String getValami(){
      return this.title;
  }
}
