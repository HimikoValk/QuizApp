package com.himiko.server.protocol.response;

public class Response<T> {
    private ResponseType responseType;
    private T data;

    public Response(T data, ResponseType responseType) {
        this.data = data;
        this.responseType = responseType;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
