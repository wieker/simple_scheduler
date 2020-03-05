package org.allesoft.simple_scheduler.scheduler;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class BingMatrix {
    public static void main(String[] args) {
        String url = "https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?key=" + System.getProperty("bing.api.key");
        String request = "{\n" +
                "    \"origins\": [{\n" +
                "        \"latitude\": 47.6044,\n" +
                "        \"longitude\": -122.3345\n" +
                "    },\n" +
                "    {\n" +
                "        \"latitude\": 47.6731,\n" +
                "        \"longitude\": -122.1185\n" +
                "    },\n" +
                "    {\n" +
                "        \"latitude\": 47.6149,\n" +
                "        \"longitude\": -122.1936\n" +
                "    }],\n" +
                "    \"destinations\": [{\n" +
                "        \"latitude\": 45.5347,\n" +
                "        \"longitude\": -122.6231\n" +
                "    }, \n" +
                "    {\n" +
                "        \"latitude\": 47.4747,\n" +
                "        \"longitude\": -122.2057\n" +
                "    }],\n" +
                "    \"travelMode\": \"driving\"\n" +
                "}";
        ResponseEntity<String> forEntity = new RestTemplate().exchange(url, HttpMethod.POST, new HttpEntity<>(request), String.class);
        System.out.println(forEntity.toString());
    }
}
