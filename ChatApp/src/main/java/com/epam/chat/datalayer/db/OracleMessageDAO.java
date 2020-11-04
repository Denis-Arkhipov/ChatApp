package com.epam.chat.datalayer.db;

import java.util.List;

import com.epam.chat.datalayer.MessageDAO;
import com.epam.chat.datalayer.dto.Message;

public class OracleMessageDAO implements MessageDAO {
    private SQLHendler helper = new SQLHendler();

    @Override
    public void sendMessage(Message message) {
	helper.addMessageDB(message);
    }

    @Override
    public List<Message> getLast(int count) {
	return helper.getLastMessages(count);
    }

}
