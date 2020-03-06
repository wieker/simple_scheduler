package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.GeoPoint;

public interface Route {
    double distance();
    double time();

    GeoPoint from();
    GeoPoint to();
}
