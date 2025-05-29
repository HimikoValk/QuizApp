export type Package<T> ={
    category:"REQUEST" | "RESPONSE"; 
    data:T;
};
export type Request<T> = {
    requestType: ""
    data:T;
     
};

// TODO
export type Response = {
    
};

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

    public send(obj:any) : void
    {
        const json:string = JSON.stringify(obj);
        this.connectionSocket.send(json); 
        console.log("Successfully send data to Server!");
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
        this.connectionSocket.addEventListener("message", (message) => {
            console.log("Recevied message:%s", message.data);
        });
    }

    public handelMessage()
    {

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