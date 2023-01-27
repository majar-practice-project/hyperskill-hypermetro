package metro.util;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DijkstraPerformer<T> {
    private List<T> path;
    private int cost;
    public void perform(Function<T, Set<T>> neighborFunction, BiFunction<T, T, Integer> weightFunction, T start, T dest) {
        Map<T, Integer> distances = new HashMap<>(Map.of(start, 0));
        Map<T, T> prevMap = new HashMap<>();
        prevMap.put(start, null);

        Queue<T> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        queue.add(start);

        while (!queue.isEmpty()) {
            T node = queue.poll();
            if(node.equals(dest)) break;

            int dist = distances.get(node);
            if(dist == -1) continue;    // -1 means visited
            distances.put(node, -1);

            Set<T> neighbors = neighborFunction.apply(node);

            for (T neighbor : neighbors) {
                int neighborDist = dist + weightFunction.apply(node, neighbor);
                if (neighborDist < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, neighborDist);
                    queue.add(neighbor);
                    prevMap.put(neighbor, node);
                }
            }
        }


        if(!prevMap.containsKey(dest)) {
            path = null;
            cost = -1;
        } else {
            path = linkedNodesToPath(prevMap, start, dest);
            cost = distances.get(dest);
        }
    }

    public Optional<List<T>> getPath() {
        return Optional.ofNullable(path);
    }

    public int getCost() {
        return cost;
    }

    private static <T> List<T> linkedNodesToPath(Map<T, T> prevMap, T start, T dest) {
        Deque<T> stack = new ArrayDeque<>();
        stack.push(dest);

        T node = dest;
        while(!node.equals(start)){
            node = prevMap.get(node);
            stack.push(node);
        }

        return new ArrayList<>(stack);
    }


}
