package metro.data;

import metro.Configuration;
import metro.domain.Station;
import metro.domain.StationInfo;
import metro.domain.SubwayLine;

import java.util.*;

public class StationGraphRepository {
    private final Map<StationInfo, Map<StationInfo, Integer>> adajcencyList = new HashMap<>();

    private final int TRANSFER_TIME = Configuration.getTransferTime();
    public StationGraphRepository(List<SubwayLine> lines){
        for(SubwayLine line: lines){
            Set<Station> stations = line.getStations();

            for(Station station: stations){
                addStation(line.getName(), station);
            }
        }
    }

    public Optional<Set<StationInfo>> getNeighbors(StationInfo station){
        if(!adajcencyList.containsKey(station)) return Optional.empty();

        return Optional.of(Collections.unmodifiableSet(adajcencyList.get(station).keySet()));
    }

    public Optional<Integer> getWeight(StationInfo station1, StationInfo station2){
        if(!adajcencyList.containsKey(station1) || !adajcencyList.get(station1).containsKey(station2)) {
            return Optional.empty();
        }

        return Optional.of(adajcencyList.get(station1).get(station2));
    }

    public boolean connectStations(StationInfo station1, StationInfo station2, int weight){
        if(!adajcencyList.containsKey(station1) || !adajcencyList.containsKey(station2)) return false;

        adajcencyList.get(station1).put(station2, weight);
        adajcencyList.get(station2).put(station1, weight);
        return true;
    }

    public void removeStation(StationInfo stationToBeRemoved){
        for(StationInfo station: adajcencyList.get(stationToBeRemoved).keySet()){
            adajcencyList.get(station).remove(stationToBeRemoved);
        }
        adajcencyList.remove(stationToBeRemoved);
    }

    public void addStation(String lineName, Station stationToBeAdded){
        StationInfo stationInfo = new StationInfo(lineName, stationToBeAdded.getName());
        adajcencyList.putIfAbsent(stationInfo, new HashMap<>());
        Map<StationInfo, Integer> neighbors = adajcencyList.get(stationInfo);

        Set<StationInfo> transferStations = stationToBeAdded.getTransfer();
        for(StationInfo transferStation: transferStations){
            neighbors.put(transferStation, TRANSFER_TIME);
        }

        for(String name: stationToBeAdded.getNext()){
            StationInfo nextStation = new StationInfo(lineName, name);
            neighbors.put(nextStation, stationToBeAdded.getTime());
            adajcencyList.putIfAbsent(nextStation, new HashMap<>());
            adajcencyList.get(nextStation).put(stationInfo, stationToBeAdded.getTime());
        }
    }
}
