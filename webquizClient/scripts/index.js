import { Network } from "./network";
let network;
window.addEventListener("DOMContentLoaded", () => {
    const connectButton = document.getElementById("connect_button");
    if (!connectButton)
        return;
    connectButton.addEventListener("click", () => {
        console.log("button clicked!");
        const server_ip = document.getElementById("server_ip")?.ariaValueText;
        const port = parseInt(document.getElementById("port")?.ariaValueText);
        network = new Network(server_ip, port);
        network.connect();
    });
});
//# sourceMappingURL=index.js.map