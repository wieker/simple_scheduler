package org.allesoft.simple_scheduler;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.allesoft.simple_scheduler.scheduler.Route;

import static org.allesoft.simple_scheduler.scheduler.SchedulerImpl.sqr;

public class CaffeineApp {
    public static void main(String[] args) {
        LoadingCache<Route, Route> cache = Caffeine.newBuilder().maximumSize(100_000_000).build(
                key -> new Route() {
                    @Override
                    public double distance() {
                        System.out.println("calculate");
                        return Math.sqrt(
                                sqr(key.from().lat() - key.to().lat()) + sqr(key.from().lon() - key.to().lon()));
                    }

                    @Override
                    public double time() {
                        return 0;
                    }

                    @Override
                    public GeoPoint from() {
                        return key.from();
                    }

                    @Override
                    public GeoPoint to() {
                        return key.to();
                    }
                }
        );

        System.out.println(cache.get(getRoute(getGeoPoint(0, 0), getGeoPoint(1, 1))).distance());
        System.out.println(cache.get(getRoute(getGeoPoint(0, 0), getGeoPoint(1, 1))).distance());
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
