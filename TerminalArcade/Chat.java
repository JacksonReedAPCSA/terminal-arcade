package TerminalArcade;

import java.util.ArrayList;


public class Chat{
    public static ArrayList<String> chat = new ArrayList<>();
    public static String text;

    public Chat(){
        chat = new ArrayList<>();
    }

    public static void add(String msg){
        text = msg;
        chat.add(msg);
    }

    public String toString(){
        String msg = "";
        for(String line: chat){
            msg += line + "\n";
        }
        return msg;
    }

    public static void send(){
        if(text != null){
            System.out.println(text);
        }
    }
}
