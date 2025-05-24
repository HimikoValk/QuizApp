import WebScoket from "ws";
export class Network {
    connectionSocket;
    server_ip;
    port;
    server_url;
    constructor(server_ip, port) {
        this.server_ip = server_ip;
        this.port = port;
        const tmp_url = `ws://${this.server_ip}:${this.port}`;
        this.server_url = tmp_url;
        this.connectionSocket = new WebScoket(this.server_url);
    }
    connect() {
        this.connectionSocket.on("open", () => {
            console.log("Connected to server!");
        });
    }
    disconnect() {
        this.connectionSocket.close();
    }
    getServerUrl() {
        return this.server_url;
    }
    getPort() {
        return this.port;
    }
    getConnectionSocket() {
        return this.connectionSocket;
    }
}
//# sourceMappingURL=network.js.map