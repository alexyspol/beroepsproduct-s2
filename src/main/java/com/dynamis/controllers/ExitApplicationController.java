package com.dynamis.controllers;

import com.dynamis.App;

public class ExitApplicationController implements Controller {

    private String name;
    private App app;

    public ExitApplicationController(String name, App app) {
        this.name = name;
        this.app = app;
    }

    @Override public void run() {
        app.quit = true;
    }

    @Override public String toString() {
        return name;
    }

}
