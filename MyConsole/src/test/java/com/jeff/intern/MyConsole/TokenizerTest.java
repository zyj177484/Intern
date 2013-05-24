package com.jeff.intern.MyConsole;

import java.io.Reader;
import java.io.StringReader;

import org.junit.* ;
import static org.junit.Assert.* ;

public class TokenizerTest {    
    private Reader reader = new StringReader("{\"nihao\" :12 }");
    private Tokenizer tokenizer=new Tokenizer(reader);
    
    @Test
    public void testNext()
    {
        assertEquals(tokenizer.next(), "{");
        assertEquals(tokenizer.next(), "\"nihao\"");
        assertEquals(tokenizer.next(), ":");        
        assertEquals(tokenizer.next(), "12");
        assertEquals(tokenizer.next(), "}");
    }
}
