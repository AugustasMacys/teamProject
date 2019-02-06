package com.cauldron.bodyconquest.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ClientSender extends Thread {
  public DatagramSocket socket;
  ClientReceiver clientReceiver;

  public ClientSender(ClientReceiver clientReceiver) throws SocketException {
    socket = new DatagramSocket();
    this.clientReceiver = clientReceiver;
  }

  public void sendMessage(String message){
    try {
      DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), clientReceiver.address, 3000);
      socket.send(packet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
