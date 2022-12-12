package com.g2.taxinowbe.notifier;

import com.g2.taxinowbe.models.Ride;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Notifier {
    private static int TRANSISSION_PORT = 7778;

    private static MulticastSocket sock;
    private static InetAddress addr;

    public static void initialize() throws IOException {
        sock = new MulticastSocket();
        addr = InetAddress.getByName("234.5.6.7");
    }

    public static void notifyNewRide(int numOfPassengers) throws IOException {
        byte[] numOfPassengersBytes = new byte[] {(byte) numOfPassengers};
        DatagramPacket packet = new DatagramPacket(numOfPassengersBytes, 1, addr, TRANSISSION_PORT);
        sock.send(packet);
    }

}
