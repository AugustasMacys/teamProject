package com.cauldron.bodyconquest.entities.resources;

import com.cauldron.bodyconquest.game_logic.utils.Timer;
import com.cauldron.bodyconquest.networking.ServerSender;
import com.cauldron.bodyconquest.networking.utilities.MessageMaker;

public class Resources extends Thread {
  private final int MAX_RESOURCE = 100;
  private int lipids;
  private int sugars;
  private int proteins;
  private int regenerationLipids;
  private int regenerationSugars;
  private int regenerationProteins;
  private ServerSender serverSender;
  private String player;

  public Resources(ServerSender serverSender, String player) {
    this.serverSender = serverSender;
    this.player = player;
    lipids = MAX_RESOURCE;
    sugars = MAX_RESOURCE;
    proteins = MAX_RESOURCE;
    regenerationLipids = 7;
    regenerationSugars = 5;
    regenerationProteins = 6;

    String message = MessageMaker.resourceUpdate(lipids, sugars, proteins, player);
    serverSender.sendMessage(message);
  }

  public void run() {
    // wait for a second before increasing the resources
    while(true){
      boolean successfulTimer = Timer.startTimer(1000);
      while (!successfulTimer){
        successfulTimer = Timer.startTimer(1000);
      }

      if (lipids < MAX_RESOURCE) {
        if (lipids + regenerationLipids > MAX_RESOURCE) {
          lipids = MAX_RESOURCE;
        } else {
          lipids += regenerationLipids;
        }
      }

      if (sugars < MAX_RESOURCE) {
        if (sugars + regenerationSugars > MAX_RESOURCE) {
          sugars = MAX_RESOURCE;
        } else {
          sugars += regenerationSugars;
        }
      }

      if (proteins < MAX_RESOURCE) {
        if (proteins + regenerationProteins > MAX_RESOURCE) {
          proteins = MAX_RESOURCE;
        } else {
          proteins += regenerationProteins;
        }
      }
      String message = MessageMaker.resourceUpdate(lipids, sugars, proteins, player);
      serverSender.sendMessage(message);
    }
  }

  public boolean canAfford(int priceLipids, int priceSugars, int priceProteins) {
    if (lipids >= priceLipids && sugars >= priceSugars && proteins >= priceProteins) {
      return true;
    }
    return false;
  }

  public void buy(int priceLipids, int priceSugars, int priceProteins) {
    lipids -= priceLipids;
    sugars -= priceSugars;
    proteins -= priceProteins;
    //TO DO: send resource update to the client
  }

  public void increaseLipidRegeneration(int increase) {
    regenerationLipids += increase;
  }

  public void increaseSugarRegeneration(int increase) {
    regenerationSugars += increase;
  }

  public void increaseProteinRegeneration(int increase) {
    regenerationProteins += increase;
  }

  public int getLipids() {
    return lipids;
  }

  public int getProteins() {
    return proteins;
  }

  public int getSugars() {
    return sugars;
  }
}
