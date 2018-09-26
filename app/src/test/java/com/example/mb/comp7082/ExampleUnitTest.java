package com.example.mb.comp7082;

import com.example.mb.comp7082.database.DataStorage;
import com.example.mb.comp7082.database.IDataStore;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testState() throws Exception{
        IDataStore dss = new DataStorage();
        dss.saveState("Testing");
        assertEquals( "Testing", dss.getState());
    }
}