package com.himiko.server.protocol.request;

public class Request<T> {
    private T data;
    private RequestType requestType;

    public Request(T data,RequestType requestType) {
        this.data = data;
        this.requestType = requestType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }
}
