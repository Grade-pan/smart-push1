package com.sed.message;

import java.util.Collection;

import com.sed.content.ExceptionNotice;

public interface INoticeSendComponent {

	public void send(String blamedFor, ExceptionNotice exceptionNotice);

	public Collection<String> getAllBuddies();

}
