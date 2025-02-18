package com.himiko.server.utils;


import com.google.gson.Gson;
import com.himiko.server.protocol.Package;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

/**
 * @author Valk on 14.02.2025
 * @project quizServer
 */
public class NetworkClient {

    private final Socket client;

    private OutputStream clientStream;
    private PrintWriter writer;
    private BufferedReader reader;

    public NetworkClient(final Socket pClient) throws Exception
    {
        this.client = pClient;
        this.clientStream = this.client.getOutputStream();
        this.writer = new PrintWriter(this.clientStream, true);
        this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    public <T> void sendPackage(Package<T> data)
    {
        if(data == null) return;

        String json = new Gson().toJson(data);

        this.sendData(json);
    }

    public void sendData(String data)
    {
        if(this.writer == null) return;

        this.writer.println(data);
    }

    public String receive() throws Exception
    {
        if(this.reader != null) return this.reader.readLine();
        return null;
    }

    public Socket getClient() {
        return client;
    }

    public OutputStream getClientStream() {
        return clientStream;
    }

    public void setClientStream(OutputStream clientStream) {
        this.clientStream = clientStream;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }
}
