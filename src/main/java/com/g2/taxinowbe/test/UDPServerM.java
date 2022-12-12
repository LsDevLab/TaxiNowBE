package com.g2.taxinowbe.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class UDPServerM {

    private static int TRANSISSION_PORT = 7778;

    private static String[] file = {"Line1", "Line 2", "Line3"};

    public static void main(String[] args) throws IOException, InterruptedException {

        MulticastSocket sock = new MulticastSocket();
        InetAddress addr = InetAddress.getByName("234.5.6.7");

        System.out.println(sock.getNetworkInterface().getName());
        while (true) {
            System.out.println("SENDING");
            for (String line : file) {
                byte[] lineBytes = line.getBytes();
                DatagramPacket packet = new DatagramPacket(lineBytes, lineBytes.length, addr, TRANSISSION_PORT);
                sock.send(packet);
                Thread.sleep(500);
            }
        }
    }

}
