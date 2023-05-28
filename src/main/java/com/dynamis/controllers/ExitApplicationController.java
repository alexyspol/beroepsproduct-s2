package com.dynamis.controllers;

import com.dynamis.App;

public class ExitApplicationController implements Controller {

    private String description;
    private App app;

    public ExitApplicationController(String description, App app) {
        this.description = description;
        this.app = app;
    }

    @Override public void run() {
        app.quit = true;
    }

    @Override public String toString() {
        return description;
    }

}
