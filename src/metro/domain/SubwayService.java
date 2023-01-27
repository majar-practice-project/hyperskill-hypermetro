package metro.domain;

import metro.Configuration;
import metro.data.StationGraphRepository;
import metro.data.SubwayRepository;
import metro.io.SubwayReader;
import metro.util.DijkstraPerformer;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SubwayService {
    private final DijkstraPerformer<StationInfo> dijkstraPerformer = new DijkstraPerformer<>();
    private final SubwayRepository subwayRepository;
    private final StationGraphRepository stationGraphRepository;

    private final int TRANSFER_TIME = Configuration.getTransferTime();

    public SubwayService(String subwayInfoFileName) {
        List<SubwayLine> lines = new SubwayReader().read(new File(subwayInfoFileName));
        subwayRepository = new SubwayRepository(lines);
        stationGraphRepository = new StationGraphRepository(lines);
    }

    private static Station findStationByName(SubwayLine line, String stationName) {
        return line.findAny(station -> station.getName().equals(stationName))
                .orElseThrow(() -> new IllegalArgumentException("No such station found!"));
    }

    public SubwayLine getById(String id) {
        return subwayRepository.getById(id);
    }

    public void appendStation(Station station, String subwayLineName) {
        SubwayLine newSubwayLine = getById(subwayLineName);
        newSubwayLine.add(station);
        subwayRepository.save(newSubwayLine);
    }

    public void prependStation(Station station, String subwayLineName) {
        SubwayLine newSubwayLine = getById(subwayLineName);
        newSubwayLine.add(station);
        subwayRepository.save(newSubwayLine);
    }

    public void removeStation(String subwayLineName, String stationNameToBeRemoved) {
        SubwayLine newSubwayLine = getById(subwayLineName);
        Station stationToBeRemoved = findStationByName(newSubwayLine, stationNameToBeRemoved);
        StationInfo stationInfoToBeRemoved = new StationInfo(subwayLineName, stationToBeRemoved.getName());

        stationGraphRepository.removeStation(stationInfoToBeRemoved);

        // remove from transfer list
        stationToBeRemoved.getTransfer().forEach(transfer -> {
            String stationName = transfer.getStation();
            SubwayLine line = subwayRepository.getById(transfer.getLine());
            Station connectedStation = line.findAny(station -> station.getName().equals(stationName)).orElseThrow();
            connectedStation.getTransfer().remove(stationInfoToBeRemoved);
            subwayRepository.save(line);
        });

        newSubwayLine.remove(station -> station.getName().equals(stationToBeRemoved.getName()));
        subwayRepository.save(newSubwayLine);
    }

    public void connectStations(String lineName1, String stationName1, String lineName2, String stationName2) {
        SubwayLine line1 = subwayRepository.getById(lineName1);
        SubwayLine line2 = subwayRepository.getById(lineName2);
        Station station1 = findStationByName(line1, stationName1);
        Station station2 = findStationByName(line2, stationName2);

        StationInfo stationInfo1 = new StationInfo(lineName1, stationName1);
        StationInfo stationInfo2 = new StationInfo(lineName2, stationName2);

        stationGraphRepository.connectStations(stationInfo1, stationInfo2, TRANSFER_TIME);

        station1.getTransfer().add(stationInfo1);
        station2.getTransfer().add(stationInfo2);

        subwayRepository.save(line1);
        subwayRepository.save(line2);
    }

    public List<StationInfo> findPath(String startLineName, String startStationName, String destLineName, String destStationName) {
        StationInfo startStation = new StationInfo(startLineName, startStationName);
        StationInfo destStation = new StationInfo(destLineName, destStationName);

        Function<StationInfo, Set<StationInfo>> neighborFunction = station -> stationGraphRepository.getNeighbors(station).orElseThrow();

        BiFunction<StationInfo, StationInfo, Integer> weightFunction =
                (station1, station2) -> station1.getLine().equals(station2.getLine()) ? 1 : 0;

        dijkstraPerformer.perform(neighborFunction, weightFunction, startStation, destStation);

        return dijkstraPerformer.getPath().orElseThrow(() -> new IllegalArgumentException("No path can be found!"));
    }

    public PathResult findFastestPath(String startLineName, String startStationName, String destLineName, String destStationName) {
        StationInfo startStation = new StationInfo(startLineName, startStationName);
        StationInfo destStation = new StationInfo(destLineName, destStationName);

        Function<StationInfo, Set<StationInfo>> neighborFunction = station -> stationGraphRepository.getNeighbors(station).orElseThrow();
        BiFunction<StationInfo, StationInfo, Integer> weightFunction =
                (station1, station2) -> stationGraphRepository.getWeight(station1, station2).orElseThrow();

        dijkstraPerformer.perform(neighborFunction, weightFunction, startStation, destStation);

        return new PathResult(dijkstraPerformer.getPath().orElseThrow(() -> new IllegalArgumentException("No path can be found!")),
                dijkstraPerformer.getCost());
    }
}