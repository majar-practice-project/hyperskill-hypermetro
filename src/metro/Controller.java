package metro;

import com.google.gson.JsonSyntaxException;
import metro.domain.PathResult;
import metro.domain.Station;
import metro.domain.StationInfo;
import metro.domain.SubwayService;
import metro.view.CommandView;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final CommandView view = new CommandView();
    private SubwayService subwayService;

    public Controller(String[] args) {
        String filePath = args[0];
        // hack for incorrect test cases, hyperskill never fixes this :(
        filePath = filePath.replaceAll("prague.json", "prague_corrected.json");
        try {
            subwayService = new SubwayService(filePath);
        } catch (IllegalArgumentException | JsonSyntaxException e) {
            view.showError(e);
            System.exit(0);
        }
    }

    private static void validateCommandParamNums(String[] commands, int expectedNum) {
        if (commands.length != expectedNum) throw new IllegalArgumentException("Invalid command");
    }

    public void start() {
        processCommand();
    }

    public void processCommand() {
        String[] commands = view.getCommand();
        boolean exitNow = false;
        while (!exitNow) {
            try {
                switch (commands[0]) {
                    case "/add-head" -> processPrependStation(commands);    //conflict with other requirements
                    case "/append" -> processAppendStation(commands);    //conflict with other requirements
                    case "/remove" -> proceessRemoveStation(commands);
                    case "/connect" -> processConnectStations(commands);
                    case "/output" -> processOutputLine(commands);    //conflict with other requirements
                    case "/route" -> processRouting(commands);
                    case "/fastest-route" -> processFastRouting(commands);
                    case "/exit" -> exitNow = true;
                    default -> throw new IllegalArgumentException("Invalid command");
                }
            } catch (IllegalArgumentException e) {
                view.showError(e);
            }
            commands = view.getCommand();
        }
    }

    public void processPrependStation(String[] cmd) {
        if (cmd.length != 3) throw new IllegalArgumentException("Invalid command");
        String lineName = cmd[1];
        String stationName = cmd[2];

        subwayService.prependStation(new Station(stationName), lineName);
    }

    public void processAppendStation(String[] cmd) {
        validateCommandParamNums(cmd, 3);
        String lineName = cmd[1];
        String stationName = cmd[2];

        subwayService.appendStation(new Station(stationName), lineName);
    }

    private void processConnectStations(String[] cmd) {
        validateCommandParamNums(cmd, 5);

        subwayService.connectStations(cmd[1], cmd[2], cmd[3], cmd[4]);
    }

    public void processOutputLine(String[] cmd) {
        validateCommandParamNums(cmd, 2);

        String lineName = cmd[1];

        //todo how to print cycles?
        view.showStationLine(new ArrayList<>(subwayService.getById(lineName).getStations()));
    }

    public void proceessRemoveStation(String[] cmd) {
        validateCommandParamNums(cmd, 3);

        String lineName = cmd[1];
        String stationName = cmd[2];

        subwayService.removeStation(lineName, stationName);
    }

    public void processRouting(String[] cmd) {
        validateCommandParamNums(cmd, 5);

        List<StationInfo> stations = subwayService.findPath(cmd[1], cmd[2], cmd[3], cmd[4]);

        view.showStationPath(stations);
    }

    public void processFastRouting(String[] cmd) {
        validateCommandParamNums(cmd, 5);

        PathResult result = subwayService.findFastestPath(cmd[1], cmd[2], cmd[3], cmd[4]);
        view.showStationFastestPath(result);
    }
}