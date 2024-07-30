package am.aua.hw;

import am.aua.cli.SchedulerConsole;
import am.aua.exceptions.MalformedStringParameterException;
import am.aua.ui.SchedulerUI;

public class Main {
    public static void main(String[] args) throws MalformedStringParameterException {
        if (args.length > 0) {
            String mode = args[0];
            if (mode.equals("-ui")) {
                // Launch graphical UI
                SchedulerUI ui = new SchedulerUI();
                ui.showUI();
            } else if (mode.equals("-cli")) {
                // Launch command-line UI
                SchedulerConsole cli = new SchedulerConsole();
                cli.start();
            } else {
                System.out.println("Invalid argument. Launching default UI mode.");
                SchedulerUI ui = new SchedulerUI();
                ui.showUI();
            }
        } else {
            // No arguments, launch default UI mode
            SchedulerUI ui = new SchedulerUI();
            ui.showUI();
        }
    }
}
