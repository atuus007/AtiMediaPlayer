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
import hu.abstergo.ati.mediaplayerjava.Model.TimeFromatConverter;
import javafx.util.Duration;

/**
 *
 * @author Ati
 */
public class TimeFormatJUnitTest {
    
    public TimeFormatJUnitTest() {
    }
    
    @Test
    public void test1(){
        Duration dr=Duration.ZERO;
        String pont=TimeFromatConverter.formatTime(dr);
        String exp="00:00";
        assertNotEquals(exp, pont);
        
    }
}
