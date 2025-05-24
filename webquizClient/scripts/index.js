import WebSocket from "ws";
const port = 8080;
const server_url = `ws://localhost:${port}`;
const clientSocket = new WebSocket(server_url);
clientSocket.on('open', () => {
    console.log("Connected to server!");
    clientSocket.close();
});
clientSocket.on('close', () => {
    console.log("Closed connection!");
});
//# sourceMappingURL=index.js.map