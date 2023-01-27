package metro.domain;

import java.util.Collections;
import java.util.List;

public class PathResult {
    private final List<StationInfo> path;
    private final int cost;

    public PathResult(List<StationInfo> path, int cost) {
        this.path = Collections.unmodifiableList(path);
        this.cost = cost;
    }

    public List<StationInfo> getPath() {
        return path;
    }

    public int getCost() {
        return cost;
    }
}
