package org.allesoft.simple_scheduler.scheduler;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;

public class GeoMatrix {
    public static void main(String[] args) throws Exception {
        GeoApiContext apiContext = new GeoApiContext.Builder()
                .apiKey("")
                .build();

        DistanceMatrixApiRequest req= DistanceMatrixApi.newRequest(apiContext);
        DistanceMatrix t=req.origins("Estonia").destinations("London")
                .mode(TravelMode.DRIVING).await();
        System.out.println(t.rows[0].elements[0].distance);
    }
}
