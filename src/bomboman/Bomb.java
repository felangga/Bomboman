/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bomboman;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author fcomputer
 */
public abstract class Bomb extends TimerTask {

    private int damage = 10; // Damage sebesar 10
    private int lifetime = 5; // Meledak setelah 5 detik
    private int noBomb;
    private int lokasiX = 0;
    private int lokasiY = 0;
    private int pemilikBomb = 0;
    private int radius = 32;
    private Timer timer;
    private JLabel bomb = new JLabel();

    Bomb(int pemilikBomb, int id, int X, int Y, Timer tmr) {
        this.pemilikBomb = pemilikBomb;
        lokasiX = X;
        lokasiY = Y;
        noBomb = id;
        timer = tmr;
    }
    
    public Timer getTimer() {
        return timer;
    }
    
    public int getRadius() {
        return radius;
    }

    public int getPemilik() {
        return pemilikBomb;
    }
    
    public void setPemilik(int pemilik) {
        pemilikBomb = pemilik;
    }

    public int getDamage() {
        return damage;
    }

    public JLabel tampilkan() {
        ImageIcon b1;
        if (lifetime > 0) {
            b1 = new ImageIcon(this.getClass().getResource("gambar/bomb.png"));
        } else {
            b1 = new ImageIcon(this.getClass().getResource("gambar/rumput.png"));
        }


        bomb.setIcon(b1);
        bomb.setBounds(lokasiX, lokasiY, 32, 32);
        bomb.repaint();
        return bomb;
    }

    public void setVisibility(Boolean Visibel) {
        bomb.setVisible(Visibel);

    }

    @Override
    public void run() {
        lifetime--;
        if (lifetime > -1) {
        } else {
            meledak(noBomb, lokasiX, lokasiY, timer);
        }
    }

    // abstract void dipanggil ketika bomb meledak
    public abstract void meledak(int noBomb, int X, int Y, Timer sumberTimer);
}
