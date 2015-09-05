package com.three_stack.digital_compass.backend;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import testGame.AInitialPhase;

public class PhaseManager {
	private static HashMap<String, GameState> states;

	public static void main(String[] args) throws URISyntaxException {
		final Socket socket = IO.socket("http://192.168.0.109:3000");
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

			// @Override
			public void call(Object... args) {
				socket.emit("Backend Connected");
			}

		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

			// @Override
			public void call(Object... args) {
				socket.emit("Backend Disconnected");
			}

		}).on("Initialize Game", new Emitter.Listener() {
			//Producer consumer problem - queue for incoming actions to be processed
			
			// @Override
			public void call(Object... args) {
				JSONObject details = new JSONObject(args[0]);
				try {
					System.out.println(args[0]);
					createGame(details);
					socket.emit("Game Created");
					System.out.println("success");
				} catch (JSONException e) {
					socket.emit("Invalid JSON",args);
					System.out.println("fail");
				}
			}

		});
		socket.connect();
	}

	private static void createGame(JSONObject details) throws JSONException {
		String gameCode = details.getString("gameCode");
		JSONArray players = new JSONArray(details.getJSONArray("players"));
		Player[] playerList = new Player[players.length()];
		for (int i = 0; i < players.length(); i++) {
			playerList[i] = new Player(players.getString(i));
		}
		GameState state = new AInitialPhase().begin(playerList);
		states.put(gameCode,state);
	}
}
