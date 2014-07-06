package common.api.email;

import java.util.ArrayList;

public interface MailClient {
    boolean send(MailMessage mail);
	ArrayList<MailMessage> read(boolean delete);
}
