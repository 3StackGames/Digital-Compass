var exports = module.exports = {};

//by default, enable logging
exports.logging = true;

exports.log = function (message, printObject) {
  if(exports.logging) {
    if(printObject == null) {
      console.log(message);
    } else {
      console.log(message);
      console.log('\n');
      console.log(printObject);
      console.log('\n');
    }
  }
};
