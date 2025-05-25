import { Network } from "./network.js";

let network:Network;

window.addEventListener("DOMContentLoaded", () =>{
   const connectButton = document.getElementById("connect_button");  
   const disconnectButton = document.getElementById("disconnect_button");

   if(!connectButton || !disconnectButton) return; 

    connectButton.addEventListener("click", () => {
        const ipInput = document.getElementById("server_ip") as HTMLInputElement;
        const server_ip:string = ipInput.value;
        const portInput = document.getElementById("port") as HTMLInputElement;
        const port:number = parseInt(portInput.value, 10);
        console.log("Init connection to server... IP:%s Port:%d", server_ip, port);
        network = new Network(server_ip, port); 
    });

   
    disconnectButton.addEventListener("click", () => {
        if(network === undefined){ 
            alert(" You need to establish a connectio first!");
            return;
        } 

        network.disconnect();
        alert("Disconnected from Server!");
    }); 
});