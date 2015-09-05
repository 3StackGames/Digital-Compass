var io = require('socket.io')(3000);

/*======================
  Classes
=======================*/
var Game = function() {
  this.gameCode =  "";
  this.players = [];//array of players
};

var Player = function(name) {
  if(name != null) {
    this.name = name;
  } else {
    this.name = "";
  }
}

/*======================
  Mock Classes
=======================*/
var mockGame = function() {
  var agame = new Game();
  agame.gameCode = 'ABCD';

  agame.players.push(new Player('Jason'));
  agame.players.push(new Player('Arjun'));
  agame.players.push(new Player('Richard'));
  agame.players.push(new Player('David'));
  return agame; 
};

/*======================
  Global Variables
=======================*/
var games = [];
var backendSocket = null;

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

var joinGame = function(socket, gameCode, role) {
  socket.join(gameCode + role);
  socket.join(gameCode);
}

io.on('connection', function(socket) {
  /*======================
    Setting up Room
  =======================*/
  /*===== BACKEND ========*/
  socket.on('Backend Connected', function() {
    backendSocket = socket;
    games = [];
    //temporary
    var agame = mockGame();
    socket.emit('Initialize Game', agame);
  });

  /*===== DISPLAY ========*/

  socket.on('Display Join', function() {
    var gameCode = generateGameCode();
    socket.emit('Game Code', gameCode);

    socket.gameCode = gameCode;
    joinGame(socket, gameCode, 'Display');


  });

  /*===== GAMEPAD ========*/
  socket.on('Gamepad Join', function(gameCode) {
    //TODO: add to game

    joinGame(socket, gameCode, 'Gamepad');
    socket.emit('Join Successful');
  });

  socket.on('Begin Game', function() {
    //TODO: intialize game with Backend

    socket.emit('Initialize Game', game);
  });


  /*======================
    Game Relay
  =======================*/

  io.on('Display Action Complete', function() {

  });

  io.on('Gamepad Input', function(data) {

  });

  io.on('State Update', function(data) {

  });
});