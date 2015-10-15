module.exports = function(gameCode) {
  this.gameCode = gameCode != null ? gameCode : "";
  this.players = [];
  this.displayConnected = true;
  //always store the last update, so you can provide it when a client reconnects
  this.lastUpdate = null;
};
