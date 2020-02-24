package org.allesoft.simple_scheduler.acm_sgu_ru;

import java.util.*;
import java.util.stream.Collectors;

public class Algorithms {
    public ArrayList<String> findByWords(int numWords,
                                         int topWords,
                                         List<String> wordsToSearch,
                                         int numWhereToSearch,
                                         List<String> whereToSearch)
    {
        List<Set<String>> preprocessedText = whereToSearch.subList(0, numWhereToSearch).stream()
                .map(String::toLowerCase).map(s -> new HashSet<String>(Arrays.asList(s.split("[\\p{Punct}\\s]+")))).collect(Collectors.toList());

        List<String> preprocessedPatterns = wordsToSearch.stream()
                .map(String::toLowerCase).collect(Collectors.toList());
        Map<String, Integer> counter = new HashMap<>();
        preprocessedPatterns.forEach(e -> {
            counter.put(e, (int) preprocessedText.stream().filter(s -> s.contains(e)).count());
        });
        preprocessedPatterns.sort((s, t1) -> {
            int iComp = counter.get(s).compareTo(counter.get(t1));
            if (iComp == 0) {
                return s.compareTo(t1);
            } else {
                return -iComp;
            }
        });
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < topWords && i < numWords; i ++) {
            String o = preprocessedPatterns.get(i);
            Integer feature = counter.get(o);
            if (feature > 0) {
                // Return with the original case
                result.add(wordsToSearch.stream().filter(o::equalsIgnoreCase).findFirst().orElse("impossible"));
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

    int widthSearchLength(int rows, int columns, List<List<Integer>> matrix)
    {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < rows; i ++) {
            for (int j = 0; j < columns; j ++) {
                if (matrix.get(i).get(j) == 1) {
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
            if (current.x > 0 && matrix.get(current.x - 1).get(current.y) == 0) {
                Point point = new Point();
                point.x = current.x - 1;
                point.y = current.y;
                point.t = current.t + 1;
                matrix.get(current.x - 1).set(current.y, 1);
                points.add(point);
            }
            if (current.y > 0 && matrix.get(current.x).get(current.y - 1) == 0) {
                Point point = new Point();
                point.x = current.x;
                point.y = current.y - 1;
                point.t = current.t + 1;
                matrix.get(current.x).set(current.y - 1, 1);
                points.add(point);
            }
            if (current.x < rows - 1 && matrix.get(current.x + 1).get(current.y) == 0) {
                Point point = new Point();
                point.x = current.x + 1;
                point.y = current.y;
                point.t = current.t + 1;
                matrix.get(current.x + 1).set(current.y, 1);
                points.add(point);
            }
            if (current.y < columns - 1 && matrix.get(current.x).get(current.y + 1) == 0) {
                Point point = new Point();
                point.x = current.x;
                point.y = current.y + 1;
                point.t = current.t + 1;
                matrix.get(current.x).set(current.y + 1, 1);
                points.add(point);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(new Algorithms().findByWords(3, 2,
                Arrays.asList("some1", "some2", "some3"), 3, Arrays.asList("1111 some1,,", "1111 some2,,,,", "1111 some3.")));
    }
}
