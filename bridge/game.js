module.exports = function(gameCode) {
  this.gameCode = gameCode != null ? gameCode : "";
  this.players = [];
  this.displayConnected = true;
  //always store the last update, so you can provide it when a client reconnects
  this.lastUpdate = null;
  //store if DISPLAY_ACTION_COMPLETE has occured yet so a reconnecting client knows
  this.lastUpdateDisplayActionCompleted = false;
};
