package org.allesoft.enterprise_db;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.ArrayList;
import java.util.List;

public class ConsumerBean implements MessageListener {
    private final List<Message> messages = new ArrayList<Message>();
    private boolean verbose;
    private String id = null;

    /**
     * Constructor.
     */
    public ConsumerBean() {
    }

    public ConsumerBean(String id) {
        this.id = id;
    }

    /**
     * @return all the messages on the list so far, clearing the buffer
     */
    public List<Message> flushMessages() {
        List<Message> answer = null;
        synchronized(messages) {
            answer = new ArrayList<Message>(messages);
            messages.clear();
        }
        return answer;
    }

    /**
     * Method implemented from MessageListener interface.
     *
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        synchronized (messages) {
            messages.add(message);
            if (verbose) {
            }
            messages.notifyAll();
        }
    }

    /**
     * Use to wait for a single message to arrive.
     */
    public void waitForMessageToArrive() {

        long start = System.currentTimeMillis();

        try {
            if (hasReceivedMessage()) {
                synchronized (messages) {
                    messages.wait(4000);
                }
            }
        } catch (InterruptedException e) {
        }
        long end = System.currentTimeMillis() - start;

    }

    /**
     * Used to wait for a message to arrive given a particular message count.
     *
     * @param messageCount
     */

    public void waitForMessagesToArrive(int messageCount){
        waitForMessagesToArrive(messageCount,120 * 1000);
    }
    public void waitForMessagesToArrive(int messageCount,long maxWaitTime) {
        long maxRemainingMessageCount = Math.max(0, messageCount - messages.size());
        long start = System.currentTimeMillis();
        long endTime = start + maxWaitTime;
        while (maxRemainingMessageCount > 0) {
            try {
                synchronized (messages) {
                    messages.wait(1000);
                }
                if (hasReceivedMessages(messageCount) || System.currentTimeMillis() > endTime) {
                    break;
                }
            } catch (InterruptedException e) {
            }
            maxRemainingMessageCount = Math.max(0, messageCount - messages.size());
        }
        long end = System.currentTimeMillis() - start;
    }

    public void assertMessagesArrived(int total) {
        waitForMessagesToArrive(total);
        synchronized (messages) {
            int count = messages.size();

        }
    }

    public void assertMessagesArrived(int total, long maxWaitTime) {
        waitForMessagesToArrive(total,maxWaitTime);
        synchronized (messages) {
            int count = messages.size();

        }
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Identifies if the message is empty.
     *
     * @return
     */
    protected boolean hasReceivedMessage() {
        return messages.isEmpty();
    }

    /**
     * Identifies if the message count has reached the total size of message.
     *
     * @param messageCount
     * @return
     */
    protected boolean hasReceivedMessages(int messageCount) {
        synchronized (messages) {
            return messages.size() >= messageCount;
        }
    }
}
