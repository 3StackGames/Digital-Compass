var exports = module.exports = {};

//Everyone's first event is connection
exports.CONNECTION = 'connection';
//relayed from bridge to everyone on display or gamepad join
//relayed from backend to everyone on backend state update
exports.STATE_UPDATE = 'State Update';
//relayed from display to backend
exports.DISPLAY_ACTION_COMPLETE = 'Display Action Complete';
//relayed from gamepad to backend
exports.GAMEPAD_INPUT = 'Gamepad Input';


/* ===== BACKEND ===== */
exports.BACKEND_CONNECTED = 'Backend Connected';
exports.INITIALIZE_GAME = 'Initialize Game';


/* ===== DISPLAY ===== */
exports.DISPLAY_JOIN = 'Display Join';


/* ===== GAMEPAD ===== */
exports.GAMEPAD_JOIN = 'Gamepad Join';
exports.BEGIN_GAME = 'Begin Game';
