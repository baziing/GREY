package com.example.grey.setting;

public class Option {
    private String name;
    private int imageId;

    public Option(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId(){
        return imageId;
    }
}
