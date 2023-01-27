package metro.io;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import metro.domain.Station;
import metro.domain.SubwayLine;
import metro.util.LinkedNode;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * reads subway input from files in json format using Gson library
 */
public class SubwayReader {
    private final Gson gson = new Gson();
    public List<SubwayLine> read(File file) {
        List<SubwayLine> subwayLines = new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Map<String, JsonElement> jsonLines = gson.fromJson(reader, JsonObject.class).asMap();

            for (String lineName : jsonLines.keySet()) {
                subwayLines.add(readAsSubwayLine(lineName, jsonLines.get(lineName)));
            }
            return subwayLines;
        } catch (IOException e) {
            throw new IllegalArgumentException("Error! Such a file doesn't exist!");
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Incorrect file");
        }
    }

    private SubwayLine readAsSubwayLine(String lineName, JsonElement jsonLines) {
        Set<Station> stations = new HashSet<>();
        for(JsonElement el: jsonLines.getAsJsonArray()){
            stations.add(gson.fromJson(el, Station.class));
        }
        return new SubwayLine(lineName, stations);
    }
}