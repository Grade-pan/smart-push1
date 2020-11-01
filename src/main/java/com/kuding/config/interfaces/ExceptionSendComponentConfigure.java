package com.kuding.config.interfaces;

import com.kuding.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;
import org.springframework.boot.autoconfigure.mail.MailProperties;

public interface ExceptionSendComponentConfigure {

	 public void addSendComponent(ExceptionNoticeHandlerDecoration exceptionNoticeHandlerDecoration, MailProperties mailProperties);
}
