package com.himiko.network.protocol;

public class Package<T>{
    private T data;
    private PackageCategory category;

    public Package(T data, PackageCategory category)
    {
        this.data = data;
        this.category = category;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public PackageCategory getCategory() {
        return category;
    }

    public void setCategory(PackageCategory category) {
        this.category = category;
    }
}
