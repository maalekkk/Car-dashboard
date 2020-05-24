package org.Presentation;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import javafx.collections.ObservableList;
import org.Data.DashboardSerializationModel;
import org.Data.Serialization;
import org.Logic.Dashboard;
import org.Logic.GearException;
import org.Logic.SpeedThread;
import org.Logic.TurnSignalException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;


/**
 * The type Tui.
 */
public class Tui extends UiController {
    private Thread keyThread;
    private boolean engineRunning = false;
    private SpeedThread speedThread = null;
    private final ColoredPrinter cpWhite;
    private final ColoredPrinter cpCyan;
    private final ColoredPrinter cpGreen;
    private final ColoredPrinter cpRed;
    private final ColoredPrinter cpYellow;

    /**
     * Instantiates a new Tui.
     */
    public Tui() {
        cpWhite = new ColoredPrinter.Builder(1, false)
                .foreground(Ansi.FColor.WHITE)
                .build();
        cpCyan = new ColoredPrinter.Builder(1, false)
                .foreground(Ansi.FColor.CYAN)
                .build();
        cpGreen = new ColoredPrinter.Builder(1, false)
                .foreground(Ansi.FColor.GREEN)
                .build();
        cpRed = new ColoredPrinter.Builder(1, false)
                .foreground(Ansi.FColor.RED)
                .build();
        cpYellow = new ColoredPrinter.Builder(1, false)
                .foreground(Ansi.FColor.YELLOW)
                .build();
    }

    @Override
    public Dashboard getDashboard() {
        return dashboard;
    }

    private void separator() {
        cpCyan.println("\n------------------------------------------------------------------------------------");
    }

    private void printWithStaticWidthCenter(Object text, int width) {
        StringBuilder result = new StringBuilder();
        int len = width - text.toString().length();
        String repeat = " ".repeat(len / 2);
        result.append(repeat);
        if (len % 2 != 0)
            result.append(" ");
        result.append(text.toString());
        result.append(repeat);
        cpYellow.print(result);
    }

    private void printWithStaticWidthLeft(Object text, int width) {
        StringBuilder result = new StringBuilder();
        int len = text.toString().length();
        String repeat = " ".repeat(Math.max(0, width - len));
        result.append(text.toString());
        result.append(repeat);
        cpCyan.print(result);
    }

    private String lightsToString() {
        StringBuilder lights = new StringBuilder();
        lights.append("| Lights on:");
        if (dashboard.isLeftTurnSignal())
            lights.append(" Left turn signal ");
        if (dashboard.isFrontFogLights())
            lights.append(" Front fog ");
        if (dashboard.isRearFogLights())
            lights.append(" Rear fog ");
        if (dashboard.isLowBeamLights())
            lights.append(" Low beam ");
        if (dashboard.isHighBeamLights())
            lights.append(" High beam ");
        if (dashboard.isPositionLights())
            lights.append(" Position ");
        if (dashboard.isRightTurnSignal())
            lights.append(" Right turn signal ");
        return lights.toString();
    }

    @Override
    public void refresh() {
        try {
            cls();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        int width = 84;
        printMiniTitle(18);
        separator();
        printWithStaticWidthLeft("| Actual gear: " + dashboard.getActualGear(), width);
        cpCyan.print("|");
        separator();
        printWithStaticWidthLeft("| Speed: " + (Math.round(dashboard.getSpeed() * 10.0f) / 10.0f) + ", revs: " + (Math.round(dashboard.getRevs() * 10.0f) / 10.0f), width);
        cpCyan.print("|");
        separator();
        printWithStaticWidthLeft("| Avg. speed: " + dashboard.getOnBoardComputer().getAvgSpeed() + ", max. speed: " + dashboard.getOnBoardComputer().getMaxSpeed(), width);
        cpCyan.print("|");
        separator();
        printWithStaticWidthLeft("| Avg. fuel usage: " + dashboard.getOnBoardComputer().getAvgCombustion() + ", max. fuel usage: " + dashboard.getOnBoardComputer().getMaxCombustion(), width);
        cpCyan.print("|");
        separator();
        printWithStaticWidthLeft("| Journey distance: " + (Math.round(dashboard.getOnBoardComputer().getJourneyDistance() * 10.0f) / 10.0f) + ", journey time: " + dashboard.getOnBoardComputer().getJourneyTime(), width);
        cpCyan.print("|");
        separator();
        printWithStaticWidthLeft("| Total counter: " + (Math.round(dashboard.getCounter() * 10.0f) / 10.0f), width);
        cpCyan.print("|");
        separator();
        printWithStaticWidthLeft(lightsToString(), width);
        cpCyan.print("|");
        separator();
    }

    @Override
    public void engineStartStop(boolean enable, boolean engineError) throws TurnSignalException, InterruptedException {
        if (enable) {
            if (dashboard.getOnBoardComputer().getJourneyTimeLocalDateTime() == null)
                dashboard.getOnBoardComputer().startJourneyTime();
            engineRunning = true;
            keyThread();
            speedThread = new SpeedThread(this, 0);
            speedThread.setEngineRunning(true);
            speedThread.start();
        } else {
            keyThread.interrupt();
            keyThread = null;
            engineRunning = false;
            speedThread.setEngineRunning(false);
            speedThread.interrupt();
            speedThread = null;
            try {
                menu();
            } catch (IOException e) {}
        }
    }

    @Override
    public void reloadDashboardAfterSettings(boolean editMusicPlayer) throws InterruptedException {
        super.reloadDashboardAfterSettings(false);
    }

    @Override
    public void switchOffCruiseControl() {}

    private void cls() throws IOException, InterruptedException{
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

    private void keyThread() {
        keyThread = new Thread(){
            public void run()
            {
                char znak;
                Scanner scanner = new Scanner(System.in);
                while(engineRunning)
                {
                    znak = scanner.next().charAt(0);
                    System.out.println((short)(znak-'0'));
                    if(Character.isDigit(znak)) {
                        if(znak>= '0' && znak <= '6') {
                            try {
                                dashboard.setActualGear((byte) (znak-'0'),true);
                            } catch (GearException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        switch (znak) {
                            //Przyspieszanie/zwalnianie
                            case 'w':
                                dashboard.setKeyUp(true);
                                break;
                            case 's':
                                dashboard.setKeyUp(false);
                                break;
                                //Światła
                            case 'p':
                                dashboard.setPositionLights(!dashboard.isPositionLights());
                                break;
                            case 'l':
                                dashboard.setLowBeamLights(!dashboard.isLowBeamLights());
                                break;
                            case 'h':
                                dashboard.setHighBeamLights(!dashboard.isHighBeamLights());
                                break;
                            case 'r':
                                dashboard.setRearFogLights(!dashboard.isRearFogLights());
                                break;
                            case 'f':
                                dashboard.setFrontFogLights(!dashboard.isFrontFogLights());
                                break;
                            case '[':
                                try {
                                    dashboard.setLeftTurnSignal(!dashboard.isLeftTurnSignal());
                                } catch (TurnSignalException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case ']':
                                try {
                                    dashboard.setRightTurnSignal(!dashboard.isRightTurnSignal());
                                } catch (TurnSignalException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 'm':
                                try {
                                    engineStartStop(false, false);
                                } catch (TurnSignalException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    menu();
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                    try{
                        keyThread.sleep(300);
                    } catch(Exception e){}
                }
            }
        };
        keyThread.start();
    }

    private void printRowInMenu(Object text, int width, int leftMargin) {
        cpYellow.print(" ".repeat(leftMargin));
        cpCyan.print("| ");
        int len = text.toString().length();
        String repeat = " ".repeat(Math.max(0, width - len));
        cpWhite.print(text.toString() + repeat);
        cpCyan.println("|");
    }

    private void printMiniTitle(int leftMargin) {
        System.out.println();
        cpYellow.print(" ".repeat(leftMargin));
        cpYellow.println("________________________________________________");
        cpYellow.print(" ".repeat(leftMargin));
        cpYellow.print("| ");
        cpCyan.print(" __        __        __   __        __   __ ");
        cpYellow.println(" |");
        cpYellow.print(" ".repeat(leftMargin));
        cpYellow.print("| ");
        cpCyan.print("|  \\  /\\  /__` |__| |__) /  \\  /\\  |__) |  \\");
        cpYellow.println(" |");
        cpYellow.print(" ".repeat(leftMargin));
        cpYellow.print("| ");
        cpCyan.print("|__/ /~~\\ .__/ |  | |__) \\__/ /~~\\ |  \\ |__/");
        cpYellow.println(" |");
        cpYellow.print(" ".repeat(leftMargin));
        cpYellow.print("------------------------------------------------");
    }

    private void printBigTitle() {
        System.out.println();
        cpYellow.println("_______________________________________________________");
        cpYellow.print("| ");
        cpCyan.print("    ___          _     _                         _ ");
        cpYellow.println(" |");
        cpYellow.print("| ");
        cpCyan.print("   /   \\__ _ ___| |__ | |__   ___   __ _ _ __ __| |");
        cpYellow.println(" |");
        cpYellow.print("| ");
        cpCyan.print("  / /\\ / _` / __| '_ \\| '_ \\ / _ \\ / _` | '__/ _` |");
        cpYellow.println(" |");
        cpYellow.print("| ");
        cpCyan.print(" / /_// (_| \\__ \\ | | | |_) | (_) | (_| | | | (_| |");
        cpYellow.println(" |");
        cpYellow.print("| ");
        cpCyan.print("/___,' \\__,_|___/_| |_|_.__/ \\___/ \\__,_|_|  \\__,_|");
        cpYellow.println(" |");
        cpYellow.print("-------------------------------------------------------");
        System.out.println();
    }

    private void menu() throws IOException, InterruptedException {
        cls();
        char choice = 0;
        Scanner scanner = new Scanner(System.in);
        printBigTitle();
        while (choice < '1' || choice > '7') {
            cpYellow.print(" ".repeat(5));
            printWithStaticWidthCenter("Menu", 44);
            System.out.println();
            cpYellow.print(" ".repeat(5));
            cpCyan.println("---------------------------------------------");
            printRowInMenu("1. Turn on engine ('m' turn off)", 42, 5);
            printRowInMenu("2. Engine settings", 42, 5);
            printRowInMenu("3. Import", 42, 5);
            printRowInMenu("4. Export", 42, 5);
            printRowInMenu("5. About the program", 42, 5);
            printRowInMenu("6. Instructions", 42, 5);
            printRowInMenu("7. Exit", 42, 5);
            cpYellow.print(" ".repeat(5));
            cpCyan.println("---------------------------------------------");
            cpCyan.print("Please give me your choice: ");
            choice = scanner.next().charAt(0);
        }
        switch (choice) {
            case '1':
                try {
                    engineStartStop(true, false);
                } catch (TurnSignalException e) {
                    e.printStackTrace();
                }
                break;
            case '2':
                cls();
                System.out.print("Please give me maximum of speed in dashboard: ");
                short maxSpeed = scanner.nextShort();
                while (maxSpeed < 10 || maxSpeed > 999) {
                    System.out.print("Please give me number between 10 and 999: ");
                    maxSpeed = scanner.nextShort();
                }
                dashboard.getSettings().setMaxSpeed(maxSpeed);

                System.out.print("Please give me number of gears: ");
                byte numOfGears = scanner.nextByte();
                while (numOfGears != 5 && numOfGears != 6) {
                    System.out.print("Please give me number between 5 and 6: ");
                    numOfGears = scanner.nextByte();
                }
                dashboard.getSettings().setNumberOfGears(numOfGears);

                System.out.print("Please give me engine type 'P' like petrol or 'D' like diesel: ");
                char engineType = scanner.next().charAt(0);
                while (!(engineType == 'P') && !(engineType == 'D')) {
                    System.out.print("Please type 'P' or 'D': ");
                    engineType = scanner.nextLine().charAt(0);
                }
                if (engineType == 'P')
                    dashboard.getSettings().setEngineType('P');
                else dashboard.getSettings().setEngineType('D');
                dashboard.maxSpeedOnGears(dashboard.getSettings().getMaxSpeed(), dashboard.getSettings().getNumberOfGears());
                cpGreen.println("Settings saved.");
                break;
            case '3':
                cls();
                importDashboard();
                break;
            case '4':
                cls();
                exportDashboard();
                break;
            case '5':
                cls();
                printMiniTitle(35);
                System.out.println();
                cpCyan.println("-----------------------------------------------------------------------------------------------------------------------");
                cpYellow.print("|");
                printWithStaticWidthCenter("Dashboard is a simple application that presents a dashboard by driving the car through a mouse and keyboard.", 117);
                cpYellow.println("|");
                cpCyan.println("-----------------------------------------------------------------------------------------------------------------------");
                cpYellow.print("|");
                printWithStaticWidthCenter("Copyright 2020. Bartlomiej Malkowski.", 117);
                cpYellow.println("|");
                cpCyan.println("-----------------------------------------------------------------------------------------------------------------------");
                break;
            case '6':
                cls();
                printMiniTitle(0);
                System.out.println();
                instructionRow("w, s", "accelerate, slow down");
                instructionRow("n, m", "engine turn on/off");
                instructionRow("p, l, h, f, r, [, ]", "lights");
                instructionRow("1, 2, 3, 4, 5, 6", "gears");
                cpYellow.println("-----------------------|------------------------");
                break;
            case '7':
                try {
                    Serialization.writeDashboard(dashboard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
                break;
            default:
                menu();
                break;
        }
        if (choice != '1') {
            cpGreen.println("\nPress enter...");
            System.in.read();
            menu();
        }
    }

    private void instructionRow(String one, String two) {
        cpYellow.println("-----------------------|------------------------");
        cpYellow.print("| ");
        printWithStaticWidthLeft(one, 20);
        cpYellow.print(" | ");
        printWithStaticWidthLeft(two, 21);
        cpYellow.println(" |");
    }

    private void importDashboard() {
        char choice = '0';
        Scanner scanner = new Scanner(System.in);
        while(choice < '1' || choice > '3') {
            System.out.println();
            printWithStaticWidthCenter("Import", 30);
            System.out.println();
            cpCyan.println("------------------------------");
            printRowInMenu("1. SQL", 27, 0);
            printRowInMenu("2. XML", 27, 0);
            printRowInMenu("3. Return to main menu", 27, 0);
            cpCyan.println("------------------------------");
            cpCyan.print("Please give me your choice: ");
            choice = scanner.next().charAt(0);
        }
        if (choice == '1') {
            short choice_elem = -1;
            ObservableList<DashboardSerializationModel> observableList = dashboard.readFromDatabase();
            System.out.println();
            printWithStaticWidthCenter("Database Records", 105);
            System.out.println();
            System.out.println("---------------------------------------------------------------------------------------------------------");
            int i;
            cpWhite.print("|");
            printWithStaticWidthLeft("ID", 5);
            printWithStaticWidthLeft("avgSpeed", 10);
            printWithStaticWidthLeft("maxSpeed", 10);
            printWithStaticWidthLeft("avgFuel", 10);
            printWithStaticWidthLeft("maxFuel", 10);
            printWithStaticWidthLeft("jouDist", 10);
            printWithStaticWidthLeft("jouTime", 8);
            printWithStaticWidthLeft("DayCou1", 10);
            printWithStaticWidthLeft("DayCou2", 10);
            printWithStaticWidthLeft("Counter", 10);
            printWithStaticWidthLeft("Date", 10);
            cpWhite.println("|");
            System.out.println("---------------------------------------------------------------------------------------------------------");
            for (i = 0; i < observableList.size(); i++) {
                cpWhite.print("|");
                printWithStaticWidthLeft(i+1, 5);
                printWithStaticWidthLeft(observableList.get(i).getAvgSpeed(), 10);
                printWithStaticWidthLeft(observableList.get(i).getMaxSpeed(), 10);
                printWithStaticWidthLeft(observableList.get(i).getAvgFuel(), 10);
                printWithStaticWidthLeft(observableList.get(i).getMaxFuel(), 10);
                printWithStaticWidthLeft(Math.round(observableList.get(i).getJourneyDistance() * 10.0f) / 10.0f, 10);
                printWithStaticWidthLeft(observableList.get(i).getJourneyMinutes(), 8);
                printWithStaticWidthLeft(Math.round(observableList.get(i).getDayCounter1() * 10.0f) / 10.0f, 10);
                printWithStaticWidthLeft(Math.round(observableList.get(i).getDayCounter2() * 10.0f) / 10.0f, 10);
                printWithStaticWidthLeft(Math.round(observableList.get(i).getCounter() * 10.0f) / 10.0f, 10);
                printWithStaticWidthLeft(observableList.get(i).getRecordDate(), 10);
                cpWhite.println("|");
            }
            System.out.println("---------------------------------------------------------------------------------------------------------");
            while (choice_elem < 0 || choice_elem > i) {
                cpCyan.print("Please give me your choice: [type '0' if you don't import data from database] ");
                choice_elem = scanner.nextShort();
            }
            if (choice_elem != 0) {
                dashboard.updateDashboardFromFile(observableList.get(choice_elem - 1));
                cpGreen.println("Successful import!");
            }
        }
        else if (choice == '2') {
            cpCyan.print("Please give me path to file: ");
            String path = scanner.nextLine();
            dashboard.readFromXML(Paths.get(path));
            cpGreen.println("Successful import!");
        } else {
            try {
                menu();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void exportDashboard() {
        char choice = '0';
        Scanner scanner = new Scanner(System.in);
        while(choice < '1' || choice > '3') {
            System.out.println();
            printWithStaticWidthCenter("Export", 30);
            System.out.println();
            cpCyan.println("------------------------------");
            printRowInMenu("1. SQL", 27, 0);
            printRowInMenu("2. XML", 27, 0);
            printRowInMenu("3. Return to main menu", 27, 0);
            cpCyan.println("------------------------------");
            cpCyan.print("Please give me your choice: ");
            choice = scanner.next().charAt(0);
        }
        if (choice == '1') {
            dashboard.writeToDatabase();
            cpGreen.println("Successful export!");
        }
        else if (choice == '2') {
            cpCyan.print("Please give me path to file: ");
            String path = scanner.nextLine();
            dashboard.writeToXml(Paths.get(path));
            cpGreen.println("Successful export!");
        } else {
            try {
                menu();
            } catch (IOException | InterruptedException e) {
                cpRed.println("Path to file is wrong. Export failed.");
            }
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        Tui tui = new Tui();
        tui.menu();

    }
}
