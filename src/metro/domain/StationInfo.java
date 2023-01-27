package metro.domain;

import java.util.Objects;

public class StationInfo {
    private String line;
    private String station;

    public StationInfo(String line, String station) {
        this.line = line;
        this.station = station;
    }

    public String getLine() {
        return line;
    }

    public String getStation() {
        return station;
    }

    @Override
    public String toString() {
        return "TransferStation{" +
                "line='" + line + '\'' +
                ", station='" + station + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationInfo that = (StationInfo) o;
        return line.equals(that.line) && station.equals(that.station);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, station);
    }
}