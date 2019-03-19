package main.com.bodyconquest.game_logic;

import main.com.bodyconquest.constants.*;
import main.com.bodyconquest.gamestates.EncounterState;
import main.com.bodyconquest.networking.Server;
import main.com.bodyconquest.networking.utilities.MessageMaker;

import java.net.SocketException;

public class Game extends Thread {

  private boolean running;
  /** The number of time the game will refresh each second. */
  private final int FPS = 60;
  /** The target time to spend on each epoch of the game. */
  private final long targetTime = 1000 / FPS;

  private Server server;
  private EncounterState encounterState;

  private final GameType gameType;

  private Player playerBottom;
  private Player playerTop;
  private boolean encounter;

  /**
   * Constructor
   *
   * @param gameType The type of game that this server is running.
   * @throws SocketException
   */
  public Game(GameType gameType) throws SocketException {
    this.gameType = gameType;
    server = new Server();
    server.startServer(gameType);
  }

  private void init() {
    running = true;
  }

  @Override
  public void run() {

    init();

    long start;
    long elapsed;
    long wait;

    // Game loop
    while (running) {
      if (server.isGameEnded()) {
        break;
      }
      start = System.nanoTime();

      update();

      elapsed = System.nanoTime() - start;

      wait = targetTime - elapsed / 1000000;
      if (wait < 0) wait = 5;

      try {
        Thread.sleep(wait);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void update() {
    if (encounter && encounterState != null){
      encounterState.update();
    }
  }

  public Server getServer() {
    return server;
  }

  public void setPlayerBottom(Disease disease) {
    playerBottom = new Player(PlayerType.PLAYER_BOTTOM, disease);
  }

  public void setPlayerTop(Disease disease) {
    playerTop = new Player(PlayerType.PLAYER_TOP, disease);
  }

  public Player getPlayerBottom() {
    return playerBottom;
  }

  public Player getPlayerTop() {
    return playerTop;
  }

  public void startEncounterState(Organ organ) {
    encounterState = new EncounterState(this, organ);
    encounter = true;
    // gsm.setCurrentGameState(encounterState);
  }

  public void startEncounterLogic(EncounterState encounterState) {
    server.startEncounterLogic(encounterState);
  }

  public void startRaceSelectionState() {
    server.startRaceSelectionLogic(this);
  }

  public GameType getGameType() {
    return gameType;
  }

  public void closeEverything() {
    server.closeEverything();
  }

  public void startBodyState() {
    if (gameType == GameType.SINGLE_PLAYER){
      setPlayerTop(Disease.INFLUENZA);
//      if(getPlayerBottom().getDisease() != Disease.INFLUENZA)
//        setPlayerTop(Disease.INFLUENZA);
//      else if(getPlayerBottom().getDisease() != Disease.MEASLES)
//        setPlayerTop(Disease.MEASLES);
      server.getServerSender().sendMessage(MessageMaker.diseaseMessage(Disease.INFLUENZA, PlayerType.PLAYER_TOP));
    }

    server.startBodyLogic(); }

  public void endEncounter() {
    encounter = false;
    encounterState = null;
    startBodyState();
  }

}
