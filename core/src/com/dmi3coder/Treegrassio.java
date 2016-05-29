package com.dmi3coder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.dmi3coder.sprites.Blebby;
import com.dmi3coder.sprites.Bubble;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

public class Treegrassio extends ApplicationAdapter {
	private static final float UPDATE_TIME = 1/60f;
	boolean removable = true;
	float timer;
	public static final String TAG = "SocketIO";
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private Socket socket;
	private final float SCALE = 10;//WORLD_TO_STAGE_SCALE
	private OrthographicCamera camera, hudCamera;
	private SpriteBatch batch;


	Bubble player;
	Texture playerBubble;
	Texture friendlyBubble;
	Texture blebbyTexture;
	HashMap<String,Bubble> friendlyPlayers;
	HashMap<String,Blebby> blebbies;

	@Override
	public void create () {
		batch = new SpriteBatch();
		playerBubble = new Texture("bubble.png");
		blebbyTexture = new Texture("blebby.png");
		blebbies = new HashMap<String, Blebby>();
		friendlyBubble = playerBubble;
		friendlyPlayers = new HashMap<String, Bubble>();
		//Camera used for Box2D related images
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
		//Camera used for HUD
		hudCamera = new OrthographicCamera(WIDTH, HEIGHT);
		hudCamera.position.set(WIDTH/2, HEIGHT/2, 0);
		try {
			connectSocket();
			configSocketEvents();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Override
	public void render () {
		camera.update();
		hudCamera.update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		handleInput(Gdx.graphics.getDeltaTime());
		updateServer(Gdx.graphics.getDeltaTime());
		batch.getProjectionMatrix().set(camera.combined);
		batch.begin();
		if(player != null){
			player.draw(batch);
		}
		for(HashMap.Entry<String,Bubble> entry : friendlyPlayers.entrySet()){
			entry.getValue().draw(batch);
		}
		try {
			for (HashMap.Entry<String, Blebby> entry : blebbies.entrySet()) {
				Blebby blebby = entry.getValue();
				if (player.contains(blebby.getX(), blebby.getY())) {
					JSONObject jsonObject = new JSONObject();
						jsonObject.put("id", entry.getKey());
						blebbies.remove(entry.getKey());
					blebbies.remove(entry.getKey());
					socket.emit("eatBlebby", jsonObject);
					player.setSize(player.getSize() + 0.01f);

				} else
					blebby.draw(batch);
			}
		}
		catch (Exception e){

		}
		batch.end();
	}

	private void handleInput(float deltaTime) {
		if(player != null){
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
				player.setPosition(player.getX() + (-200* deltaTime), player.getY());
			}else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
				player.setPosition(player.getX() + (200* deltaTime), player.getY());
			}else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
				player.setPosition(player.getX(), player.getY() + (200 * deltaTime));
			}else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				player.setPosition(player.getX(), player.getY() + (-200 * deltaTime));
			}
			camera.position.x = player.getX() +player.getWidth()/2;
			camera.position.y = player.getY() +player.getHeight()/2;
			if(player.getSize()<1)
				camera.zoom = 1f;
			else
				camera.zoom = player.getSize()*0.75f;
		}
	}

	private void updateServer(float dt){
		timer += dt;
		if(timer >= UPDATE_TIME && player !=null && player.hasMoved()){
			JSONObject data = new JSONObject();
			try {
				data.put("x",player.getX());
				data.put("y",player.getY());
				socket.emit("playerMoved",data);
			}catch (JSONException e){
				Gdx.app.log(TAG,"error " + e);
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
					JSONObject data = (JSONObject)args[0];
					String playerId = data.getString("id");
					Gdx.app.log(TAG,"New Player connect : "+ playerId);
					friendlyPlayers.put(playerId,new Bubble(friendlyBubble));
				} catch (JSONException e) {
					Gdx.app.log(TAG,"My id: " + e.toString());
				}
			}
		}).on("playerDisconnected", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject)args[0];
				try {
					String id = data.getString("id");
					friendlyPlayers.remove(id);
				} catch (JSONException e) {
					Gdx.app.log(TAG,"My id: " + e.toString());
				}
			}
		}).on("playerMoved", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject)args[0];
				try {
					String playerId = data.getString("id");
					Double x = data.getDouble("x");
					Double y = data.getDouble("y");
					if(friendlyPlayers.get(playerId) != null){
						friendlyPlayers.get(playerId).setPosition(x.floatValue(),y.floatValue());
					}
				} catch (JSONException e) {
					Gdx.app.log(TAG,"My id: " + e.toString());
				}
			}
		}).on("getPlayers", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONArray objects = (JSONArray) args[0];
				try{
					for (int i = 0; i < objects.length(); i++) {
						Bubble coopPlayer = new Bubble(friendlyBubble);
						Vector2 position = new Vector2();
						position.x = ((Double)objects.getJSONObject(i).getDouble("x")).floatValue();
						position.y = ((Double)objects.getJSONObject(i).getDouble("y")).floatValue();
						coopPlayer.setPosition(position.x,position.y);
						friendlyPlayers.put(objects.getJSONObject(i).getString("id"),coopPlayer);
					}
				}catch (JSONException e){

				}
			}
		}).on("getBlebbies", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONArray blebbiesJson = (JSONArray)args[0];
				HashMap<String,Blebby> addingList = new HashMap<String, Blebby>(blebbiesJson.length());
				try{
					for (int i = 0; i < blebbiesJson.length(); i++) {
						Blebby blebby = new Blebby(blebbyTexture);
						Vector2 position = new Vector2();
						position.x = ((Double)blebbiesJson.getJSONObject(i).getDouble("x")).floatValue();
						position.y = ((Double)blebbiesJson.getJSONObject(i).getDouble("y")).floatValue();
						blebby.setPosition(position.x,position.y);
						addingList.put(blebbiesJson.getJSONObject(i).getString("id"),blebby);
						Gdx.app.log(TAG,blebbiesJson.getJSONObject(i).getString("id"));
					}
					blebbies.putAll(addingList);
				}catch (JSONException e){

				}
			}
		}).on("eatBlebby", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject blebbyJson = (JSONObject)args[0];
				Gdx.app.log(TAG,"removed");
				String id;
				try {
					id = blebbyJson.getString("id");
					for (HashMap.Entry<String,Blebby> entry : blebbies.entrySet()) {
						if(entry.getKey().equals(id)){
							blebbies.remove(entry.getKey());
							removable = true;
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}

}
