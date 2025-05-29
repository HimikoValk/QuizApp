import { Network } from "./network.js";
let network;
window.addEventListener("DOMContentLoaded", () => {
    const connectButton = document.getElementById("connect_button");
    const disconnectButton = document.getElementById("disconnect_button");
    if (!connectButton || !disconnectButton)
        return;
    connectButton.addEventListener("click", () => {
        const ipInput = document.getElementById("server_ip");
        const portInput = document.getElementById("port");
        const userInput = document.getElementById("username");
        const server_ip = ipInput.value;
        const port = parseInt(portInput.value, 10);
        const username = userInput.value.trim();
        console.log("Init connection to server... IP:%s Port:%d\nAnd sending login for test", server_ip, port);
        network = new Network(server_ip, port);
        network.getConnectionSocket().addEventListener("open", () => {
            const pkg = {
                category: "REQUEST",
                data: {
                    requestType: "USER_LOGIN",
                    data: {
                        name: username,
                        id: null,
                    },
                },
            };
            network.send(pkg);
        });
    }); // ← Hier schließen wir den connectButton-Handler
    disconnectButton.addEventListener("click", () => {
        if (network === undefined) {
            alert("You need to establish a connection first!");
            return;
        }
        network.disconnect();
        alert("Disconnected from Server!");
    });
}); // ← Und hier den DOMContentLoaded-Handler
//# sourceMappingURL=index.js.map