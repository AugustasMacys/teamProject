package com.cauldron.bodyconquest.networking;

import com.cauldron.bodyconquest.game_logic.Communicator;

import java.io.IOException;

/**
 * The Client thread
 */
public class Client {
  public ClientReceiver clientReceiver;
  public ClientSender clientSender;
  private ClientLogic clientLogic;

  /**
   * Starts the receiver and sender threads
   * @throws IOException
   */
  public void startClient(Communicator communicator) throws IOException {
    clientReceiver = new ClientReceiver();
    clientSender = new ClientSender(clientReceiver);
    clientLogic = new ClientLogic(clientReceiver, communicator);

    clientReceiver.start();
    clientSender.start();
    clientLogic.start();

    clientSender.sendMessage("connected");
    clientSender.sendMessage("Hi btw");
  }

  //  public static void main(String argv[]) throws Exception {
  //    ClientReceiver clientReceiver = new ClientReceiver();
  //    ClientSender clientSender = new ClientSender(clientReceiver);
  //
  //    clientReceiver.start();
  //    clientSender.start();
  //
  //    //    Thread.sleep(5000);
  //
  //    clientSender.sendMessage("connected");
  //    Thread.sleep(500);
  //    clientSender.sendMessage("This is a message sent just after the game has started");
  // String sentence;
  // String modifiedSentence;

  // BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
  // Socket clientSocket = new Socket(packet.getAddress(), 4446);
  // DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
  // DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());

  // sentence = "hello server\n";
  // outToServer.writeUTF(sentence);
  // outToServer.flush();
  // System.out.println("Sent hello to server.");
  // modifiedSentence = inFromServer.readUTF();
  // System.out.println("FROM SERVER: " + modifiedSentence);
  // clientSocket.close();
  //  }
}
