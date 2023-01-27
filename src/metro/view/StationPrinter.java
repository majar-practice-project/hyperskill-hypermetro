package metro.view;

import metro.domain.Station;
import metro.domain.StationInfo;

import java.util.List;
import java.util.Set;

public class StationPrinter {
    private static final String END = "depot";

    // not doing anything smart, just printing all the stations in whatever order
    public void print(List<Station> stations) {
        if (stations.isEmpty()) return;

        System.out.println(END);
        for (Station station : stations) {
            printStation(station);
        }
        System.out.println(END);
    }

    private void printStation(Station station) {
        Set<StationInfo> transfers = station.getTransfer();
        if (transfers.isEmpty()) {
            System.out.println(station.getName());
        } else {
            System.out.print(station.getName());
            for (StationInfo transferStation : transfers) {
                System.out.printf(" - %s (%s)", transferStation.getStation(), transferStation.getLine());
            }
            System.out.println();
        }
    }

    public void printPath(List<StationInfo> stations) {
        StationInfo currentStation = stations.get(0);
        System.out.println(currentStation.getStation());
        String currentLine = currentStation.getLine();

        for (int i = 1; i < stations.size(); i++) {
            currentStation = stations.get(i);
            if (!currentLine.equals(currentStation.getLine())) {
                currentLine = currentStation.getLine();
                printLineTransition(currentLine);
            }
            System.out.println(currentStation.getStation());
        }
    }

    private void printLineTransition(String newLine) {
        System.out.printf("Transition to line %s%n", newLine);
    }
}