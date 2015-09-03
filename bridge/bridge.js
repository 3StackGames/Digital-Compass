var io = require('socket.io')(3000);

io.on('connection', function(socket) {
	console.log('Connection!');
	socket.emit('boop', 'Hello! Love, Bridge');

	io.on('Display Action Complete', function() {

	});

	io.on('Gamepad Input', function(data) {

	});

	io.on('State Update', function(data) {
		
	});
});