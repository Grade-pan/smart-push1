package com.kuding.message;

import java.util.Collection;

import com.kuding.content.ExceptionNotice;

public interface INoticeSendComponent {

	public void send(String blamedFor, ExceptionNotice exceptionNotice);

	public Collection<String> getAllBuddies();

}
