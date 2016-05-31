var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];
var blebbies = [];

server.listen(8081, function(){
    for(var i = 0; i< 3000; i++){
        blebbies.push(new blebby(generateUUID(),Math.random()*10000 -5000,Math.random()*10000 -5000));

    }
    console.log("server running");

});


io.on('connection',function (socket) {
    console.log("player connected!");
    socket.emit('socketID',{ id : socket.id});
    socket.emit('getPlayers',players);
    socket.emit('getBlebbies',blebbies);
    socket.on('playerMoved',function (data) {
       data.id = socket.id;
        socket.broadcast.emit('playerMoved',data);
        for (var i = 0; i< players.length; i++){
            if(players[i].id == data.id){
                players[i].x = data.x;
                players[i].y = data.y;
            }
        }
    });
    socket.broadcast.emit('newPlayer',{id: socket.id});
    socket.on('disconnect',function () {
        console.log("Player disconnected");
        socket.broadcast.emit('playerDisconnected',{id : socket.id});
        for(var i = 0; i< players.length;i++){
            if(players[i].id == socket.id){
                players.splice(i,1);
            }
        }
    });
    socket.on('eatBlebby',function (data) {
        for(var i = 0;i<blebbies.length;i++){
        if(blebbies[i].id == data.id){
            blebbies.splice(i,1);
            for(var j = 0;j<players.length;j++){
                if(players[j].id == socket.id){
                    players[j].size += 0.005;
                }
            }
            socket.broadcast.emit('eatBlebby',{id: data.id,user_id : socket.id});
        }
    }
    });
    socket.on('eatPlayer',function (data) {
        for(var i = 0; i<players.length;i++){
            if(players[i].id == data.id){
                players.splice(i,1);
                socket.broadcast.emit('eatPlayer',{id: data.id, consumer_id : socket.id});
            }
            else if(players[i].id == socket.id){
                players[i].size += data.size;
            }
        }
    });
    players.push(new player(socket.id, 0.25 ,0,0));

});

function generateUUID(){
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
    });
    return uuid;
}

function blebby(id, x, y) {
    this.id = id;
    this.x = x;
    this.y = y;
}

function player(id, size, x, y) {
    this.id = id;
    this.size = size;
    this.x = x;
    this.y = y;
}