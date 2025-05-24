import { Network } from "./network";

let network:Network;

window.addEventListener("DOMContentLoaded", () =>{
   const connectButton = document.getElementById("connect_button"); 
   
   if(!connectButton) return; 

    connectButton.addEventListener("click", () => {
        console.log("button clicked!");
        const server_ip:string = (document.getElementById("server_ip")?.ariaValueText as string);
        const port:number = (parseInt((document.getElementById("port")?.ariaValueText as string)) as number);  
        network = new Network(server_ip, port); 
        network.connect();
    });
});