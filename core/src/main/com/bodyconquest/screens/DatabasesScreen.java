package main.com.bodyconquest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import main.com.bodyconquest.constants.Assets;
import main.com.bodyconquest.constants.GameType;
import main.com.bodyconquest.rendering.BodyConquest;

/** The type Databases screen. */
public abstract class DatabasesScreen extends AbstractGameScreen {

  /** The username. */
  protected String textUsername;

  /** The password. */
  protected String textPassword;

  /** The Texture for password. */
  protected Texture password;

  /** The Texture for Username. */
  protected Texture username;
  /** The Skin. */
  protected Skin skin;

  /** The Textfield for username. */
  protected TextField txfUsername;

  /** The Textfield for password. */
  protected TextField txfPassword;

  /** The Password image. */
  protected Image passwordImage;

  /** The Username image. */
  protected Image usernameImage;

  /** The Stage. */
  protected Stage stage;

  protected GameType gameType;

  /**
   * Instantiates a new Database screen.
   *
   * @param game the game
   */
  public DatabasesScreen(BodyConquest game, GameType gameType) {
    super(game);
    loadAssets();
    getAssets();
    this.gameType = gameType;
    passwordImage = new Image(password);
    usernameImage = new Image(username);
    skin = new Skin(Gdx.files.internal("uiskin.json"));
    // backBtn = new TextButton("Back",skin);
    txfUsername = new TextField("", skin);
    txfPassword = new TextField("", skin);
    txfPassword.setPasswordMode(true);
    txfPassword.setPasswordCharacter('*');
    textUsername = "";
    textPassword = "";
    stage = new Stage(viewport);
    Gdx.input.setInputProcessor(stage);
  }

  /** {@inheritDoc} */
  @Override
  public void render(float delta) {
    super.render(delta);
    game.batch.begin();
    stage.act(delta);
    stage.draw();
    game.batch.end();
  }

  /** {@inheritDoc} */
  @Override
  public void loadAssets() {
    super.loadAssets();
    manager.load(Assets.enterUsernameText, Texture.class);
    manager.load(Assets.enterPasswordText, Texture.class);
  }

  /** {@inheritDoc} */
  @Override
  public void getAssets() {
    super.getAssets();
    username = manager.get(Assets.enterUsernameText, Texture.class);
    password = manager.get(Assets.enterPasswordText, Texture.class);
  }

  /** Process registration. */
  public void processRegistration() {
    dispose();
  }

  /** {@inheritDoc} */
  @Override
  public void dispose() {
    super.dispose();
    stage.dispose();
  }

  /** Setting positions in the batch and stage. */
  public void settingPositions() {
    usernameImage.setPosition(
        BodyConquest.V_WIDTH / 2.0f - usernameImage.getWidth() / 2.0f, 360.0f);
    passwordImage.setPosition(
        BodyConquest.V_WIDTH / 2.0f - passwordImage.getWidth() / 2.0f, 220.0f);
    txfUsername.setPosition(BodyConquest.V_WIDTH / 2.0f - txfUsername.getWidth() / 2.0f, 310.0f);
    txfPassword.setPosition(BodyConquest.V_WIDTH / 2.0f - txfPassword.getWidth() / 2.0f, 170.0f);
  }

  /** Adding actors to Stage. */
  public void adding() {
    stage.addActor(passwordImage);
    stage.addActor(usernameImage);
    stage.addActor(txfUsername);
    stage.addActor(txfPassword);
  }

  /** Setting sizes in the batch and stage. */
  public void settingSizes() {
    txfUsername.setSize(300, 40);
    txfPassword.setSize(300, 40);
    passwordImage.setSize(passwordImage.getWidth(), passwordImage.getHeight());
    usernameImage.setSize(usernameImage.getWidth(), usernameImage.getHeight());
  }
}
