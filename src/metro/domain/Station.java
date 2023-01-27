package metro.domain;

import java.util.*;

public class Station {
    private String name;
    private Set<StationInfo> transfer;
    private List<String> prev;
    private List<String> next;
    private int time;

    public Station(String name, Set<StationInfo> transferStations) {
        this.name = name;
        this.transfer = transferStations;
    }

    public Station(String name) {
        this.name = name;
        this.transfer = new HashSet<>();
    }

    /**
     * copy of the prototype,
     * in case there are mutable states
     * @param prototype
     */
    public Station(Station prototype) {
        this.name = prototype.name;
        this.transfer = prototype.transfer;
        this.time = prototype.time;
        this.prev = prototype.prev;
        this.next = prototype.next;
    }

    public int getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public Set<StationInfo> getTransfer() {
        return Collections.unmodifiableSet(transfer);
    }

    public List<String> getPrev() {
        return Collections.unmodifiableList(prev);
    }

    public List<String> getNext() {
        return Collections.unmodifiableList(next);
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", transfer=" + transfer +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return name.equals(station.name) && transfer.equals(station.transfer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, transfer);
    }
}