package metro.util;

import java.util.*;
import java.util.function.Function;

/**
 * @deprecated Replaced by dijkstra's algorithm {@link metro.util.DijkstraPerformer}.
 * BiDirectionBFSPerformer may not work correctly under edge cases and doesn't work for weighted graph :(
 * <p> Use it with caution!
 * @param <T>
 */
@Deprecated()
public class BiDirectionBFSPerformer<T> {
    public Optional<List<T>> perform(Function<T, List<T>> getNeighborFunction, T start, T dest){

        Map<T, T> startVisitedPrev = new HashMap<>();
        Map<T, T> destVisitedPrev = new HashMap<>();

        T intersection = getIntersection(startVisitedPrev, destVisitedPrev, getNeighborFunction, start, dest);
        if(intersection==null) return Optional.empty();

        return Optional.of(constructPath(startVisitedPrev, destVisitedPrev, intersection));
    }

    private static <T> T getIntersection(Map<T, T> startOrderMap, Map<T, T> destOrderMap, Function<T, List<T>> neighborFunction, T start, T dest){
        Deque<T> startDeque = new ArrayDeque<>();
        Deque<T> destDeque = new ArrayDeque<>();

        startDeque.add(start);
        startOrderMap.put(start, null);
        destDeque.add(dest);
        destOrderMap.put(dest, null);

        while(!startDeque.isEmpty() || !destDeque.isEmpty()){
            if(!startDeque.isEmpty()){
                if(destOrderMap.containsKey(startDeque.peek())) {
                    return startDeque.peek();
                }
                visitOnce(startDeque, startOrderMap, neighborFunction);
            }

            if(!destDeque.isEmpty()){
                if(startOrderMap.containsKey(destDeque.peek())) {
                    return destDeque.peek();
                }
                visitOnce(destDeque, destOrderMap, neighborFunction);
            }
        }
        return null;
    }

    private static <T> void visitOnce(Deque<T> queue, Map<T, T> orderMap, Function<T, List<T>> neighborFunction){
        T element = queue.poll();

        neighborFunction.apply(element).forEach(neighbor -> {
            if(!orderMap.containsKey(neighbor)){
                orderMap.put(neighbor, element);
                queue.add(neighbor);
            }
        });
    }

    private static <T> List<T> constructPath(Map<T, T> startOrderMap, Map<T, T> destOrderMap, T intersection){
        Deque<T> stack = new ArrayDeque<>();

        T element = intersection;
        while(element != null){
            stack.push(element);
            element = startOrderMap.get(element);
        }

        List<T> path = new ArrayList<>(stack);
        element = destOrderMap.get(intersection);
        while(element != null){
            path.add(element);
            element = destOrderMap.get(element);
        }
        return path;
    }
}