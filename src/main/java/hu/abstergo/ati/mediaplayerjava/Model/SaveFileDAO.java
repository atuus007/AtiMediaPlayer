/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.abstergo.ati.mediaplayerjava.Model;

import java.util.List;

/**
 *
 * @author Fodor Edit
 */
public interface SaveFileDAO {
    public void saveList(List<PlayItem> items);
    public List<PlayItem> loadItems();
}
