package org.allesoft.simple_scheduler;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.allesoft.simple_scheduler.scheduler.structure.Route;
import org.allesoft.simple_scheduler.scheduler.model.GeoPoint;

import java.util.Date;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.allesoft.simple_scheduler.scheduler.SchedUtils.sqr;

public class CaffeineApp {
    public static void main(String[] args) {
        LoadingCache<Route, ConcurrentSkipListMap<Date, Route>> cache = Caffeine.newBuilder().maximumSize(100_000_000).build(
                key -> createTimeDependentCache(key)
        );

        System.out.println(cache.get(getRoute(getGeoPoint(0, 0), getGeoPoint(1, 1), 0, 0)).floorEntry(new Date()).getValue().distance());
        System.out.println(cache.get(getRoute(getGeoPoint(0, 0), getGeoPoint(1, 1), 0, 0)).floorEntry(new Date()).getValue().distance());
    }

    private static ConcurrentSkipListMap<Date, Route> createTimeDependentCache(Route route) {
        ConcurrentSkipListMap<Date, Route> dateRouteConcurrentSkipListMap = new ConcurrentSkipListMap<>();
        System.out.println("Top level cache miss");
        dateRouteConcurrentSkipListMap.put(route.date(), getCalculatedRoute(route));
        return dateRouteConcurrentSkipListMap;
    }

    private static Route getCalculatedRoute(Route route) {
        return getRoute(route.from(), route.to(), Math.sqrt(
                    sqr(route.from().getLat() - route.to().getLat()) + sqr(route.from().getLon() - route.to().getLon())), 0);
    }

    private static Route getRoute(final GeoPoint from, final GeoPoint to, final double distance, final int time) {
        return new Route() {
            @Override
            public double distance() {
                return distance;
            }

            @Override
            public double time() {
                return time;
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
            public double getLat() {
                return lat;
            }

            @Override
            public double getLon() {
                return lon;
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof GeoPoint) {
                    return getLat() == ((GeoPoint) obj).getLon() && getLon() == ((GeoPoint) obj).getLon();
                } else {
                    return false;
                }
            }
        };
    }
}
