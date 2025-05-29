export class Network {
    connectionSocket;
    server_url;
    constructor(server_ip, port) {
        this.server_url = `ws://${server_ip}:${port}/ws`;
        this.connectionSocket = new WebSocket(this.server_url);
        //Event listerner for socket
        this.initEvents();
    }
    send(obj) {
        const json = JSON.stringify(obj);
        this.connectionSocket.send(json);
        console.log("Successfully send data to Server!");
    }
    initEvents() {
        this.connectionSocket.addEventListener("open", () => {
            alert("Connected to server!");
            console.log("Connected to server!");
        });
        this.connectionSocket.addEventListener("close", () => {
            alert("🔒 Connection closed");
            console.log("🔒 Connection closed");
        });
        this.connectionSocket.addEventListener("message", (message) => {
            console.log("Recevied message:%s", message.data);
        });
    }
    handelMessage() {
    }
    disconnect() {
        this.connectionSocket.close();
    }
    getServerUrl() {
        return this.server_url;
    }
    getConnectionSocket() {
        return this.connectionSocket;
    }
}
//# sourceMappingURL=network.js.map