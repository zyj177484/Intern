package com.jeff.intern.MyConsole;

import java.io.*;
import java.util.Hashtable;
import java.util.LinkedList;

import org.omg.CORBA.portable.ValueBase;

import net.sf.json.*;

public class ReadData {
    
    /**
     * Build JsonData From filepath you give with right format of jsondata
     * @throws Exception 
     */
    public static JSONObject buildData(File file) throws Exception
    {        
        if (!file.exists())
        {
            file.createNewFile();
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write("{}");
            output.close();
        }
        int count=10;
        while (!file.canRead()) 
        {
            Thread.sleep(1000);
            count--;
            if (count==0)
            {
                System.out.println("Can't read the JsonData");
                return null;
            }
        } 
        Reader reader = new InputStreamReader(new FileInputStream(file)); 
        JSONObject root=buildJsonObject(reader);
        reader.close();
        return root;
    }
    
    /**
     * Build a JSONObject by a reader for a json data with right format.
     * @param reader
     * @return JSONObject
     * @throws DataException
     */
    public static JSONObject buildJsonObject(Reader reader) throws DataException
    {
        Tokenizer tokenizer=new Tokenizer(reader);
        JSONObject root=new JSONObject();        
        LinkedList<String> stackBrace=new LinkedList<String>();
        stackBrace.addLast("\"root\"");
        stackBrace.addLast(":");        
        LinkedList<JSONObject> stackJSON=new LinkedList<JSONObject>();
        stackJSON.addLast(root);
        while (true)
        {            
            String s=tokenizer.next();
            if (s==null) break;
            if (s.equals("{"))
            {
                stackBrace.add("{");
                JSONObject tempJsonObject=new JSONObject();
                stackJSON.addLast(tempJsonObject);
            }
            else if (s.equals("}"))
            {
                if (!stackBrace.isEmpty())
                    stackBrace.removeLast();
                else throw new DataException("Data Exception");
                
                if (!stackBrace.isEmpty())
                    stackBrace.removeLast();
                else throw new DataException("Data Exception");
                
                if (!stackBrace.isEmpty() && !stackJSON.isEmpty())
                {
                    String tempString=stackBrace.removeLast();
                    JSONObject tempJsonObject=stackJSON.removeLast();                
                    stackJSON.getLast().put(tempString.substring(1, tempString.length()-1), tempJsonObject);
                }
                else throw new DataException("Data Exception");
            }
            else if (s.equals(":"))
            {
                stackBrace.addLast(s);
            }            
            else
            {
                if (stackBrace.getLast().equals(":"))
                {                            
                    if (!stackBrace.isEmpty())
                        stackBrace.removeLast();
                    else throw new DataException("Data Exception");
                    if (!stackBrace.isEmpty() && !stackJSON.isEmpty())
                    {
                        String tempString=stackBrace.removeLast();
                        if (s.charAt(0)!='"')
                            stackJSON.getLast().put(tempString.substring(1, tempString.length()-1), Integer.parseInt(s));
                        else stackJSON.getLast().put(tempString.substring(1, tempString.length()-1), s.substring(1,s.length()-1));
                    }
                    else throw new DataException("Data Exception");
                }
                else stackBrace.addLast(s);
            }
        }        
        if (stackJSON.size()!=1) throw new DataException("Data Exception");
        return root;    
    }
}
