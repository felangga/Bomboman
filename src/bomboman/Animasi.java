/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bomboman;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author fcomputer
 */
public class Animasi {

    private int X;
    private int Y;
    private JLabel gambar = new JLabel();
    private ImageIcon a1;

    Animasi(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public JLabel output() {
        return gambar;
    }
    
    public void setVisibility (Boolean Visibel) {
        gambar.setVisible(Visibel);
    }
    
    public ImageIcon getFrame(int nomor) {
        ImageIcon frame = null;
        switch (nomor) {
            case 0:
                frame = new ImageIcon(this.getClass().getResource("gambar/blast1.png"));
                break;
            case 1:
                frame = new ImageIcon(this.getClass().getResource("gambar/blast2.png"));
                break;
            case 2:
                frame = new ImageIcon(this.getClass().getResource("gambar/blast3.png"));
                break;
        }
        return frame;
    }

    public JLabel doAnim(int nomerframe) {
        switch (nomerframe) {
            case 0:
                a1 = new ImageIcon(this.getClass().getResource("gambar/blast1.png"));
                break;
            case 1:
                a1 = new ImageIcon(this.getClass().getResource("gambar/blast2.png"));
                break;
            case 2:
                a1 = new ImageIcon(this.getClass().getResource("gambar/blast3.png"));
                break;
        }
        gambar.setBounds(X - 20, Y - 10, 64, 64);
        gambar.setIcon(a1);
        gambar.repaint();
        return gambar;
    }
}  
