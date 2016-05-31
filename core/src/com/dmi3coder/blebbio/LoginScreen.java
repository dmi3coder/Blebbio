package com.dmi3coder.blebbio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.swing.*;

/**
 * Created by dmi3coder on 31/05/16;15:20.
 */
public class LoginScreen implements Screen {
    private final Treegrassio game;
    private SpriteBatch batch;
    private Stage stage;


    public LoginScreen(final Treegrassio game, final SpriteBatch batch){
        this.game = game;
        this.batch = batch;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        final TextField field = new TextField("", skin,"default");
        TextButton button = new TextButton("Click me",skin);
        Image image = new Image(new TextureRegion(new Texture("bubble.png")));
        Label gameLabel = new Label("Blebbio",skin);
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
                game.setScreen(new GameScreen(game,batch,name));
            }
        });
        Gdx.input.setInputProcessor(stage);
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
