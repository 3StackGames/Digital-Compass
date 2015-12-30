module.exports = function(displayName, accountName) {
  this.displayName = displayName || "";
  this.accountName = accountName || "";
  this.connected = true;
}
