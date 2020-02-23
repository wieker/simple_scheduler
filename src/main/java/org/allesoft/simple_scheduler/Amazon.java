package org.allesoft.simple_scheduler;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class Amazon {
    // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
    public ArrayList<String> popularNFeatures(int numFeatures,
                                              int topFeatures,
                                              List<String> possibleFeatures,
                                              int numFeatureRequests,
                                              List<String> featureRequests)
    {
        // WRITE YOUR CODE HERE
        List<Set<String>> filteredR = featureRequests.subList(0, numFeatureRequests).stream()
                .map(String::toLowerCase).map(s -> new HashSet<String>(Arrays.asList(s.split("[\\p{Punct}\\s]+")))).collect(Collectors.toList());

        List<String> filteredP = possibleFeatures.stream()
                .map(String::toLowerCase).collect(Collectors.toList());
        Map<String, Integer> counter = new HashMap<>();
        filteredP.forEach(e -> {
            counter.put(e, (int) filteredR.stream().filter(s -> s.contains(e)).count());
        });
        filteredP.sort((s, t1) -> {
            int iComp = counter.get(s).compareTo(counter.get(t1));
            if (iComp == 0) {
                return s.compareTo(t1);
            } else {
                return -iComp;
            }
        });
        System.out.println(counter);
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < topFeatures && i < numFeatures; i ++) {
            String o = filteredP.get(i);
            Integer feature = counter.get(o);
            if (feature > 0) {
                // Return with the original case
                result.add(possibleFeatures.stream().filter(o::equalsIgnoreCase).findFirst().orElse("impossible"));
            } else {
                break;
            }
        }
        return result;
    }

    class Point {
        int x;
        int y;
        int t;
    }

    int minimumHours(int rows, int columns, List<List<Integer> > grid)
    {
        // WRITE YOUR CODE HERE
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < rows; i ++) {
            for (int j = 0; j < columns; j ++) {
                if (grid.get(i).get(j) == 1) {
                    Point point = new Point();
                    point.x = i;
                    point.y = j;
                    point.t = 0;
                    points.add(point);
                }
            }
        }
        int result = -1;
        for (int i = 0; i < points.size(); i ++) {
            if (points.size() == rows * columns) {
                return points.get(points.size() - 1).t;
            }
            Point current = points.get(i);
            if (current.x > 0 && grid.get(current.x - 1).get(current.y) == 0) {
                Point point = new Point();
                point.x = current.x - 1;
                point.y = current.y;
                point.t = current.t + 1;
                grid.get(current.x - 1).set(current.y, 1);
                points.add(point);
            }
            if (current.y > 0 && grid.get(current.x).get(current.y - 1) == 0) {
                Point point = new Point();
                point.x = current.x;
                point.y = current.y - 1;
                point.t = current.t + 1;
                grid.get(current.x).set(current.y - 1, 1);
                points.add(point);
            }
            if (current.x < rows - 1 && grid.get(current.x + 1).get(current.y) == 0) {
                Point point = new Point();
                point.x = current.x + 1;
                point.y = current.y;
                point.t = current.t + 1;
                grid.get(current.x + 1).set(current.y, 1);
                points.add(point);
            }
            if (current.y < columns - 1 && grid.get(current.x).get(current.y + 1) == 0) {
                Point point = new Point();
                point.x = current.x;
                point.y = current.y + 1;
                point.t = current.t + 1;
                grid.get(current.x).set(current.y + 1, 1);
                points.add(point);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(new Amazon().popularNFeatures(3, 2,
                Arrays.asList("battery", "calendar", "arm"), 3, Arrays.asList("more battery,,", "more arm,,,,", "more arm.")));
    }
}
