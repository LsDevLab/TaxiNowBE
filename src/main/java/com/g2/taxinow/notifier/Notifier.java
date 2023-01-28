package com.g2.taxinow.notifier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

    public static void notifyRide(RideNotification notification) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(notification);
        byte[] buf = baos.toByteArray();
        System.out.println(buf.length);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        sock.send(packet);
    }

}
