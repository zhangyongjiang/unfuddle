package common.api.email;

public interface MailClient {
    boolean send(MailMessage mail);
}
