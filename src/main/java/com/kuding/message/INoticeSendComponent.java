package com.kuding.message;

import com.kuding.content.ExceptionNotice;

import java.util.Collection;

public interface INoticeSendComponent {

    public void send(String blamedFor, ExceptionNotice exceptionNotice);

    public Collection<String> getAllBuddies();

}
