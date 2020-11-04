package com.epam.chat.datalayer;

import java.util.List;

import com.epam.chat.datalayer.dto.Message;

/**
 * This interface describes methods to access data from DB or XML file
 */
public interface MessageDAO {

    /**
     * Send a message
     * 
     * @param message - message to send
     */
    void sendMessage(Message message);

    /**
     * Get last number of messages
     * 
     * @param count number of last messages to get (sorted by date)
     * @return
     */
    List<Message> getLast(int count);

}
