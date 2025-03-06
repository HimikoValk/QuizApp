package com.himiko.server.protocol.request;

public class UserRequest {
    private UserRequestType requestType;

    public UserRequest(UserRequestType requestType) {
        this.requestType = requestType;
    }

    public UserRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(UserRequestType requestType) {
        this.requestType = requestType;
    }
}
