package Client;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//H

import Client.Race_Track;

import java.awt.Container;
import java.awt.LayoutManager;
import javax.swing.JFrame;

public class GameFrame extends JFrame {
    private Race_Track raceTrack;

    public GameFrame() {

        this.setTitle("Race");
        this.setBounds(100, 100, 1050, 1000);
        this.setDefaultCloseOperation(3);
        Container cp = this.getContentPane();
        cp.setLayout((LayoutManager)null);
        this.raceTrack = new Race_Track();
        this.addKeyListener(raceTrack);
        this.raceTrack.setBounds(90, 90, 1050, 1000);
        cp.add(this.raceTrack);
        this.setExtendedState(MAXIMIZED_BOTH);

    }
}