package com.dmi3coder.blebbio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class LoginScreen implements Screen {
    private final Treegrassio game;
    private String alertText;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;

    private TextButton button;
    private Image image;
    private Label gameLabel, alertLabel;
    private TextField field;


    public LoginScreen(final Treegrassio game, final SpriteBatch batch){
        this.game = game;
        this.batch = batch;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        field = new TextField("", skin,"default");
        button = new TextButton("Click me",skin);
        image = new Image(new TextureRegion(new Texture("bubble.png")));
        gameLabel = new Label("Blebbio",skin);
        field.setWidth(200f);
        field.setHeight(20f);
        field.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 - 10f);
        button.setPosition(field.getX()+field.getWidth()-button.getWidth(),field.getY() - 40f);
        gameLabel.setPosition(field.getX()+field.getWidth()/2-gameLabel.getWidth()/2,field.getY()+field.getHeight() + 20f);
        image.setPosition(gameLabel.getX()+gameLabel.getWidth()/2 - image.getWidth()/2,gameLabel.getY()+30);
        stage.addActor(field);
        stage.addActor(button);
        stage.addActor(gameLabel);
        stage.addActor(image);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String name = field.getText();
                button.setLayoutEnabled(false);
                button.remove();
                image.remove();
                gameLabel.remove();
                field.remove();
                game.setScreen(new GameScreen(game,batch,name));
            }
        });
        Gdx.input.setInputProcessor(stage);
    }

    public LoginScreen(final Treegrassio game, final SpriteBatch batch,String alertText,Color alertColor){
        this(game,batch);
        this.alertText = alertText;
        alertLabel = new Label(alertText,skin);
        alertLabel.setColor(alertColor);
        alertLabel.setPosition(Gdx.graphics.getWidth()/2-alertLabel.getWidth()/2,0);
        stage.addActor(alertLabel);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        batch.begin();
        stage.draw();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }

    @Override
    public void show() {

    }

}
