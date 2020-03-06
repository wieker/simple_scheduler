package org.allesoft.simple_scheduler;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.allesoft.simple_scheduler.scheduler.Route;

import java.util.Date;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.allesoft.simple_scheduler.scheduler.SchedulerImpl.sqr;

public class CaffeineApp {
    public static void main(String[] args) {
        LoadingCache<Route, ConcurrentSkipListMap<Date, Route>> cache = Caffeine.newBuilder().maximumSize(100_000_000).build(
                key -> createTimeDependentCache(key)
        );

        System.out.println(cache.get(getRoute(getGeoPoint(0, 0), getGeoPoint(1, 1))).floorEntry(new Date()).getValue().distance());
        System.out.println(cache.get(getRoute(getGeoPoint(0, 0), getGeoPoint(1, 1))).floorEntry(new Date()).getValue().distance());
    }

    private static ConcurrentSkipListMap<Date, Route> createTimeDependentCache(Route route) {
        ConcurrentSkipListMap<Date, Route> dateRouteConcurrentSkipListMap = new ConcurrentSkipListMap<>();
        dateRouteConcurrentSkipListMap.put(route.date(), new Route() {
            @Override
            public double distance() {
                System.out.println("calculate");
                return Math.sqrt(
                        sqr(route.from().lat() - route.to().lat()) + sqr(route.from().lon() - route.to().lon()));
            }

            @Override
            public double time() {
                return 0;
            }

            @Override
            public GeoPoint from() {
                return route.from();
            }

            @Override
            public GeoPoint to() {
                return route.to();
            }

            @Override
            public Date date() {
                return route.date();
            }
        });
        return dateRouteConcurrentSkipListMap;
    }

    private static Route getRoute(final GeoPoint from, final GeoPoint to) {
        return new Route() {
            @Override
            public double distance() {
                return 0;
            }

            @Override
            public double time() {
                return 0;
            }

            @Override
            public GeoPoint from() {
                return from;
            }

            @Override
            public GeoPoint to() {
                return to;
            }

            @Override
            public Date date() {
                return new Date();
            }
        };
    }

    private static GeoPoint getGeoPoint(double lat, double lon) {
        return new GeoPoint() {
            @Override
            public double lat() {
                return lat;
            }

            @Override
            public double lon() {
                return lon;
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof GeoPoint) {
                    return lat() == ((GeoPoint) obj).lon() && lon() == ((GeoPoint) obj).lon();
                } else {
                    return false;
                }
            }
        };
    }
}
