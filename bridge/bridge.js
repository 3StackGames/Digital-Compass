/*======================
  Dependencies
=======================*/
var io = require('socket.io')(3333);
var events = require('./events.js');
var codeGenerator = require('./code-generator.js');
var Game = require('./game.js');
var Player = require('./player.js');
var logger = require('./logger.js');
logger.logging = true;
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
    var reconnect = data != null && data.gameCode.length == 4 && games[data.gameCode] != null && !games[data.gameCode].displayConnected;
    var gameCode = null;
    if(reconnect) {
      gameCode = data.gameCode;
      games[gameCode].displayConnected = true;
      backendSocket.emit(events.DISPLAY_RECONNECTED);
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
    socket.on(events.DISPLAY_ACTION_COMPLETE, function() {
      backendSocket.emit(events.DISPLAY_ACTION_COMPLETE, socket.gameCode);
      logger.log('Display Action Commplete Relayed to Backend');
    });

    //disconnect
    socket.on(events.DISCONNECT, function(){
      var game = games[socket.gameCode];
      if(game == null) return;
      //mark display as disconnected
      game.displayConnected = false;
    });
  });

  /*===== GAMEPAD ========*/
  socket.on(events.GAMEPAD_JOIN, function(data) {
    var gameCode = data.gameCode;
    var displayName = data.name;
    var player = getPlayer(gameCode, displayName);
    var reconnect = player != null && !player.connected;
    //setup
    socket.join(gameCode);
    socket.gameCode = gameCode;
    socket.displayName = displayName;
    if(reconnect) {
      player.connected = true;
      backendSocket.emit(events.GAMEPAD_RECONNECTED, player);
      logger.log('Gamepad Reconnected. Backend notified.');
    } else {
      games[gameCode].players.push(new Player(data.name));
      //let everyone know a player has joined
      io.to(gameCode).emit(events.STATE_UPDATE, games[gameCode]);
      logger.log('Gamepad Joined. Update sent to everyone.', games[gameCode]);
    }

    //Begin Game
    socket.on(events.BEGIN_GAME, function() {
      backendSocket.emit(events.INITIALIZE_GAME, games[socket.gameCode]);
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
      if(player == null) return;
      //mark the player as disconnected
      player.connected = false;
    });
  });
});
/*======================
  Helper Methods
=======================*/
var getPlayer = function(gameCode, displayName) {
  if(games[gameCode] == null) return null;
  var players = games[gameCode].players;
  for(var i = 0; i < players.length; i++) {
    if(players[i].displayName == displayName) {
      return players[i];
    }
  }
  return null;
};
