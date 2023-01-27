package metro.view;

import metro.Configuration;
import metro.domain.PathResult;
import metro.domain.Station;
import metro.domain.StationInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class CommandView {
    private String TIME_UNIT = Configuration.getTimeUnit();
    private final Scanner scanner = new Scanner(System.in);

    private final StationPrinter stationPrinter = new StationPrinter();

    public void showStationLine(List<Station> lines) {
        stationPrinter.print(lines);
    }

    public void showStationPath(List<StationInfo> stations) {
        stationPrinter.printPath(stations);
    }

    public void showStationFastestPath(PathResult result){
        stationPrinter.printPath(result.getPath());
        System.out.printf("Total: %d %s in the way%n", result.getCost(), TIME_UNIT);
    }

    public void showError(Exception e) {
        System.out.println(e.getMessage());
    }

    public String[] getCommand() {
        return Arrays.stream(scanner.nextLine().split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"))
                .map(s -> s.replaceAll("^\"|\"$", ""))
                .toArray(String[]::new);
    }
}
