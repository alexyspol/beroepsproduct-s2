package com.dynamis.options;

import com.dynamis.App;

public class ExitApplicationOption implements Option {

    @Override
    public void run(App app) {
        System.out.println("\n> Goodbye\n");
        app.setUserWantsToExit(true);
    }

    @Override
    public String toString() {
        return "Exit application";
    }

}
