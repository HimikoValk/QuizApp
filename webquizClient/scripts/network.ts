export class Network
{
    private connectionSocket:WebSocket;  
    private readonly server_url:string;

    constructor(server_ip:string, port:number)
    {
        this.server_url = `ws://${server_ip}:${port}/ws`;
        this.connectionSocket = new WebSocket(this.server_url); 
        //Event listerner for socket
        this.initEvents();
    }

    private initEvents() : void
    { 
        this.connectionSocket.addEventListener("open", () => {
            alert("Connected to server!");
            console.log("Connected to server!");
        });
        this.connectionSocket.addEventListener("close", () => {
            alert("🔒 Connection closed");
            console.log("🔒 Connection closed");
        });
    }

    public disconnect():void
    {
        this.connectionSocket.close();
    }

    public getServerUrl():string 
    {
        return this.server_url;
    }

    public getConnectionSocket():WebSocket
    {
        return this.connectionSocket; 
    }   
}