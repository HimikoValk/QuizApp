export class Network {
    connectionSocket;
    server_url;
    constructor(server_ip, port) {
        this.server_url = `ws://${server_ip}:${port}/ws`;
        this.connectionSocket = new WebSocket(this.server_url);
        this.connectionSocket.addEventListener("open", () => {
            alert("Connected to server!");
            console.log("Connected to server!");
        });
        this.connectionSocket.addEventListener("close", () => {
            alert("🔒 Connection closed");
            console.log("🔒 Connection closed");
        });
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