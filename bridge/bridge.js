/*======================
  Dependencies
=======================*/
var io = require('socket.io')(3333);
var events = require('./events.js');
var codeGenerator = require('./code-generator.js');
var Game = require('./game.js');
var Player = require('./player.js');
var DisplayConnect = require('./display-connect.js');
var GamepadConnect = require('./gamepad-connect.js');
var Reason = require('./reason.js');
var logger = require('./logger.js');
logger.enabled = true;
/*======================
  Global Variables
=======================*/
var games = {};
var backendSocket = null;
/*======================
  Event Logic
=======================*/
io.on(events.CONNECTION, function(socket) {
  /*===== BACKEND ========*/
  socket.on(events.BACKEND_CONNECTED, function() {
    //setup
    backendSocket = socket;
    games = {};
    logger.log('Backend Connected');

    //relay
    socket.on(events.STATE_UPDATE, function(data) {
      var parsedData = JSON.parse(data);
      io.to(parsedData.gameCode).emit(events.STATE_UPDATE, parsedData);
      logger.log('State Update Relayed from Backend to Frontend', data);
    });
  });

  /*===== DISPLAY ========*/
  socket.on(events.DISPLAY_JOIN, function(data) {
    //setup
    //check if it's a reconnect or if a display is already connected
    var reconnect = false;
    var activeGameCode = false;
    //set gamecode
    var gameCode = null;
    if(data && data.gameCode) {
        gameCode = data.gameCode.toUpperCase();
    }
    
    if(data && gameCode) {
      activeGameCode = gameCode.length == 4 && games[gameCode];
      reconnect = activeGameCode && !games[gameCode].displayConnected;
    } else if(activeGameCode && !reconnect) {
      socket.emit(events.DISPLAY_JOIN_REJECTED, new Reason('A display is already connected.'));
      return;
    }

    if(reconnect) {
      games[gameCode].displayConnected = true;
      var displayReconnect = new DisplayConnect(gameCode);
      backendSocket.emit(events.DISPLAY_RECONNECTED, displayReconnect);
      logger.log('Display Reconnected. Backend notified.');
    } else {
      gameCode = codeGenerator.generate(games);
      games[gameCode] = new Game(gameCode);
      socket.emit(events.STATE_UPDATE, games[gameCode]);
      logger.log('Display Joined. State Update sent to Display.');
    }
    socket.gameCode = gameCode;
    socket.join(gameCode);

    //Relay
    socket.on(events.DISPLAY_ACTION_COMPLETE, function(data) {
      backendSocket.emit(events.DISPLAY_ACTION_COMPLETE, data);
      logger.log('Display Action Commplete Relayed to Backend');
    });

    //disconnect
    socket.on(events.DISCONNECT, function(){
      var game = games[socket.gameCode];
      if(!game) return;
      //mark display as disconnected
      game.displayConnected = false;
      //notify backend
      var displayDisconnect = new DisplayConnect(gameCode);
      backendSocket.emit(events.DISPLAY_DISCONNECTED, displayDisconnect);
    });
  });

  /*===== GAMEPAD ========*/
  socket.on(events.GAMEPAD_JOIN, function(data) {
    if(!data || !data.gameCode) {
      socket.emit(events.GAMEPAD_JOIN_REJECTED, new Reason('No data sent.'))
    }
    var gameCode = data.gameCode.toUpperCase();
    var displayName = data.displayName;
    var accountName = data.accountName;
    var player = getPlayer(gameCode, displayName);
    var reconnect = player && !player.connected;

    //setup
    socket.gameCode = gameCode;
    socket.displayName = displayName;
    if(reconnect) {
      player.connected = true;
      socket.join(gameCode);
      var playerReconnect = new GamepadConnect(gameCode, displayName);
      backendSocket.emit(events.GAMEPAD_RECONNECTED, playerReconnect);
      logger.log('Gamepad Reconnected. Backend notified.');
    } else if(games[gameCode] && games[gameCode].begun) {//trying to join after game started
      socket.emit(events.GAMEPAD_JOIN_REJECTED, new Reason('Game already began.'));
      return;
    } else {//new join
      socket.join(gameCode);
      games[gameCode].players.push(new Player(displayName, accountName));
      //let everyone know a player has joined
      io.to(gameCode).emit(events.STATE_UPDATE, games[gameCode]);
      logger.log('Gamepad Joined. Update sent to everyone.', games[gameCode]);
    }


    //Begin Game
    socket.on(events.BEGIN_GAME, function() {
      backendSocket.emit(events.INITIALIZE_GAME, games[socket.gameCode]);
      games[socket.gameCode].begun = true;
      logger.log('Begin Game Received. Sent Initialize Game to Backend.', games[socket.gameCode]);
    });

    //Relay
    socket.on(events.GAMEPAD_INPUT, function(data) {
      backendSocket.emit(events.GAMEPAD_INPUT, data);
      logger.log('Gamepad input relayed to backend', data);
    });

    //disconnect
    socket.on(events.DISCONNECT, function() {
      var player = getPlayer(socket.gameCode, socket.displayName);
      if(!player) return;
      //mark the player as disconnected
      player.connected = false;
      //notify backend
      var playerDisconnect = new GamepadConnect(socket.gameCode, player.displayName);
      backendSocket.emit(events.GAMEPAD_DISCONNECTED, playerDisconnect);
    });
  });
});
/*======================
  Helper Methods
=======================*/
var getPlayer = function(gameCode, displayName) {
  if(!games[gameCode]) return null;
  var players = games[gameCode].players;
  for(var i = 0; i < players.length; i++) {
    if(players[i].displayName == displayName) {
      return players[i];
    }
  }
  return null;
};
