package com.jeff.intern.MyConsole;

import java.io.*;

public class Tokenizer {
    
    private Reader reader;
    private int tempchar;
    private boolean start=false;
    
    /**
     * Build Tokennizer of JSONData by a reader
     * @param reader
     */
    public Tokenizer(Reader reader)
    {
        this.reader=reader;
    }
    
    /**
     * get the next jsondata with right format
     * @return
     */
    public String next()
    { 
        StringBuffer resultBuffer=new StringBuffer();
        try {                                      
            while (true)
            { 
                if (start==false)
                {
                    start=true;
                    tempchar = reader.read();                        
                }
                if (tempchar==-1)
                    return null;
                
                if (((char)tempchar)=='"')
                { 
                    resultBuffer.append((char)tempchar);                    
                    while ((char)(tempchar = reader.read()) != '"')                    
                        resultBuffer.append((char)tempchar);
                    resultBuffer.append('"');
                    tempchar = reader.read();
                    return resultBuffer.toString();
                } 
                else if (((char)tempchar)=='{')
                {
                    resultBuffer.append((char)tempchar);
                    tempchar = reader.read();
                    return resultBuffer.toString();
                }
                else if (((char)tempchar)=='}')
                {
                    resultBuffer.append((char)tempchar);
                    tempchar = reader.read();
                    return resultBuffer.toString();
                }
                else if (((char)tempchar)==':')
                {
                    resultBuffer.append((char)tempchar);
                    tempchar = reader.read();
                    return resultBuffer.toString();
                }
                else if (((char)tempchar)>='0' && ((char)tempchar)<='9')
                {
                    resultBuffer.append((char)tempchar);
                    while (true)
                    {
                        tempchar = reader.read();
                        if (((char)tempchar)>='0' && ((char)tempchar)<='9')
                                resultBuffer.append((char)tempchar);
                        else break;
                    }
                    return resultBuffer.toString();
                }
                else 
                {
                    tempchar = reader.read();
                }
            }            
        } 
        catch (Exception e) 
        { 
            e.printStackTrace(); 
            return null;
        }
    }
}
