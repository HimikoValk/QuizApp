package com.himiko.server.protocol;


/**
 * @author Valk on 16.02.2025
 * @project quizServer
 */
public class Package<T>{
    private T data;
    private PackageCategory category;

    public Package(T data, PackageCategory action)
    {
        this.data = data;
        this.category = action;
    }

    public PackageCategory getAction() {
        return this.category;
    }

    public void setCategory(PackageCategory category) {
        this.category = category;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
