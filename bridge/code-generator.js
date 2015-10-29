var exports = module.exports = {};

exports.generate = function (existingGames) {
  var uniqueCode = false;
  var newGameCode;
  while(!uniqueCode) {
    newGameCode = exports.genRandLetter()+exports.genRandLetter()+exports.genRandLetter()+exports.genRandLetter();
    if(!exports.isExistingGameCode(newGameCode, existingGames)) uniqueCode = true;
  }
  return newGameCode;
};

exports.isExistingGameCode = function(gameCode, existingGames) {
  if(typeof existingGames[gameCode] === 'undefined') return false;
  else return true;
};

exports.genRandLetter = function () {
  var randInt = Math.floor(Math.random()*26);
  return String.fromCharCode(65+randInt);
};
