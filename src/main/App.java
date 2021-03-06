package main;


import main.ui.MenuBar;

import javax.swing.*;

import static main.World.windowHeight;
import static main.World.windowWidth;

public class App {

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("飞机大战");
        
        World world = null;
        try {
            world = new World();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        MenuBar menuBar = new MenuBar();
        
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth + 15, windowHeight + 61);
        frame.setLocationRelativeTo(null);

        frame.add(world);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);  // make the window visible, and call the paint function
    }
}
