package metro.data;

import metro.domain.SubwayLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubwayRepository {
    private final Map<String, SubwayLine> LINES = new HashMap<>();

    public SubwayRepository(List<SubwayLine> stationLines){
        for (SubwayLine stationLine : stationLines) {
            LINES.put(stationLine.getName(), stationLine);
        }
    }

    public SubwayLine getById(String id){
        if (!LINES.containsKey(id)) throw new IllegalArgumentException("No such line found!");
        return new SubwayLine(LINES.get(id));
    }

    public void save(SubwayLine line){
        LINES.put(line.getName(), line);
    }

}
