package com.himiko.server.utils;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;

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

    public void sendData(String data)
    {
        if(this.writer == null) return;
        this.writer.println(data);
        this.writer.flush();
    }

    public String receive() {
        if (this.reader != null)
            try {
                return this.reader.readLine();
            } catch (SocketException se) {
                if (se.getMessage().equals("Connection reset")) {
                    return null;
                } else {
                    throw new RuntimeException(se);
                }
            } catch (IOException e) {
                return null;
            }
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
