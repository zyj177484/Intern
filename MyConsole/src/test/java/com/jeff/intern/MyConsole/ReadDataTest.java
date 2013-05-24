package com.jeff.intern.MyConsole;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;

import net.sf.json.JSONObject;

import org.junit.* ;
import static org.junit.Assert.* ;

public class ReadDataTest {
    private ReadData readData=new ReadData();    
    
    @Test
    public void testBuildJsonObject() throws DataException
    {        
        Reader reader=new StringReader("{\"lilei\":{\"age\":12}}");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("age", 12);
        JSONObject jsonObject2=new JSONObject();
        jsonObject2.put("lilei", jsonObject);
        JSONObject jsonObject3=new JSONObject();
        jsonObject3.put("root", jsonObject2);
        JSONObject temp=ReadData.buildJsonObject(reader);
        assertEquals(temp,jsonObject3);
    }
    
    @Test
    public void testBuildData() throws Exception
    {
        File file = new File("newFile");
        JSONObject jsonO1=new JSONObject();
        JSONObject jsonO2=new JSONObject();
        jsonO2.put("root", jsonO1);
        assertEquals(ReadData.buildData(file),jsonO2);
        
        file = new File("oldFile");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("age", 12);
        JSONObject jsonObject2=new JSONObject();
        jsonObject2.put("lilei", jsonObject);
        JSONObject jsonObject3=new JSONObject();
        jsonObject3.put("root", jsonObject2);
        assertEquals(ReadData.buildData(file),jsonObject3);
    }
}
