package com.three_stack.digital_compass.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import testGame.AGameState;
import testGame.AInitialPhase;

public class PhaseManager {
	private static HashMap<String, GameState> states = new HashMap<String, GameState>();

	public static String BACKEND_CONNECTED = "Backend Connected";
	public static String BACKEND_DISCONNECTED = "Backend Disconnected";
	public static String GAME_CREATED = "Game Created";
	public static String INVALID_JSON = "Invalid Json";

	public static void main(String[] args) throws URISyntaxException {
		final Socket socket = IO.socket("http://192.168.0.109:3000");
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

			// @Override
			public void call(Object... args) {
				socket.emit(BACKEND_CONNECTED);
			}

		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

			// @Override
			public void call(Object... args) {
				socket.emit(BACKEND_DISCONNECTED);
			}

		}).on("Initialize Game", new Emitter.Listener() {
			//Producer consumer problem - queue for incoming actions to be processed
			
			// @Override
			public void call(Object... args) {
				try {
					JSONObject details = (JSONObject) args[0];
					createGame(details);
					socket.emit(GAME_CREATED);
					System.out.println("success");
				} catch (JSONException e) {
					socket.emit(INVALID_JSON, args);
					System.out.println("fail");
				}
			}

		});
		socket.connect();
	}

	private static void createGame(JSONObject details) throws JSONException {
		// TODO: @HyunbinTodo: taking toString() of JSONObject seems inefficient
		AGameState gameState = new Gson().fromJson(details.toString(), AGameState.class);
		String gameCode = gameState.getGameCode();
		List<Player> players = gameState.getPlayers();
		GameState state = new AInitialPhase().begin(players);
		states.put(gameCode,state);
	}
}
