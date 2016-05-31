package com.dmi3coder.blebbio;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.dmi3coder.blebbio.sprites.Blebby;
import com.dmi3coder.blebbio.sprites.Bubble;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Treegrassio extends Game {
	public static final float UPDATE_TIME = 1/24f;
	private SpriteBatch batch;


	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new LoginScreen(this,batch));
	}



	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
