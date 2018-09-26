/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bomboman;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author fcomputer
 */
public abstract class Pemain {

    private int Health = 100;
    private int posX;
    private int posY;
    private int papan[][];
    private JLabel pemain;
    private String namaPemain;
    private int bawaBomb;
    private Bomb[] b = new Bomb[200];
    private int counter = 0;
    private Timer[] tBomb = new Timer[200];
    private String[] listBomb = new String[200];
    private int Karakter;
    private int score = 0;

    Pemain(int pemainKE, int maxBomb, int papan[][]) {
        this.papan = papan;
        this.bawaBomb = maxBomb;
        pemain = new JLabel();
        Karakter = pemainKE;
        switch (pemainKE) {
            case 1:
                posX = 42;
                posY = 42;
                break;
            case 2:
                posX = 42;
                posY = 458;
                break;
            case 3:
                posX = 458;
                posY = 42;
                break;
            case 4:
                posX = 458;
                posY = 458;
                break;
        }

    }

    public int getBombBawa() {
        return bawaBomb;
    }
    
    public void setBombBawa(int jumlah) {
        bawaBomb = jumlah;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    public String getNamaPemain() {
        return namaPemain;
    }
    
    public void setNamaPemain(String nama) {
        namaPemain = nama;
    }

    public void increaseBomb() {
        bawaBomb++;
    }

    public void kenaLedakan(int damage) {
        if (Health - damage > 0) {
            Health -= damage;
        } else {
            Health = 0;
        }
    }

    public void animasi(final int karakter) {
        Thread animasi = new Thread(new Runnable() {
            @Override
            public void run() {
                String nama = "";
                switch (karakter) {
                    case 1:
                        nama = "A";
                        break;
                    case 2:
                        nama = "B";
                        break;
                    case 3:
                        nama = "C";
                        break;
                    case 4:
                        nama = "D";
                        break;
                    default:
                        nama = "A";
                        break;
                }
                ImageIcon g1 = new ImageIcon(this.getClass().getResource("gambar/" + nama + ".png"));;
                ImageIcon g2 = new ImageIcon(this.getClass().getResource("gambar/" + nama + "2.png"));
                ImageIcon m1 = new ImageIcon(this.getClass().getResource("gambar/kuburan.png"));


                int i = 0;
                while (Health > 0) {
                    try {
                        if (Health > 0) {
                            if (i % 20 < 10) {
                                pemain.setIcon(g2);

                            } else {
                                pemain.setIcon(g1);
                            }
                            i += 1;
                        } else {
                            pemain.setIcon(m1);
                        }
                        pemain.repaint();

                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Pemain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                pemain.setIcon(m1);
            }
        });
        animasi.start();
    }

    public JLabel tampilkan() {
        pemain.setBounds(getPosX(), getPosY(), 32, 32);
        pemain.setFocusable(true);
        animasi(Karakter);

        pemain.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (Health > 0) {
                    int tombolMana = e.getKeyCode();

                    if (tombolMana == KeyEvent.VK_RIGHT) {
                        geserKanan();

                    } else if (tombolMana == KeyEvent.VK_LEFT) {
                        geserKiri();

                    } else if (tombolMana == KeyEvent.VK_DOWN) {
                        geserBawah();

                    } else if (tombolMana == KeyEvent.VK_UP) {
                        geserAtas();
                    } else if (tombolMana == KeyEvent.VK_SPACE) {
                        if (bawaBomb > 0) {
                            tBomb[counter] = new Timer();
                            bawaBomb--;
                            listBomb[counter] = getPosX() + "-" + getPosY() + "-" + Karakter;
                            b[counter] = new Bomb(Karakter, counter, getPosX(), getPosY(), tBomb[counter]) {
                                @Override
                                public void meledak(int noBomb, int X, int Y, Timer sumberTimer) {
                                    int radius = b[noBomb].getRadius();
                                    if (getPosX() <= X + radius && getPosY() <= Y + radius && getPosX() >= X - radius && getPosY() >= Y - radius) {
                                        kenaLedakan(b[noBomb].getDamage());
                                        if (b[noBomb].getPemilik() == Karakter) {
                                            setScore(getScore() - 100);
                                        }
                                    }

                                    listBomb[noBomb] = "";
                                    tampil_bomb(b[noBomb].tampilkan(), listBomb);
                                    Animasi an[] = new Animasi[3];
                                    for (int i = 0; i < 3; i++) {
                                        try {
                                            an[i] = new Animasi(X, Y);
                                            tampil_bomb(an[i].doAnim(i), listBomb);
                                            Thread.sleep(200);
                                            an[i].setVisibility(false);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(BomboMain.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }

                                    tBomb[noBomb].cancel();
                                    if (noBomb == counter) {
                                        counter = 0;
                                    }

                                }
                            };
                            tBomb[counter].schedule(b[counter], 0, 1000);
                            tampil_bomb(b[counter].tampilkan(), listBomb);
                            counter++;
                        }
                    }

                    pemain.setLocation(posX, posY);
                    pemain.repaint();
                }
            }
        });


        return pemain;
    }

    public abstract void tampil_bomb(Component b, String[] listBomb);

    public JLabel getComponent() {
        return pemain;
    }

    private void geserKanan() {
        if (papan[(posX + 32) / 32][posY / 32] != 1) {
            posX += 32;
        }
    }

    private void geserKiri() {
        if (papan[(posX - 32) / 32][posY / 32] != 1) {
            posX -= 32;
        }
    }

    private void geserAtas() {
        if (papan[posX / 32][(posY - 32) / 32] != 1) {
            posY -= 32;
        }
    }

    private void geserBawah() {
        if (papan[posX / 32][(posY + 32) / 32] != 1) {
            posY += 32;
        }
    }

    /**
     * @return the Health
     */
    public int getHealth() {
        return Health;
    }
    
    public void setHealth(int newHealth) {
        this.Health = newHealth;
    }

    /**
     * @return the posX
     */
    public int getPosX() {
        return posX;
    }

    /**
     * @param posX the posX to set
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * @return the posY
     */
    public int getPosY() {
        return posY;
    }

    /**
     * @param posY the posY to set
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getMatrixX() {
        return posX / 32;
    }

    public int getMatrixY() {
        return posY / 32;

    }

    public void setMatrixX(int x) {
        this.posX = (x * 32) + 10;
        pemain.setLocation(posX, posY);
        pemain.repaint();

    }

    public void setMatrixY(int y) {
        this.posY = (y * 32) + 10;
        pemain.setLocation(posX, posY);
        pemain.repaint();

    }
}
