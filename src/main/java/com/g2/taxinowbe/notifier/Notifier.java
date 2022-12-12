package com.g2.taxinowbe.notifier;

import com.g2.taxinowbe.models.Ride;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Notifier {
    private static int port;
    private static InetAddress address;
    private static MulticastSocket sock;

    public static void initialize(String address, int port) throws IOException {
        sock = new MulticastSocket();
        Notifier.port = port;
        Notifier.address = InetAddress.getByName(address);
    }

    public static void notifyNewRide(int numOfPassengers) throws IOException {
        byte[] numOfPassengersBytes = new byte[] {(byte) numOfPassengers};
        DatagramPacket packet = new DatagramPacket(numOfPassengersBytes, 1, address, port);
        sock.send(packet);
    }

}
