var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

server.listen(8081, function(){
    console.log("server running");
});

io.on('connection',function (socket) {
    console.log("player connected!");
    socket.emit('socketID',{ id : socket.id});
    socket.broadcast.emit('newPlayer',{id: socket.id});
    socket.on('disconnect',function () {
        console.log("Player disconnected");
    });
});