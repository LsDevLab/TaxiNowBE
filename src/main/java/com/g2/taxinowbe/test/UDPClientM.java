package com.g2.taxinowbe.test;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;

public class UDPClientM {

    private static int SERVER_PORT = 7777;
    private static int CLIENT_PORT = 7778;

    public static void main(String[] args) throws IOException {

       Enumeration<NetworkInterface> ns= NetworkInterface.getNetworkInterfaces();
       while (ns.hasMoreElements())
           System.out.println(ns.nextElement().getName());

        InetAddress mcastaddr = InetAddress.getByName("230.0.0.0");
        InetSocketAddress group = new InetSocketAddress(mcastaddr, 6789);
        NetworkInterface netIf = NetworkInterface.getByName("0.0.0.0");
        MulticastSocket s = new MulticastSocket(7778);

        s.joinGroup(group, netIf);

        while (true) {
            byte[] buf = new byte[1000];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            s.receive(recv);
            // OK, I'm done talking - leave the group...
           // s.leaveGroup(group, netIf);

            System.out.println("Ricevuti: " + recv.getLength() + " bytes");
        }
    }

}
