package main.com.bodyconquest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import main.com.bodyconquest.constants.Assets;
import main.com.bodyconquest.constants.GameType;
import main.com.bodyconquest.rendering.BodyConquest;

import java.io.IOException;

/** The type Menu screen. */
public class MenuScreen extends AbstractGameScreen implements Screen {

  private Texture title;
  private Texture playButtonMultiplayer;
  private Texture playButtonSinglePlayer;
  private Texture settingsButton;
  private Texture creditsButton;
  private Texture exitButton;
  private Rectangle multiplayerBounds;
  private Rectangle singleplayerBounds;
  private Rectangle settingsBounds;
  private Rectangle creditsBounds;
  private Rectangle exitBounds;

  /** The constant timeOfServer which shows how long the server is running. */
  public static long timeOfServer;

  private String username;

  /**
   * Instantiates a new Menu screen.
   *
   * @param game the game
   * @param username the username
   */
  public MenuScreen(BodyConquest game, String username) {
    super(game);
    loadAssets();
    getAssets();
    setRectangles();
    this.username = username;
    System.out.println(BodyConquest.scaleRatioHeight);
  }

  /** {@inheritDoc} */
  @Override
  public void show() {}

  /** {@inheritDoc} */
  @Override
  public void render(float delta) {

    super.render(delta);
    game.batch.begin();

    game.batch.draw(background, 0, 0, BodyConquest.V_WIDTH, BodyConquest.V_HEIGHT);
    game.batch.draw(
        title,
        BodyConquest.V_WIDTH / 2 - title.getWidth() / 2,
        450 * BodyConquest.scaleRatioHeight);
    game.batch.draw(
        playButtonSinglePlayer,
        BodyConquest.V_WIDTH / 2 - playButtonSinglePlayer.getWidth() / 2,
        300 * BodyConquest.scaleRatioHeight);
    game.batch.draw(
        playButtonMultiplayer,
        BodyConquest.V_WIDTH / 2 - playButtonMultiplayer.getWidth() / 2,
        240 * BodyConquest.scaleRatioHeight);
    game.batch.draw(
        settingsButton,
        BodyConquest.V_WIDTH / 2 - settingsButton.getWidth() / 2,
        180 * BodyConquest.scaleRatioHeight);
    game.batch.draw(
        creditsButton,
        BodyConquest.V_WIDTH / 2 - creditsButton.getWidth() / 2,
        120 * BodyConquest.scaleRatioHeight);
    game.batch.draw(
        exitButton,
        BodyConquest.V_WIDTH / 2 - exitButton.getWidth() / 2,
        60 * BodyConquest.scaleRatioHeight);

    checkPressed();

    game.batch.end();
  }

  @Override
  public void checkPressed() {

    super.checkPressed();

    if (Gdx.input.justTouched()) {
      if (multiplayerBounds.contains(tmp.x, tmp.y)) {
        playButtonSound();
        timeOfServer = System.currentTimeMillis();
        System.out.println("Multiplayer Is touched");
        dispose();
        game.setScreen(new HostScreen(game, username));
      }

      if (singleplayerBounds.contains(tmp.x, tmp.y)) {
        playButtonSound();
        System.out.println("Singleplayer Is touched");
        try {
          timeOfServer = System.currentTimeMillis();
          game.setScreen(new RaceSelection(game, GameType.SINGLE_PLAYER, username));
          dispose();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (settingsBounds.contains(tmp.x, tmp.y)) {
        playButtonSound();
        System.out.println("Settings Is touched");
        dispose();
        game.setScreen(new SettingsScreen(game, username));
      }
      if (creditsBounds.contains(tmp.x, tmp.y)) {
        playButtonSound();
        System.out.println("Credits Is touched");
        dispose();
        game.setScreen(new CreditsScreen(game, username));
      }

      if (exitBounds.contains(tmp.x, tmp.y)) {
        playButtonSound();
        dispose();
        Gdx.app.exit();
        System.exit(0);
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public void loadAssets() {
    super.loadAssets();
    manager.load(Assets.menuTitle, Texture.class);
    manager.load(Assets.multiplayerButton, Texture.class);
    manager.load(Assets.singleplayerButton, Texture.class);
    manager.load(Assets.settingsButton, Texture.class);
    manager.load(Assets.creditsButton, Texture.class);
    manager.load(Assets.exitButton, Texture.class);
    manager.finishLoading();
  }

  /** {@inheritDoc} */
  @Override
  public void getAssets() {
    super.getAssets();
    title = manager.get(Assets.menuTitle, Texture.class);
    playButtonMultiplayer = manager.get(Assets.multiplayerButton, Texture.class);
    playButtonSinglePlayer = manager.get(Assets.singleplayerButton, Texture.class);
    settingsButton = manager.get(Assets.settingsButton, Texture.class);
    creditsButton = manager.get(Assets.creditsButton, Texture.class);
    exitButton = manager.get(Assets.exitButton, Texture.class);
  }

  /** {@inheritDoc} */
  @Override
  public void setRectangles() {

    singleplayerBounds =
        new Rectangle(
            BodyConquest.V_WIDTH / 2 - playButtonSinglePlayer.getWidth() / 2,
            300 * BodyConquest.scaleRatioHeight,
            playButtonSinglePlayer.getWidth(),
            playButtonSinglePlayer.getHeight());

    multiplayerBounds =
        new Rectangle(
            BodyConquest.V_WIDTH / 2 - playButtonMultiplayer.getWidth() / 2,
            240 * BodyConquest.scaleRatioHeight,
            playButtonMultiplayer.getWidth(),
            playButtonMultiplayer.getHeight());

    settingsBounds =
        new Rectangle(
            BodyConquest.V_WIDTH / 2 - settingsButton.getWidth() / 2,
            180 * BodyConquest.scaleRatioHeight,
            settingsButton.getWidth(),
            settingsButton.getHeight());
    creditsBounds =
        new Rectangle(
            BodyConquest.V_WIDTH / 2 - creditsButton.getWidth() / 2,
            120 * BodyConquest.scaleRatioHeight,
            creditsButton.getWidth(),
            creditsButton.getHeight());
    exitBounds =
        new Rectangle(
            BodyConquest.V_WIDTH / 2 - exitButton.getWidth() / 2,
            60 * BodyConquest.scaleRatioHeight,
            exitButton.getWidth(),
            exitButton.getHeight());
  }
}
