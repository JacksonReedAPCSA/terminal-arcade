package TerminalArcade;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Lobby extends JFrame{
    public Lobby(){
        setFrame("Terminal Arcade", 500);
        this.pack();
    }

    public void setFrame(String title, int size){
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(size, size);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Lobby();
    }
}
