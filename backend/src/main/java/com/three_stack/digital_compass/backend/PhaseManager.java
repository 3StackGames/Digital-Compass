package com.three_stack.digital_compass.backend;

import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONException;
import org.json.JSONObject;

import testGame.AGameState;
import testGame.AInitialPhase;

public class PhaseManager {
	private static PhaseManager instance = new PhaseManager();
	
	private final int MAX_THREADS = 16;
	private Map<String, GameState> states = new HashMap<String, GameState>();
	private List<Thread> threads = new ArrayList<Thread>(MAX_THREADS);
	private Queue<JSONObject> actionsToProcess = new ArrayBlockingQueue<JSONObject>(MAX_THREADS);
	private Thread threadManager;
	private Lock queueLock = new ReentrantLock();
    private Condition queueSignal = queueLock.newCondition();
    private boolean shutdown = false;
    private Socket socket;

	public final String BACKEND_CONNECTED = "Backend Connected";
	public final String BACKEND_DISCONNECTED = "Backend Disconnected";
	public final String GAME_CREATED = "Game Created";
	public final String INVALID_JSON = "Invalid Json";
	public final String INITIALIZE_EVENT = "Initialize Game";
	public final String ACTION_EVENT = "Game Action";

	private PhaseManager() { }
	
	public static void main(String args[]) {
		instance.main("http://192.168.0.109:3000");	
	}

	public void main(String URI) {
		try {
			socket = IO.socket(URI);
		}
		catch (URISyntaxException e) {
			System.out.println(e);
		}		
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

		}).on(INITIALIZE_EVENT, new Emitter.Listener() {
			
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

		}).on(ACTION_EVENT, new Emitter.Listener() {;
			
			public void call(Object... args) {
				queueLock.lock();
				JSONObject action = (JSONObject) args[0];
				actionsToProcess.add(action);
				queueSignal.signalAll();
				queueLock.unlock();
			}
		});
		socket.connect();

		threadManager = new Thread(instance.new ThreadManager());
        threadManager.start();
	}
	
	public void shutDown() throws InterruptedException {
		shutdown = true;
		threadManager.join();
		for(Thread t : threads) {
			t.join();
		}
		socket.disconnect();
	}

	private void createGame(JSONObject details) throws JSONException {
		AGameState gameState = new Gson().fromJson(details.toString(), AGameState.class);
		String gameCode = gameState.getGameCode();
		GameState state = new AInitialPhase().begin(gameState);
		states.put(gameCode,state);
	}
	
	private class ProcessAction implements Runnable {	
		JSONObject details;
		
		public ProcessAction(JSONObject details) {
			this.details = details;
		}
		
		public void run() {
			try {
				String gameCode = details.getString("gameCode");
				GameState state = states.get(gameCode);
				if(state != null) {
					System.out.println("thread running");
					//process action through gamestate's current phase
					//get gamestate
					//socket.emit(gamestate)
				}
			} catch (JSONException e) {
			}
		}
	}
	
	private class ThreadManager implements Runnable {
		
		public void run() {
			while(!shutdown) {
				try {
					queueLock.lock();
					while(actionsToProcess.isEmpty()) {
						queueSignal.await();
					}
					
					if(threads.size() < MAX_THREADS) {
						Thread newThread = new Thread(new ProcessAction(actionsToProcess.poll()));
						newThread.start();
						threads.add(newThread);
					}
					queueLock.unlock();
					
					//should change to notify
					for(Thread t : threads) {
						if (!t.isAlive()) {
							t.join();
							threads.remove(t);
						}
					}
				
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println("ThreadManager interrupted");
				}
			}
		}
	}
}
