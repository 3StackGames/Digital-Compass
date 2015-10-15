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
  Helper Functions
=======================*/
var joinGame = function(socket, gameCode, name) {
  socket.join(gameCode);
  //add to game
  logger.log(games);
  games[gameCode].players.push(new Player(name));
}

var createGame = function(socket, gameCode) {
  socket.join(gameCode);
  games[gameCode] = new Game(gameCode);
}

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
  socket.on(events.DISPLAY_JOIN, function() {
    //setup
    var gameCode = codeGenerator.generate(games);
    socket.gameCode = gameCode;
    createGame(socket, gameCode);
    socket.emit(events.STATE_UPDATE, games[gameCode]);
    logger.log('Display Joined. State Update sent to Display.');

    //Relay
    socket.on(events.DISPLAY_ACTION_COMPLETE, function() {
      io.to(socket.gameCode).emit(events.DISPLAY_ACTION_COMPLETE);
      logger.log('Display Action Commplete Received and Relayed to everyone');
    });
  });

  /*===== GAMEPAD ========*/
  socket.on(events.GAMEPAD_JOIN, function(data) {
    var gameCode = data.gameCode;
    //setup
    joinGame(socket, gameCode, data.name);
    socket.gameCode = gameCode;
    //let everyone know a new player has joined
    io.to(gameCode).emit(events.STATE_UPDATE, games[gameCode]);
    logger.log('Gamepad Joined. Update sent to everyone.', games[gameCode]);

    //Begin Game
    socket.on(events.BEGIN_GAME, function() {
      backendSocket.emit(events.INITIALIZE_GAME, games[socket.gameCode]);
      logger.log('Begin Game Received. Sent Initialize Game to Backend.', games[socket.gameCode]);
    });

    //Relay
    socket.on(events.GAMEPAD_INPUT, function(data) {
      backendSocket.emit(events.GAMEPAD_INPUT, data);
      logger.log('Gamepad input received and relayed to backend', data);
    });
  });
});
