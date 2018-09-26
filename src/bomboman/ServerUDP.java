/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bomboman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author fcomputer
 */
public abstract class ServerUDP {

    private ServerSocket serverTCP;
    private Boolean online = true;
    private Boolean UDPOnline = true;
    private int penghuni = 0;
    private String namaServer;
    private String isiRoom = "";
    private static String message = "";

    ServerUDP(String namaServer) {
        try {
            this.namaServer = namaServer;
            isiRoom = namaServer+" (Server):";
            penghuni++;
            final DatagramSocket server = new DatagramSocket();
            final InetAddress multicastAddress = InetAddress.getByName("224.0.0.2");
            final int multicastPort = 9693;

            MulticastSocket ms = new MulticastSocket(multicastPort);
            ms.joinGroup(multicastAddress);

            serverTCP = new ServerSocket(multicastPort);
            BomboMain bomb = new BomboMain(true, "127.0.0.1", namaServer) {
                @Override
                public void sudahSelesai() {
                    try {
                        penghuni = 0;
                        isiRoom = "";
                        UDPOnline = false;
                        message = "%%" + InetAddress.getLocalHost().getHostAddress();
                        serverTCP.close();
                        byte[] buffer = message.getBytes();
                        DatagramPacket data = new DatagramPacket(buffer, buffer.length, multicastAddress, multicastPort);
                        server.send(data);
                        serverExit();
                    } catch (IOException ex) {
                        Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            bomb.setVisible(true);

            Thread pancarUDP = new Thread(new Runnable() {
                @Override
                public void run() {
                    Thread terimaTCP = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (online) {
                                try {
                                    // Batasi client cuma 4 user
                                    if (penghuni < 4) {
                                        final Socket koneksi = serverTCP.accept();

                                        // Thread untuk menangkap informasi nama user setelah itu mati dan dilanjutkan dengan koneksi TCP
                                        // pada port 6666 pada class BomboMain.java
                                        Thread koneksiBaru = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                penghuni++;
                                                BufferedReader in = null;
                                                try {
                                                    in = new BufferedReader(new InputStreamReader(koneksi.getInputStream()));
                                                    PrintWriter out = new PrintWriter(koneksi.getOutputStream());
                                                    String input = "";
                                                    input = in.readLine();
                                                    isiRoom += input + ":";
                                                    koneksi.close();
                                                } catch (IOException ex) {
                                                } finally {
                                                    try {
                                                        in.close();
                                                    } catch (IOException ex) {
                                                    }
                                                }
                                            }
                                        });
                                        koneksiBaru.start();
                                    } else {
                                        online = false;
                                    }
                                } catch (IOException ex) {
                                }

                            }
                            
                            // reset value
                            penghuni = 0;
                            isiRoom = "";
                            UDPOnline = false;
                        }
                    });
                    terimaTCP.start();
                    
                    
                    DatagramPacket data;
                    while (UDPOnline) {
                        try {
                            message = InetAddress.getLocalHost().getHostAddress() + "-" + getNamaServer() + "-" + penghuni + "-" + isiRoom;
                            System.out.println(message);
                            byte[] buffer = message.getBytes();
                            data = new DatagramPacket(buffer, buffer.length, multicastAddress, multicastPort);
                            server.send(data);
                            Thread.sleep(1000);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            });
            pancarUDP.start();
        } catch (BindException be) {
            JOptionPane.showMessageDialog(null, "Failed to create new server, please close another active server.");
        } catch (SocketException ex) {
            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerUDP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getNamaServer() {
        return namaServer;
    }

    public abstract void serverExit();
}
