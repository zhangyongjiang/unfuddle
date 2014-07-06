package common.api.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class MailClientImpl implements MailClient {
    private static final Logger logger = LoggerFactory.getLogger(MailClientImpl.class);

    @Value ("${mail.smtpHost}") private String smtpHost = "smtp.gmail.com";
    @Value ("${mail.pop3Host}") private String pop3Host = "pop.gmail.com";
    @Value ("${mail.inboxUserName}") private String inboxUserName = "kevin@onsalelocal.com";
    @Value ("${mail.inboxPassword}") private String inboxPassword = "r3dmine!";
    @Value ("${mail.outboxUserName}") private String outboxUserName = "kevin@onsalelocal.com";
    @Value ("${mail.outboxPassword}") private String outboxPassword = "r3dmine!";
    @Value ("${mail.defaultFrom}") private String defaultFrom = "Onsale Local";
    @Value ("${mail.port}") private int port = 587;

    public boolean send(MailMessage mail) {
        boolean sent = false;
        try {
            String from = mail.getFrom();
            if ((from == null) || (from.length() == 0)) {
                from = defaultFrom;
            }
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", getSmtpHost());
            properties.setProperty("mail.smtp.submitter", inboxUserName);
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.debug", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.setProperty("mail.smtp.port", port + "");
            Session session = Session.getInstance(properties, new Authenticator(outboxUserName, outboxPassword));

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            for (String to : mail.getTo()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            }

            message.setSubject(mail.getSubject());

            if (mail.isHtml()) {
                message.setContent(mail.getContent(), "text/html");
            } else {
                message.setContent(mail.getContent(), "text/plain");
            }

            // Send message
            Transport.send(message);

            sent = true;
        } catch (Exception e) {
            logger.error("can't send mail - " + e.getMessage());
        }

        return sent;
    }

    private class Authenticator extends javax.mail.Authenticator {
        private final PasswordAuthentication authentication;

        public Authenticator(String userName, String password) {
            authentication = new PasswordAuthentication(userName, password);
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }
    }

    public void setDefaultFrom(String defaultFrom) {
        this.defaultFrom = defaultFrom;
    }

    public String getDefaultFrom() {
        return defaultFrom;
    }

    public String getUserName() {
        return inboxUserName;
    }

    public void setUserName(String userName) {
        this.inboxUserName = userName;
    }

    public String getPassword() {
        return inboxPassword;
    }

    public void setPassword(String password) {
        this.inboxPassword = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    private String getPlainText(Message msg) throws IOException, MessagingException {
        Object content = msg.getContent();
        if (content instanceof MimeMultipart) {
            return getPlainText((MimeMultipart) content);
        } else {
            return content.toString();
        }

    }

    private String getPlainText(MimeMultipart mmp) throws IOException, MessagingException {
        StringBuilder sb = new StringBuilder();
        getPlainText(sb, mmp);
        return sb.toString();
    }

    private void getPlainText(StringBuilder sb, MimeMultipart mmp) throws IOException, MessagingException {
        for (int i = 0; i < mmp.getCount(); i++) {
            BodyPart body = mmp.getBodyPart(i);
            String contentType = body.getContentType();
            if (contentType.indexOf("text/plain") != -1) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(body.getContent().toString());
            } else if (contentType.indexOf("multipart") != -1) {
                getPlainText(sb, (MimeMultipart) body.getContent());
            }
        }
    }

    private MailMessage buildMailMessage(Message msg) throws MessagingException, IOException {
        MailMessage mm = new MailMessage();
        InternetAddress addr = (InternetAddress) msg.getFrom()[0];
        mm.setFrom(addr.getAddress());
        mm.setUserName(addr.getPersonal());
        InternetAddress to = (InternetAddress) msg.getRecipients(RecipientType.TO)[0];
        mm.setToString(to.getAddress());
        mm.setSubject(msg.getSubject());
        mm.setContent(getPlainText(msg));
        return mm;
    }
    
    public ArrayList<MailMessage> read(boolean delete) {
        ArrayList<MailMessage> mailList = new ArrayList<MailMessage>();
        Properties properties = new Properties();
        properties.setProperty("mail.pop3.host", getPop3Host());
		String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		properties.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		properties.setProperty("mail.pop3.socketFactory.fallback", "false");
		properties.setProperty("mail.pop3.port", "995");
		properties.setProperty("mail.pop3.socketFactory.port", "995");
        Session session = Session.getInstance(properties, new Authenticator(inboxUserName, inboxPassword));
        try {
            Store store = session.getStore("pop3");
            store.connect();
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            Message[] messageList = folder.getMessages();
            for (Message msg : messageList) {
                MailMessage mm = buildMailMessage(msg);
                mailList.add(mm);
                if(delete)
                	msg.setFlag(Flag.DELETED, true);

                logger.trace("email received from " + mm.getFrom() + " - " + mm.getSubject() + " -- to: "
                        + mm.getToString());
            }
            folder.close(true);
            store.close();
        } catch (Exception e) {
            logger.error("cannot read email. " + e.getMessage());
        }
        return mailList;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setPop3Host(String pop3Host) {
        this.pop3Host = pop3Host;
    }

    public String getPop3Host() {
        return pop3Host;
    }

    public static void main(String[] args) throws Exception {
        MailClientImpl client = new MailClientImpl();
//        ArrayList<MailMessage> msgList = client.read();
//        for (MailMessage msg : msgList) {
//            System.out.println(msg.getUserName());
//            System.out.println(msg.getFrom());
//            System.out.println(msg.getSubject());
//            System.out.println(msg.getContent());
//        }
//        MailMessage mm = new MailMessage("kevin@raved.com", "test msg from msg server", "hello world");
//        client.send(mm);
        
        client.read(false);
        Thread.sleep(10000);
    }

}
