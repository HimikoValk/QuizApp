import WebScoket from "ws";

export class Network
{
    private connectionSocket:WebScoket;  
    private readonly server_ip:string;
    private readonly port:number;
    private readonly server_url:string;

    constructor(server_ip:string, port:number)
    {
        this.server_ip = server_ip;
        this.port = port;
        const tmp_url = `ws://${this.server_ip}:${this.port}`;
        this.server_url = tmp_url;
        this.connectionSocket = new WebScoket(this.server_url); 
    }

    public connect() : void
    {
        this.connectionSocket.on("open", () => {
            console.log("Connected to server!");
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

    public getPort():number
    {
        return this.port;
    }

    public getConnectionSocket():WebScoket
    {
        return this.connectionSocket; 
    }
    
}