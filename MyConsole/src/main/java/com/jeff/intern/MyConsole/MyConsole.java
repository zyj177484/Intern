package com.jeff.intern.MyConsole;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import net.sf.json.JSONObject;

/**
 * Console
 * 
 */
public class MyConsole {
    private static MyConsole myConsole;
    private static int count = 0;

    private MyConsole() {
    }

    /**
     * Only one Console can be built.
     * @return MyConsole
     */
    public static MyConsole getMyConsole() {
        if (count == 0) {
            count++;
            myConsole = new MyConsole();
        }
        return myConsole;
    }
    
    /**
     * Let the console start work
     * @param root JsonData's root built from file
     * @param file store the JsonData
     * @param scan read from the console
     * @throws Exception
     */
    public void startWork(JSONObject root,File file,Scanner scan) throws Exception {
        if (root==null) return;
        System.out.print("ab> ");        
        ExecutorService exec = Executors.newCachedThreadPool();
        ManiData.setDeep(0);
        ManiData.setRoot(root);
        ManiData.setQuit(false);
        while (true) {
            String s = scan.nextLine().trim().toLowerCase();
            ManiData maniData = null;
            String ctype = null, ob = null, key = null, value = null;
            int i = 0;
            // command ls
            if (s.equals("ls"))
                exec.execute(new ManiData("ls", ob, key, value,file));
            // command cd
            else if (s.charAt(i) == 'c') {
                if (i + 3 < s.length() && s.charAt(i + 1) == 'd'
                        && s.charAt(i + 2) == ' ') {
                    ctype = "cd";
                    ob = s.substring(i + 3, s.length()).trim();
                    exec.execute(new ManiData(ctype, ob, key, value,file));
                }
                // command cat
                else if (i + 4 < s.length() && s.charAt(i + 1) == 'a'
                        && s.charAt(i + 2) == 't' && s.charAt(i + 3) == ' ') {
                    ctype = "cat";
                    ob = s.substring(i + 4, s.length()).trim();
                    exec.execute(new ManiData(ctype, ob, key, value,file));                    
                } else
                    unrecognizedCommand();
            }
            // command add
            else if (s.equals("add")) {
                ctype = "add";
                System.out.print("key: ");
                key = scan.nextLine().trim();
                System.out.print("value: ");
                value = scan.nextLine().trim();
                exec.execute(new ManiData(ctype, ob, key, value,file));
            }
            // command remove
            else if (s.equals("remove")) {
                ctype = "remove";
                System.out.print("please give the key: ");
                key = scan.nextLine().trim();
                exec.execute(new ManiData(ctype, ob, key, value,file));
            } else if (s.equals("!help"))
                unrecognizedCommand();
            else if (s.equals("!quit"))
                break;
            else
                unrecognizedCommand();            
        }
        exec.shutdown();
        scan.close();
    }

    private void unrecognizedCommand() {
        System.out.println("ab> Your command is unrecognized.");
        System.out
                .println("ab> You can use these command \"ls, cd, cat, add, remove\"");
        System.out.print("ab> ");
    }
}
