package com.dmi3coder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dmi3coder.sprites.Bubble;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Treegrassio extends ApplicationAdapter {
	public static final String TAG = "SocketIO";
	SpriteBatch batch;
	private Socket socket;
	Bubble player;
	Texture playerBubble;
	Texture friendlyBubble;
	HashMap<String,Bubble> friendlyPlayers;

	@Override
	public void create () {
		batch = new SpriteBatch();
		playerBubble = new Texture("bubble.png");
		friendlyBubble = playerBubble;
		friendlyPlayers = new HashMap<String, Bubble>();
		try {
			connectSocket();
			configSocketEvents();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		handleInput(Gdx.graphics.getDeltaTime());

		batch.begin();
		if(player != null){
			player.draw(batch);
		}
		for(HashMap.Entry<String,Bubble> entry : friendlyPlayers.entrySet()){
			entry.getValue().draw(batch);
		}
		batch.end();
	}

	private void handleInput(float deltaTime) {
		if(player != null){
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
				player.setPosition(player.getX() + (-200* deltaTime), player.getY());
			}else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
				player.setPosition(player.getX() + (200* deltaTime), player.getY());
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		playerBubble.dispose();
		friendlyBubble.dispose();
	}

	private void connectSocket() throws Exception {
		socket = IO.socket("http://localhost:8081");
		socket.connect();
	}


	private void configSocketEvents() throws Exception{
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO","Connected");
				player = new Bubble(playerBubble);
			}
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				try {
					JSONObject data = (JSONObject)args.clone()[0];
					String id = data.getString("id");
					Gdx.app.log(TAG,"My id: " + id);
				} catch (JSONException e) {
					Gdx.app.log(TAG,"My id: " + e.toString());
				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				try {
					JSONObject data = (JSONObject)args.clone()[0];
					String id = data.getString("id");
					Gdx.app.log(TAG,"New Player connect : "+ id);
					friendlyPlayers.put(id,new Bubble(friendlyBubble));
				} catch (JSONException e) {
					Gdx.app.log(TAG,"My id: " + e.toString());
				}
			}
		});
	}
}
