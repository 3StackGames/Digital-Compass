var io = require('socket.io')(3333);
var events = require('./events.js');
var codeGenerator = require('./code-generator.js');
var Game = require('./game.js');
var Player = require('./player.js');
var logging = true;

/*======================
  Global Variables
=======================*/
var games = {};
var backendSocket = null;

/*======================
  Helper Functions
=======================*/
var joinGame = function(socket, gameCode, name) {
  if(logging) console.log('adding gamepad to room: ' + gameCode);
  socket.join(gameCode);
  //add to game
  if(logging) console.log(games);
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
  socket.on(BACKEND_CONNECTED, function() {
    if(logging) console.log('Backend Connected');
    //setup
    backendSocket = socket;
    games = {};

    //relay
    socket.on(events.STATE_UPDATE, function(data) {
      if(logging) {
        console.log('State Update Received from Backend: ');
        console.log(data);
      }
      var parsedData = JSON.parse(data);
      io.to(parsedData.gameCode).emit(events.STATE_UPDATE, parsedData);
      if(logging) console.log('State Update Relayed to Frontend');
    });
  });

  /*===== DISPLAY ========*/
  socket.on(events.DISPLAY_JOIN, function() {
    if(logging) console.log('Display Joined');
    //setup
    var gameCode = codeGenerator.generate(games);
    socket.gameCode = gameCode;
    createGame(socket, gameCode);
    socket.emit(events.STATE_UPDATE, games[gameCode]);
    if(logging) console.log('State Update Sent to Display');

    //Relay
    socket.on(events.DISPLAY_ACTION_COMPLETE, function() {
      if(logging) console.log('Display Action Complete Received');
      //relay to gamepads
      io.to(socket.gameCode).emit(events.DISPLAY_ACTION_COMPLETE);
      if(logging) console.log('Display Action Relayed to gamepads');
    });
  });

  /*===== GAMEPAD ========*/
  socket.on(events.GAMEPAD_JOIN, function(data) {
    if(logging) console.log('Gamepad joined');
    var gameCode = data.gameCode;
    //setup
    joinGame(socket, gameCode, data.name);
    socket.gameCode = gameCode;
    //let everyone know a new player has joined
    io.to(gameCode).emit(events.STATE_UPDATE, games[gameCode]);
    if(logging) console.log('Sent Player Joined to Everyone');

    //Begin Game
    socket.on(events.BEGIN_GAME, function() {
      if(logging) console.log('Begin Game Received. Sending Game to Backend: ');
      if(logging) console.log(games[socket.gameCode]);
      backendSocket.emit(events.INITIALIZE_GAME, games[socket.gameCode]);
      if(logging) console.log('Sent Initialize Game to backend');
    });

    //Relay
    socket.on(events.GAMEPAD_INPUT, function(data) {
      if(logging) console.log('Gamepad input received');
      backendSocket.emit(events.GAMEPAD_INPUT, data);
      if(logging) console.log('Gamepad input relayed to backend');
    });
  });
});
