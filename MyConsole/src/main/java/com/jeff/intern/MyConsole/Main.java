package com.jeff.intern.MyConsole;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.Scanner;

import net.sf.json.JSONObject;

public class Main {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        System.out.print("Please Input your JsonData path:");        
        Scanner scan = new Scanner(System.in);
        String path = scan.nextLine();
        File file = new File(path);
        MyConsole myConsole;
        myConsole=MyConsole.getMyConsole();
        System.out.println("Reading Data...");
        JSONObject jsonObject=ReadData.buildData(file);
        myConsole.startWork(jsonObject, file, scan);
    }
}
