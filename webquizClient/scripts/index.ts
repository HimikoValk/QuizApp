import WebSocket from "ws";

const port = 8080
const server_url:string = `ws://localhost:${port}`;
const clientSocket:WebSocket = new WebSocket(server_url);

//Tetsing webscoket!
clientSocket.on('open', ()=>{ 
    console.log("Connected to server!");
    clientSocket.close();    
});

clientSocket.on('close', () => {
    console.log("Closed connection!");
});

