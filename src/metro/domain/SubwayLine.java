package metro.domain;

import java.util.*;
import java.util.function.Predicate;

public class SubwayLine implements Cloneable {
    private final String name;
    private final Set<Station> stations;

    public SubwayLine(String name, Set<Station> stations) {
        this.name = name;
        this.stations = stations;
    }

    /**
     * deep copy of the prototype
     * @param prototype
     */
    public SubwayLine(SubwayLine prototype){
        this.name = prototype.name;
        stations = new HashSet<>();
        for(Station station: prototype.stations){
            stations.add(new Station(station));
        }
    }

    public String getName() {
        return name;
    }

    public Optional<Station> findAny(Predicate<Station> condition){
        for(Station station: stations){
            if(condition.test(station)) return Optional.of(station);
        }
        return Optional.empty();
    }

    public void add(Station station) {
        stations.add(station);
    }
    public Set<Station> getStations(){
        return Collections.unmodifiableSet(stations);
    }
    public void remove(Predicate<Station> condition) {
        stations.removeIf(condition);
    }

    @Override
    public String toString() {
        return "SubwayLine{" +
                "name='" + name + '\'' +
                ", stations=" + stations +
                '}';
    }
}