package org.allesoft.enterprise_db.xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;

public class Client {
    public static void main(String[] args) throws Exception {
        XMPPTCPConnectionConfiguration.Builder conf = XMPPTCPConnectionConfiguration.builder();
        conf.setUsernameAndPassword("user", "pass");
        conf.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        conf.setXmppDomain("localhost");
        conf.setCompressionEnabled(true);

        TLSUtils.acceptAllCertificates(conf);
        XMPPTCPConnectionConfiguration config = conf.build();
        XMPPTCPConnection connection = new XMPPTCPConnection(config);
        connection = connection;


        connection.connect();

        connection.login();
        connection.addAsyncStanzaListener(packet -> System.out.println(packet.getFrom()), stanza -> true);

        for (;;) {
            Message m = new Message("user2@debian", "test");
            m.setType(Message.Type.chat);
            m.setBody("hi");
            connection.sendStanza(m);
            Thread.sleep(2000);
        }
    }



}
