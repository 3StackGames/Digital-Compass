package com.three_stack.digital_compass.backend;

import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import testGame.AGameState;

public class PhaseManager {
	
    private Socket socket;
    private GameState initialState;
	private Thread threadManager;
	private Map<String, GameState> gameStates = new HashMap<String, GameState>();
	private ArrayBlockingQueue<JSONObject> actionsToProcess = new ArrayBlockingQueue<JSONObject>(100000); //account for full queue?

    private HashSet<String> gamesBeingProcessed = new HashSet<String>();
    private ArrayList<Thread> threads = new ArrayList<Thread>();
    private Lock threadLock = new ReentrantLock();
    private Condition threadSignal = threadLock.newCondition();

	public final String BACKEND_CONNECTED = "Backend Connected";
	public final String BACKEND_DISCONNECTED = "Backend Disconnected";
	public final String GAME_CREATED = "Game Created";
	public final String INVALID_JSON = "Invalid Json";
	public final String INITIALIZE_GAME = "Initialize Game";
	public final String GAMEPAD_INPUT = "Gamepad Input";
	public final String STATE_UPDATE = "State Update";

	public PhaseManager() { }
	
	public static void main(String args[]) {
		PhaseManager manager = new PhaseManager();
		if(args.length == 0)
			manager.connect("http://192.168.0.109:3333");
		else 
			manager.connect(args[0]);
	}
	
	public void initialize(String URI, GameState defaultState) {
		initialState = defaultState;
		
		if(URI == null) 
			connect("http://192.168.0.109:3333");
		else 
			connect(URI);
	}

	public void connect(String URI) {
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

		}).on(INITIALIZE_GAME, new Emitter.Listener() {
			
			// @Override
			public void call(Object... args) {
				try {
					JSONObject details = (JSONObject) args[0];
					createGame(details);
					socket.emit(GAME_CREATED);
				} catch (JSONException e) {
					socket.emit(INVALID_JSON, args);
				}
			}

		}).on(GAMEPAD_INPUT, new Emitter.Listener() {;
			
			public void call(Object... args) {
				JSONObject action = (JSONObject) args[0];
				actionsToProcess.add(action);
			}
		});
		socket.connect();

		threadManager = new Thread(new ThreadManager());
        threadManager.start();
	}
	
	public void shutDown() throws InterruptedException {
		threadManager.interrupt();
		threadManager.join();
		for(Thread t : threads) {
			t.join();
		}
		socket.disconnect();
	}

	private void createGame(JSONObject details) throws JSONException {
		GameState gameState = new Gson().fromJson(details.toString(), AGameState.class);

		//need to make this a deep copy in the future
		GameState newState = initialState;
		String gameCode = gameState.getGameCode();
		initialState.setPlayers(gameState.getPlayers());
		initialState.setGameCode(gameCode);
		
		gameStates.put(gameCode,newState);
		socket.emit(STATE_UPDATE, new Gson().toJson(initialState));
	}
	
	//todo: race conditions when same gamecode is called multiple times
	private class ProcessAction implements Runnable {	
		JSONObject details;
		
		public ProcessAction(JSONObject details) {
			this.details = details;
		}
		
		public void run() {
			String gameCode = null;
			try {
				gameCode = details.getString("gameCode");
				GameState state = gameStates.get(gameCode);
				if(state != null) {
					synchronized(gamesBeingProcessed) {
						if(gamesBeingProcessed.contains(gameCode)) {
							
						}
						gamesBeingProcessed.add(gameCode);
					}
					System.out.println("thread running");
					GameState newState = state.getCurrentPhase().processAction(details,state);
					socket.emit(STATE_UPDATE, new Gson().toJson(newState));
				}
			} catch (JSONException e) {
				System.out.println("json exception in thread");
			} finally {
				synchronized(threads) {
					threads.remove(this);
				}
				synchronized(gamesBeingProcessed) {
					gamesBeingProcessed.remove(gameCode);
				}
				threadLock.lock();
				threadSignal.signalAll();
				threadLock.unlock();
			}
		}
	}
	
	private class ThreadManager implements Runnable {
		
		private final int MAX_THREADS = 16;
		
		public void run() {
			while(true) {
				try {
					if(threads.size() < MAX_THREADS) {
						Thread newThread = new Thread(new ProcessAction(actionsToProcess.take()));
						synchronized(threads) {
							threads.add(newThread);
						}
						newThread.start();
					}
					else {
						threadLock.lock();
						System.out.println("max threads reached");
						threadSignal.await();
						threadLock.unlock();
					}
					
					Thread.sleep(100); //should take out once await is ready
				} catch (InterruptedException e) {
					System.out.println("ThreadManager interrupted");
				}
			}
		}
	}
}
