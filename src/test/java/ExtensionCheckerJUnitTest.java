/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import hu.abstergo.ati.mediaplayerjava.Model.ExtensionChecker;
/**
 *
 * @author Fodor Edit
 */
public class ExtensionCheckerJUnitTest {
    
    public ExtensionCheckerJUnitTest() {
    }
    
    @Test
    public void testExtension(){
        ExtensionChecker ext = new ExtensionChecker();
        boolean test=ext.isGoodExtension("dfadfasf.xff");
        boolean exp=false;
        assertEquals(exp, test);
    }
    @Test
    public void testExtension2(){
        ExtensionChecker ext = new ExtensionChecker();
        boolean test=ext.isGoodExtension("dfadfasf.wav");
        boolean exp=true;
        assertEquals(exp, test);
    }
    @Test
    public void testExtension3(){
        ExtensionChecker ext = new ExtensionChecker();
        String test=ext.getMediaExtension("fdsafasdfasdf.mp4");
        String exp="mp4";
        assertEquals(exp, test);
    }
    @Test
    public void testExtension4(){
        ExtensionChecker ext = new ExtensionChecker();
        String test=ext.getMediaExtension("fdsafasdfasdf.dfasdf");
        String exp="mp4";
        assertNotEquals(exp, test);
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
