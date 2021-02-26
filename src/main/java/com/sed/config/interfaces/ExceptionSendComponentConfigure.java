package com.sed.config.interfaces;

import com.sed.exceptionhandle.interfaces.ExceptionNoticeHandlerDecoration;
import org.springframework.boot.autoconfigure.mail.MailProperties;

public interface ExceptionSendComponentConfigure {

    public void addSendComponent(ExceptionNoticeHandlerDecoration exceptionNoticeHandlerDecoration, MailProperties mailProperties);
}
