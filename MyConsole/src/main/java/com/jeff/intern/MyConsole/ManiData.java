package com.jeff.intern.MyConsole;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.locks.*;

import net.sf.json.*;

public class ManiData implements Runnable {
    private static JSONObject root;
    private static int deep;
    private static Lock lockJson = new ReentrantLock();
    private static Lock lockdeep = new ReentrantLock();
    private static boolean quit;
    private static File file;
    private String ctype, ob, key, value;
    private JSONObject now;

    /**
     * Construction of ManiData
     * @param ctype command type is must
     * @param ob command object can be null
     * @param key command key can be null
     * @param value command value can be null
     * @param file JsonData file is must
     */
    public ManiData(String ctype, String ob, String key, String value,File file) {
        this.ctype = ctype;
        this.ob = ob;
        this.key = key;
        this.value = value;
        this.file=file;
        now = root.getJSONObject("root");
        if (deep == 1)
            now = now.getJSONObject("entries");
    }       
    
    /**
     * The main logical part to deal with command.
     */
    @Override
    public void run() {
        // command ls
        if (ctype.equals("ls")) {
            System.out.print("ab> ");
            Iterator it = now.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                System.out.print(key + " ");
            }
            System.out.println();
        } else if (ctype.equals("cd")) {
            // command cd
            lockdeep.lock();
            if (deep >= 1) {
                cdError();
                System.out.print("ab> ");
                return;
            }
            lockdeep.unlock();
            if (ob.equals(""))
                System.out.println("ab> You didn't enter any key");
            else {
                if (now.containsKey(ob)) {
                    now = now.getJSONObject(ob);
                    lockdeep.lock();
                    deep++;
                    lockdeep.unlock();
                } else
                    noEntry();
            }
        }
        // command cat
        else if (ctype.equals("cat")) {
            if (ob.equals(""))
                unrecognizedCommand();
            else {
                if (now.containsKey(ob))
                    System.out.println("ab> \"" + ob + "\" : "
                            + now.getJSONObject(ob));
                else
                    noEntry();
            }
        }
        // command add
        else if (ctype.equals("add")) {
            lockdeep.lock();
            if (deep != 1) {
                addError();
                System.out.print("ab> ");
                return;
            }
            lockdeep.unlock();
            if (key.equals(""))
                System.out.println("ab> You didn't enter any key");
            else {
                if (now.containsKey(key))
                    existEntry();
                else {
                    Reader reader = new StringReader(value);
                    try {
                        JSONObject vJSONObject = ReadData.buildJsonObject(
                                reader).getJSONObject("root");
                        lockJson.lock();
                        now.put(key, vJSONObject);
                        lockJson.unlock();
                        System.out.println("address entry added");                                                
                        while (!file.canWrite()) Thread.sleep(1000);
                        BufferedWriter output = new BufferedWriter(new FileWriter(file));
                        output.write(root.getJSONObject("root").toString());
                        output.close();
                    } catch (DataException | IOException | InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        // command remove
        else if (ctype.equals("remove")) {
            if (now.containsKey(key)) {
                lockJson.lock();
                now.remove(key);
                lockJson.unlock();                
                try {
                    while (!file.canWrite()) Thread.sleep(1000);
                    BufferedWriter output = new BufferedWriter(new FileWriter(file));
                    output.write(root.getJSONObject("root").toString());
                    output.close();
                } catch (IOException | InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else
                noEntry();
        } else if (ctype.equals("!help"))
            unrecognizedCommand();
        else
            unrecognizedCommand();
        System.out.print("ab> ");
    }

    private void existEntry() {
        System.out.println("ab> This entry already exists.");
    }

    private void addError() {
        System.out.println("ab> You can't add in this directory.");
    }

    private void unrecognizedCommand() {
        System.out.println("ab> Your command is unrecognized.");
        System.out
                .println("ab> You can use these command \"ls, cd, cat, add, remove\"");
    }

    private void noEntry() {
        System.out.println("ab> This entry doesn't exist.");
    }

    private void cdError() {
        System.out.println("ab> You can't cd anymore.");
    }

    public static void setRoot(JSONObject root) {
        ManiData.root = root;
    }

    public static void setDeep(int deep) {
        ManiData.deep = deep;
    }

    public static boolean isQuit() {
        return quit;
    }

    public static void setQuit(boolean quit) {
        ManiData.quit = quit;
    }
}
