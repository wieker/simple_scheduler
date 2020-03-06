package org.allesoft.simple_scheduler.scheduler;

import org.allesoft.simple_scheduler.GeoPoint;

import java.util.Date;

public interface Route {
    double distance();
    double time();

    GeoPoint from();
    GeoPoint to();

    Date date();
}
