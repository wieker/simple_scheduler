package org.allesoft.enterprise_db.mqtt;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

public class MqttClient1 {
    public static void main(String[] args) throws Exception {
        MQTT mqtt = new MQTT();
        mqtt.setHost("localhost", 1883);

        BlockingConnection connection = mqtt.blockingConnection();
        connection.connect();

        connection.publish("foo", "Hello".getBytes(), QoS.AT_LEAST_ONCE, false);

        Topic[] topics = {new Topic("foo", QoS.AT_LEAST_ONCE)};
        byte[] qoses = connection.subscribe(topics);

        Message message = connection.receive();
        System.out.println(message.getTopic());
        byte[] payload = message.getPayload();
        // process the message then:
        message.ack();

        connection.disconnect();
    }
}
