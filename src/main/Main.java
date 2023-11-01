package main;

import javax.swing.*;

public class Main {
    //Static reference to the main window
    public static JFrame window;

    public static void main(String[] args) {
        window = new JFrame();
        GamePanel gamePanel = new GamePanel();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure");



        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);


        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}