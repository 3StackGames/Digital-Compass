module.exports = function(gameCode) {
  this.gameCode = gameCode != null ? gameCode : "";
  this.players = [];
  this.displayConnected = true;
  this.begun = false;
};
