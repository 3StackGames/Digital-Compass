var exports = module.exports = {};

/* ===== EVERYONE ===== */
//Everyone's first event is connection
exports.CONNECTION = 'connection';
//Before Game: To everyone on a Display or Gamepad join
//During Game: Relayed from backend to everyone
exports.STATE_UPDATE = 'State Update';
//Everyone's last event is disconnect
exports.DISCONNECT = 'disconnect';

/* ===== BACKEND ONLY ===== */
exports.BACKEND_CONNECTED = 'Backend Connected';
exports.INITIALIZE_GAME = 'Initialize Game';
//payload: gameCode, displayName
exports.DISPLAY_DISCONNECTED = 'Display Disconnected';
exports.DISPLAY_RECONNECTED = 'Display Reconnected';
exports.GAMEPAD_DISCONNECTED = 'Gamepad Disconnected';
exports.GAMEPAD_RECONNECTED = 'Gamepad Reconnected';

/* ===== DISPLAY ONLY ===== */
exports.DISPLAY_JOIN = 'Display Join';
//payload: gameCode
exports.DISPLAY_ACTION_COMPLETE = 'Display Action Complete';

/* ===== GAMEPAD ONLY ===== */
exports.GAMEPAD_JOIN = 'Gamepad Join';
exports.BEGIN_GAME = 'Begin Game';
exports.GAMEPAD_INPUT = 'Gamepad Input';
