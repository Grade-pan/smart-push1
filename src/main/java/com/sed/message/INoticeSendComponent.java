package com.sed.message;

import com.sed.content.ExceptionNotice;

import java.util.Collection;

public interface INoticeSendComponent {

    public void send(String blamedFor, ExceptionNotice exceptionNotice);

    public Collection<String> getAllBuddies();

}
