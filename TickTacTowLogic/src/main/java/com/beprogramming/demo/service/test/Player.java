package com.beprogramming.demo.service.test;

public enum Player {
    X("PlayerX"), O("PlayerO");

    public String getColour() {
        return colour;
    }
    public void setColour(String colour) {
        this.colour = colour;
    }

    private String colour;


    Player(String colour) {
        this.colour = colour;
    }
}
