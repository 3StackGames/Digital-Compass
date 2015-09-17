var io = require('socket.io')(3333);
var logging = false;

/*======================
  Classes
=======================*/
var Game = function(gameCode) {
  this.gameCode = gameCode != null ? gameCode : "";
  this.players = [];//array of players
};

var Player = function(name) {
  this.name = name != null ? name : "";
}

/*======================
  Global Variables
=======================*/
var games = {};
var backendSocket = null;

/*======================
  Events
=======================*/
/* ===== GENERAL ===== */
//Everyone's first event is connection
var CONNECTION = 'connection';
//relayed from backend to everyone
var STATE_UPDATE = 'State Update';
//relayed from display to backend
var DISPLAY_ACTION_COMPLETE = 'Display Action Complete';
//relayed from gamepad to backend
var GAMEPAD_INPUT = 'Gamepad Input';
/* ===== BACKEND ===== */
var BACKEND_CONNECTED = 'Backend Connected';
var INITIALIZE_GAME = 'Initialize Game';
/* ===== DISPLAY ===== */
var DISPLAY_JOIN = 'Display Join';
var GAME_CODE = 'Game Code';
/* ===== GAMEPAD ===== */
var GAMEPAD_JOIN = 'Gamepad Join';
var JOIN_SUCCESSFUL = 'Join Successful';
var BEGIN_GAME = 'Begin Game';
/*======================
  Helper Functions
=======================*/
var generateGameCode = function () {
  var uniqueCode = false;
  while(!uniqueCode) {
    var newGameCode = genRandLetter()+genRandLetter()+genRandLetter()+genRandLetter();
    if(!isExistingGameCode(newGameCode)) uniqueCode = true;
  }
  return newGameCode;
};

var isExistingGameCode = function(gameCode) {
  if(typeof games[gameCode] === 'undefined') return false;
  else return true;
}

var genRandLetter = function () {
  var randInt = Math.floor(Math.random()*26);
  return String.fromCharCode(65+randInt);
};

var joinGame = function(socket, gameCode, name) {
  if(logging) console.log('adding gamepad to room: ' + gameCode);
  socket.join(gameCode);
  //add to game
  console.log('-------------------');
  console.log(games);
  games[gameCode].players.push(new Player(name));
}

var createGame = function(socket, gameCode) {
  socket.join(gameCode);
  games[gameCode] = new Game(gameCode);
}

io.on(CONNECTION, function(socket) {
  // if(logging) console.log('somebody connected');
  /*======================
    Setting up Room
  =======================*/
  /*===== BACKEND ========*/
  socket.on(BACKEND_CONNECTED, function() {
    if(logging) console.log('Backend Connected');
    //setup
    backendSocket = socket;
    games = {};
    //end setup

    //relay
    socket.on(STATE_UPDATE, function(data) {
      if(logging) console.log('State Update Received from Backend');
      var parsedData = JSON.parse(data);
      io.to(parsedData.gameCode).emit(STATE_UPDATE, parsedData);
      if(logging) console.log('State Update Relayed to Frontend');
    });
  });

  /*===== DISPLAY ========*/
  socket.on(DISPLAY_JOIN, function() {
    if(logging) console.log('Display Joined');
    //setup
    var gameCode = generateGameCode();
    socket.emit(GAME_CODE, gameCode);
    if(logging) console.log('Game Code Sent to Display');
    socket.gameCode = gameCode;
    createGame(socket, gameCode);
    //end setup

    //Relay
    socket.on(DISPLAY_ACTION_COMPLETE, function() {
      if(logging) console.log('Display Action Complete Received');
      //relay to gamepads
      io.to(socket.gameCode).emit(DISPLAY_ACTION_COMPLETE);
      if(logging) console.log('Display Action Relayed to gamepads');
    });
  });

  /*===== GAMEPAD ========*/
  socket.on(GAMEPAD_JOIN, function(data) {
    if(logging) console.log('Gamepad joined');
    //setup
    joinGame(socket, data.gameCode, data.name);
    socket.gameCode = data.gameCode;
    socket.emit(JOIN_SUCCESSFUL);
    if(logging) console.log('sent Joined Successful to gamepad');
    //end setup

    //Begin Game
    socket.on(BEGIN_GAME, function() {
      if(logging) console.log('Begin Game Received. Sending Game to Backend: ');
      if(logging) console.log(games[socket.gameCode]);
      backendSocket.emit(INITIALIZE_GAME, games[socket.gameCode]);
      if(logging) console.log('Sent Initialize Game to backend');
    });

    //Relay
    socket.on(GAMEPAD_INPUT, function(data) {
      if(logging) console.log('Gamepad input received');
      backendSocket.emit(GAMEPAD_INPUT, data);
      if(logging) console.log('Gamepad input relayed to backend');
    });
  });
});