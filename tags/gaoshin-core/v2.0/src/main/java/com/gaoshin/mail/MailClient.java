package com.gaoshin.mail;

import java.util.ArrayList;

public interface MailClient {
    boolean send(MailRecord mail);

    ArrayList<MailMessage> read();
}
