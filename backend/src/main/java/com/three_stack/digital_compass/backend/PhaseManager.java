package com.three_stack.digital_compass.backend;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class PhaseManager {

	public final int MAX_QUEUE_SIZE = 10000;

	private Socket socket;
	private BasicGameStateFactory defaultStateFactory;
	private Thread threadManager;
	private Map<String, BasicGameState> gameStates = new HashMap<String, BasicGameState>();
	private LinkedBlockingQueue<JSONObject> actionsToProcess = new LinkedBlockingQueue<JSONObject>(MAX_QUEUE_SIZE);
	private HashSet<String> gamesBeingProcessed = new HashSet<String>();
	private boolean running = false;

	private Vector<ProcessAction> threads = new Vector<ProcessAction>();
	private Lock threadLock = new ReentrantLock();
	private Condition threadSignal = threadLock.newCondition();

	public final String BACKEND_CONNECTED = "Backend Connected";
	public final String BACKEND_DISCONNECTED = "Backend Disconnected";
	public final String GAME_CREATED = "Game Created";
	public final String INVALID_JSON = "Invalid Json";
	public final String INITIALIZE_GAME = "Initialize Game";
	public final String GAMEPAD_INPUT = "Gamepad Input";
	public final String STATE_UPDATE = "State Update";
	public final String SHUTDOWN = "Shutdown";
	public final String END_GAME = "End Game";

	public static void main(String args[]) {
		PhaseManager manager = new PhaseManager();
		if (args.length == 0)
			manager.connect("http://192.168.0.109:3333");
		else
			manager.connect(args[0]);
	}

	public void initialize(String URI, BasicGameStateFactory defaultStateFactory) {
		this.defaultStateFactory = defaultStateFactory;

		if (URI == null)
			connect("http://192.168.0.109:3333");
		else
			connect(URI);
	}

	public void connect(String URI) {
		running = true;

		try {
			socket = IO.socket(URI);
		} catch (URISyntaxException e) {
			System.out.println(e);
		}
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

			// @Override
			public void call(Object... args) {
				System.out.println("Hi");
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

		}).on(GAMEPAD_INPUT, new Emitter.Listener() {

			public void call(Object... args) {
				JSONObject action = (JSONObject) args[0];
				try {
					actionsToProcess.put(action);
				} catch (InterruptedException e) {
					System.out.println(e);
				}
			}
		}).on(SHUTDOWN, new Emitter.Listener() {
			public void call(Object... args) {
				try {
					shutdown();
				} catch (InterruptedException e) {

				}
			}
		}).on(END_GAME, new Emitter.Listener() {
			public void call(Object... args) {
				try {
					endGame((String) args[0]);
				} catch (InterruptedException e) {

				}
			}
		});
		socket.connect();

		threadManager = new Thread(new ThreadManager());
		threadManager.start();
	}
	
	private void endGame(String gameCode) throws InterruptedException {
		for (ProcessAction t : threads) {
			if (t.gameCode != null && t.gameCode.equals(gameCode)) {
				t.join();
			}
		}
		gameStates.remove(gameCode);
	}

	public void shutdown() throws InterruptedException {
		if (running) {
			threadManager.interrupt();
			threadManager.join();
			for (ProcessAction t : threads) {
				t.join();
			}
			socket.disconnect();
		}
	}
	
	private void stateUpdate(JSONObject action, String gameCode, BasicGameState state) {
		BasicAction basicAction = null;
		BasicPhase currentPhase = state.getCurrentPhase();
		if(action != null) {
			basicAction = (BasicAction) new Gson().fromJson(action.toString(),currentPhase.getAction());
		}
		
		state = currentPhase.processAction(basicAction, state);
		if (state.getCurrentPhase() != currentPhase) {
			deleteActions(gameCode);
		}
		gameStates.put(gameCode, state);
		socket.emit(STATE_UPDATE, new Gson().toJson(state));	
	}
	
	private void deleteActions(String gameCode) {
		synchronized (actionsToProcess) {
			for (JSONObject action : actionsToProcess) {
				if (extractGameCode(action).equals(gameCode))
					actionsToProcess.remove(action);
			}
		}
	}
	
	private String extractGameCode(JSONObject action) {
		try {
			return action.getString("gameCode");
		} catch (JSONException e) {
			return null;
		}
	}

	private void createGame(JSONObject details) throws JSONException {
		try {
			BasicGameState initialState = defaultStateFactory.createState();
			BasicGameState jsonState = new Gson().fromJson(details.toString(),
					initialState.getClass());

			String gameCode = jsonState.getGameCode();
			initialState.setPlayers(jsonState.getPlayers());
			initialState.setGameCode(gameCode);
			
			stateUpdate(null, gameCode, initialState);
		} catch (NullPointerException e) {
			System.out.println(e);
		}
	}

	private class ProcessAction extends Thread {
		JSONObject details;
		String gameCode;

		public ProcessAction(JSONObject details) {
			this.details = details;
		}

		public void run() {
			try {
				gameCode = details.getString("gameCode");
				BasicGameState state = gameStates.get(gameCode);
				if (state != null) {
					System.out.println("thread running");						
					stateUpdate(details, gameCode, state);
					synchronized (gamesBeingProcessed) {
						gamesBeingProcessed.remove(gameCode);
					}
				}
			} catch (JSONException e) {
				System.out.println("json exception in thread");
			} finally {
				threads.remove(this);
				threadLock.lock();
				threadSignal.signalAll();
				threadLock.unlock();
			}
		}
	}

	private class ThreadManager implements Runnable {

		private final int MAX_THREADS = 16;

		public void run() {
			while (true) {
				try {
					if (threads.size() < MAX_THREADS) {
						ProcessAction newThread = null;
						synchronized(actionsToProcess) {						
							for (JSONObject action : actionsToProcess) {
								String gameCode = extractGameCode(action);
								if(!gamesBeingProcessed.contains(gameCode)) {
									synchronized(gamesBeingProcessed) {
										gamesBeingProcessed.add(gameCode);
									}
									actionsToProcess.remove(action);
									newThread = new ProcessAction(action);
									break;
								}
							}
						}
						if (newThread == null)
							newThread = new ProcessAction(actionsToProcess.take());
						threads.add(newThread);
						newThread.start();
					} else {
						threadLock.lock();
						System.out.println("max threads reached");
						threadSignal.await();
						threadLock.unlock();
					}
				} catch (InterruptedException e) {
					System.out.println("ThreadManager interrupted");
				}
			}
		}
	}
}
