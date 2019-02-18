package com.cauldron.bodyconquest.gamestates;

import com.cauldron.bodyconquest.constants.Constants;
import com.cauldron.bodyconquest.entities.BasicObject;
import com.cauldron.bodyconquest.entities.Map;
import com.cauldron.bodyconquest.entities.MapObject;
import com.cauldron.bodyconquest.entities.ViewObject;
import com.cauldron.bodyconquest.entities.projectiles.Projectile;
import com.cauldron.bodyconquest.entities.Troops.Bacteria;
import com.cauldron.bodyconquest.entities.Troops.Bases.BacteriaBase;
import com.cauldron.bodyconquest.entities.Troops.Bases.Base;
import com.cauldron.bodyconquest.entities.Troops.Flu;
import com.cauldron.bodyconquest.entities.Troops.Troop;
import com.cauldron.bodyconquest.constants.Constants.*;
import com.cauldron.bodyconquest.entities.Troops.Virus;
import com.cauldron.bodyconquest.game_logic.BasicTestAI;
import com.cauldron.bodyconquest.game_logic.Communicator;
import com.cauldron.bodyconquest.networking.Server;
import com.cauldron.bodyconquest.networking.ServerSender;
import com.cauldron.bodyconquest.networking.utilities.Serialization;
import com.cauldron.bodyconquest.constants.Constants;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/** The {@link GameState} where all of the encounter logic takes place. */
public class EncounterState extends GameState {

  /**
   * An enumeration for the different assignments {@link Troop}s can have to determine how Troops
   * move and who/what they attack.
   */

  /**
   * An enumeration for the different lanes {@link Troop}s can be assigned to, to determine how
   * those Troops move.
   */

  /** The map object that holds all information that needs to be known about the map. */
  private Map map;

  // Troop Arrays (Data type and usage is subject to future change)
  /** The list that stores all MapObject currently on the map. */
  private CopyOnWriteArrayList<MapObject> allMapObjects;

  /** The list that stores all the troops belonging to the top player. */
  private CopyOnWriteArrayList<Troop> troopsTop;
  /** The list that stores all the troops belonging to the bottom player. */
  private CopyOnWriteArrayList<Troop> troopsBottom;

  /** The list that stores all the projectiles that belong to the bottom player. */
  private CopyOnWriteArrayList<Projectile> projectilesBottom;
  /** The list that stores all the projectiles that belong to the top player. */
  private CopyOnWriteArrayList<Projectile> projectilesTop;

  /**
   * The communicator object which acts as a place holder for the Server (and possibly in future the
   * Client). This may remain for quick and easy implementation of single player without using a
   * Client/Server.
   */
  private Communicator comms;
  private ServerSender serverSender;

  private Base topBase;
  private Base bottomBase;

  /**
   * Constructor.
   *
   * @param comms The communication object to receive information from the Server/Model.
   */
  public EncounterState(Communicator comms, ServerSender serverSender) {
    this.comms = comms;
    this.serverSender = serverSender;
    map = new Map();

    allMapObjects = new CopyOnWriteArrayList<MapObject>();

    // Initialise unit arrays
    troopsBottom = new CopyOnWriteArrayList<Troop>();
    troopsTop = new CopyOnWriteArrayList<Troop>();

    // Create player bases
    bottomBase = new BacteriaBase(Lane.ALL, PlayerType.PLAYER_BOTTOM);
    bottomBase.setPosition(Constants.baseBottomX,Constants.baseBottomY);
    troopsBottom.add(bottomBase);
    allMapObjects.add(bottomBase);

    topBase = new BacteriaBase(Lane.ALL, PlayerType.PLAYER_TOP);
    topBase.setPosition(Constants.baseTopX,Constants.baseTopY);
    troopsTop.add(topBase);
    allMapObjects.add(topBase);

    projectilesBottom = new CopyOnWriteArrayList<Projectile>();
    projectilesTop = new CopyOnWriteArrayList<Projectile>();

    new BasicTestAI(this, PlayerType.PLAYER_TOP).start();
  }

  /**
   * Check attack interactions between the two troop lists. Initiates any resulting attack sequences
   * caused from troops being eligible to attack another troop.
   *
   * @param troopsP1 First list of troops.
   * @param troopsP2 Second list of troops.
   */
  private void checkAttack(
      CopyOnWriteArrayList<Troop> troopsP1, CopyOnWriteArrayList<Troop> troopsP2) {
    CopyOnWriteArrayList<Troop> deadTroops = new CopyOnWriteArrayList<Troop>();

    for (Troop troop : troopsP1) {
      if (troop.isDead()) {
        deadTroops.add(troop);
        continue;
      }
      troop.checkAttack(troopsP2);
    }

    for (Troop u : deadTroops) {
      troopsP1.remove(u);
      allMapObjects.remove(u);
    }
  }

  /**
   * Check collision interactions between the projectiles in the given projectile list with the
   * troops in the given troop list. Initiates any resulting attack sequences caused from troops
   * being hit.
   *
   * @param projectiles The list of projectiles to check interactions with.
   * @param enemies The list of troops to check interactions with.
   */
  private void checkProjectiles(
      CopyOnWriteArrayList<Projectile> projectiles, CopyOnWriteArrayList<Troop> enemies) {
    CopyOnWriteArrayList<Projectile> finishedProjectiles = new CopyOnWriteArrayList<Projectile>();
    for (Projectile proj : projectiles) {
      proj.checkHit(enemies);
      if (proj.getRemove()) finishedProjectiles.add(proj);
    }

    for (Projectile proj : finishedProjectiles) {
      projectiles.remove(proj);
      allMapObjects.remove(proj);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void update() {
    // Receive any input from clients
    // String command = comms.getNextComand();

    if(topBase.getHealth() > 0 && bottomBase.getHealth() > 0){




    for (MapObject mo : allMapObjects) mo.update();

    // Update All Units
    checkAttack(troopsTop, troopsBottom);
    checkAttack(troopsBottom, troopsTop);
    checkProjectiles(projectilesTop, troopsBottom);
    checkProjectiles(projectilesBottom, troopsTop);

    // Synchronize this
    // Change this so it only add new objects
    CopyOnWriteArrayList<BasicObject> sentObjects = new CopyOnWriteArrayList<BasicObject>();
    for (MapObject o : allMapObjects) {

      // Map Object does not have a unit type. How will determine which one is which?
//      switch (o.getUnitType()){
//        case FLU:
//          //TO DO add flu texture
//          break;
//        case VIRUS:
//          ////TO DO add virus texture
//        case BACTERIA:
//          ////TO DO add bacteria texture
//        case BACTERTIA_BASE:
//          viewObjects.add(new ViewObject(o,Constants.pathBaseImage));
//          break;
//        case VIRUS_BASE:
//          ////TO DO add Virus base Texture
//        case MONSTER_BASE:
//          ////TO DO add Monster base Texture
//        case BUCKET:
//          viewObjects.add(new ViewObject(o,Constants.pathBucket));
//          break;
//      }
      sentObjects.add(o.getBasicObject());
    }


    // TO DO: send this to the client
    String json = "";
    try {
      json = Serialization.serialize(sentObjects);
      serverSender.sendObjectUpdates(json);
//      comms.populateObjectList(sentObjectsDeserialized);
    } catch (IOException e) {
      e.printStackTrace();
    }
    }

//    else{
//      g
//    }
  }

  /**
   * Called by player AI's or players to spawn troops.
   *
   * @param unitType The unit/troop to be spawned.
   * @param lane The lane the unit/troop will be assigned to.
   * @param playerType The player the unit/troop will be assigned to.
   */
  public void spawnUnit(UnitType unitType, Lane lane, PlayerType playerType) {
    Troop troop = null;

    // Initialise troop type
    if (unitType.equals(UnitType.BACTERIA)) {
      troop = new Bacteria(playerType, lane);
    } else if (unitType.equals(UnitType.FLU)) {
      troop = new Flu(this, playerType, lane);
    } else if (unitType.equals(UnitType.VIRUS)) {
      troop = new Virus(playerType, lane);
    }

    // Return if invalid troop, lane or player type is used
    if (troop == null || lane == null || playerType == null) return;

    // Spawn units for bottom player
    if (playerType.equals(PlayerType.PLAYER_BOTTOM)) {
      if (lane == Lane.BOTTOM) {
        troop.setPosition(
            Constants.BP_BOT_LANE_SPAWN_X - (troop.getWidth() / 2.0),
            Constants.BP_BOT_LANE_SPAWN_Y - (troop.getHeight() / 2.0));
      } else if (lane == Lane.MIDDLE) {
        troop.setPosition(
            Constants.BP_MID_LANE_SPAWN_X - (troop.getWidth() / 2.0),
            Constants.BP_MID_LANE_SPAWN_Y - (troop.getHeight() / 2.0));
      } else if (lane == Lane.TOP) {
        troop.setPosition(
            Constants.BP_TOP_LANE_SPAWN_X - (troop.getWidth() / 2.0),
            Constants.BP_TOP_LANE_SPAWN_Y - (troop.getHeight() / 2.0));
      }
      troopsBottom.add(troop);
    }

    // Spawn units for top player
    if (playerType.equals(PlayerType.PLAYER_TOP)) {
      if (lane == Lane.BOTTOM) {
        troop.setPosition(
            Constants.TP_BOT_LANE_SPAWN_X - (troop.getWidth() / 2.0),
            Constants.TP_BOT_LANE_SPAWN_Y - (troop.getHeight() / 2.0));
      } else if (lane == Lane.MIDDLE) {
        troop.setPosition(
            Constants.TP_MID_LANE_SPAWN_X - (troop.getWidth() / 2.0),
            Constants.TP_MID_LANE_SPAWN_Y - (troop.getHeight() / 2.0));
      } else if (lane == Lane.TOP) {
        troop.setPosition(
            Constants.TP_TOP_LANE_SPAWN_X - (troop.getWidth() / 2.0),
            Constants.TP_TOP_LANE_SPAWN_Y - (troop.getHeight() / 2.0));
      }
      troopsTop.add(troop);
    }
    allMapObjects.add(troop);
  }

  /**
   * Called by ranged (projectile using) MapObjects to add their projectile to the list of
   * MapObjects.
   *
   * @param projectile The projectile to be added to the EncounterState/Map.
   * @param playerType The player that the projectile belongs to.
   */
  public void addProjectile(Projectile projectile, PlayerType playerType) {
    if (playerType == null || projectile == null) return;

    if (playerType == PlayerType.PLAYER_BOTTOM) {
      projectilesBottom.add(projectile);
    } else if (playerType == PlayerType.PLAYER_TOP) {
      projectilesTop.add(projectile);
    } else {
      return;
    }
    allMapObjects.add(projectile);
  }
}
