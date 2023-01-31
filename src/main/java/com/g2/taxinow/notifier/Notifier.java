package com.g2.taxinow.notifier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * A class which represents a UDP multicast notifier
 */
public class Notifier {

    /**
     * The port on which the notifier server will be opened
     */
    private static int port;

    /**
     * The address on which the notifier server will be opened
     */
    private static InetAddress address;

    /**
     * The MulticastSocket socket on which send notifications
     */
    private static MulticastSocket sock;

    /**
     * Initializes the notifier service on given address and port
     *
     * @param address the address
     * @param port the port
     * @throws IOException when an error initializing the socket occurs
     */
    public static void initialize(String address, int port) throws IOException {
        sock = new MulticastSocket();
        Notifier.port = port;
        Notifier.address = InetAddress.getByName(address);
    }


    /**
     * Notify all the clients which joins the multicast socket with the provided notification
     *
     * @param notification the notification to send
     * @throws IOException when an error sending the requests happens
     */
    public static void notifyRide(RideNotification notification) throws IOException {
        // creating bytes serialization stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(notification);
        // creating a buffer from the bytes of notification
        byte[] buf = baos.toByteArray();
        // creating and sending a new datagram packet
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        sock.send(packet);
        System.out.println("Notification sent!");
    }

}
