package com.three_stack.digital_compass.backend;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
	private Lock actionLock = new ReentrantLock();
	private Condition actionSignal = actionLock.newCondition();

	public final String BACKEND_CONNECTED = "Backend Connected";
	public final String BACKEND_DISCONNECTED = "Backend Disconnected";
	public final String GAME_CREATED = "Game Created";
	public final String INVALID_JSON = "Invalid Json";
	public final String INITIALIZE_GAME = "Initialize Game";
	public final String GAMEPAD_INPUT = "Gamepad Input";
	public final String DISPLAY_RECONNECTED = "Display Reconnected";
	public final String DISPLAY_DISCONNECTED = "Display Disconnected";
	public final String GAMEPAD_RECONNECTED = "Gamepad Reconnected";
	public final String GAMEPAD_DISCONNECTED = "Gamepad Disconnected";
	public final String DISPLAY_ACTION_COMPLETE = "Display Action Complete";
	public final String STATE_UPDATE = "State Update";
	public final String ERROR = "Error";
	public final String SHUTDOWN = "Shutdown";
	public final String END_GAME = "End Game";

	public void initialize(String URI, BasicGameStateFactory defaultStateFactory) {
		this.defaultStateFactory = defaultStateFactory;

		if (URI != null)
			connect(URI);
		else
			connect("http://localhost:8080");
	}

	public void connect(String URI) {
		running = true;

		try {
			socket = IO.socket(URI);
		} catch (URISyntaxException e) {
			e.printStackTrace();
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
					e.printStackTrace();
				}
			}

		}).on(GAMEPAD_INPUT, new Emitter.Listener() {

			public void call(Object... args) {
				JSONObject action = (JSONObject) args[0];
				try {
					actionsToProcess.put(action);
					actionLock.lock();
					actionSignal.signal();
					actionLock.unlock();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).on(DISPLAY_RECONNECTED, new Emitter.Listener() {
			public void call(Object... args) {
				try {
					displayConnection((JSONObject) args[0], true);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).on(DISPLAY_DISCONNECTED, new Emitter.Listener() {
			public void call(Object... args) {
				try {
					displayConnection((JSONObject) args[0], false);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).on(GAMEPAD_RECONNECTED, new Emitter.Listener() {
			public void call(Object... args) {
				try {
					playerConnection((JSONObject) args[0], true);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).on(GAMEPAD_DISCONNECTED, new Emitter.Listener() {
			public void call(Object... args) {
				try {
					playerConnection((JSONObject) args[0], false);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).on(DISPLAY_ACTION_COMPLETE, new Emitter.Listener() {
			public void call(Object... args) {
				try {
					displayComplete((JSONObject) args[0]);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).on(SHUTDOWN, new Emitter.Listener() {
			public void call(Object... args) {
				try {
					shutdown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).on(END_GAME, new Emitter.Listener() {
			public void call(Object... args) {
				try {
					endGame((String) args[0]);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		socket.connect();

		threadManager = new Thread(new ThreadManager());
		threadManager.start();
	}
	
	private void displayConnection(JSONObject display, boolean connected) throws JSONException {
		String gameCode = extractGameCode(display);
		BasicGameState state = gameStates.get(gameCode);
		
		synchronized(state) {
			if(connected) {
				state = state.getCurrentPhase().onDisplayReconnect(state);
			} else {
				state = state.getCurrentPhase().onDisplayDisconnect(state);					
			}
		}
		
		stateUpdate(null,gameCode,state);
	}
	
	private void playerConnection(JSONObject player, boolean connected) throws JSONException {
		String gameCode = extractGameCode(player);
		String name = player.getString("displayName");
		BasicGameState state = gameStates.get(gameCode);
		synchronized(state) {
			List<BasicPlayer> players = state.getPlayers();
			for(BasicPlayer p : players) {
				if(p.getDisplayName().equals(name)) {
					if(connected) {
						state = state.getCurrentPhase().onGamepadReconnect(state,p);
					} else {
						state = state.getCurrentPhase().onGamepadDisconnect(state,p);					
					}
					break;
				}
			}
		}
		
		stateUpdate(null,gameCode,state);
	}
	
	private void displayComplete(JSONObject display) throws JSONException {
		String gameCode = extractGameCode(display);
		BasicGameState state = gameStates.get(gameCode);
		synchronized(state) {
			state = state.getCurrentPhase().onDisplayActionComplete(state);
		}
		stateUpdate(null,gameCode,state);
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
		try {
			if(action != null) {
				BasicPhase currentPhase = state.getCurrentPhase();
				BasicAction basicAction = (BasicAction) new Gson().fromJson(action.toString(),currentPhase.getAction());
				
				state = currentPhase.processAction(basicAction, state);
				if (state.getCurrentPhase() != currentPhase) {
					deleteActions(gameCode);
					state.setDisplayComplete(false);
				}
			}
			
			gameStates.put(gameCode, state);
			socket.emit(STATE_UPDATE, new Gson().toJson(state));
		} catch (InvalidInputException e) {
			try {
				JSONObject error = new JSONObject();
				error.append("gameCode", gameCode);
				error.append("player", action.get("player"));
				error.append("code", e.getCode());
				error.append("message", e.getMessage());
				socket.emit(ERROR, error.toString());
			} catch (JSONException je) {
				je.printStackTrace();
			}
		}
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
			e.printStackTrace();
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
			e.printStackTrace();
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
					stateUpdate(details, gameCode, state);
					synchronized (gamesBeingProcessed) {
						gamesBeingProcessed.remove(gameCode);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
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
						if (newThread == null) {
							if (gamesBeingProcessed.isEmpty()) {
								newThread = new ProcessAction(actionsToProcess.take());
								threads.add(newThread);
								newThread.start();
							} else {
								actionLock.lock();
								actionSignal.await();
								actionLock.unlock();
							}
						}
					} else {
						threadLock.lock();
						System.out.println("max threads reached");
						threadSignal.await();
						threadLock.unlock();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
