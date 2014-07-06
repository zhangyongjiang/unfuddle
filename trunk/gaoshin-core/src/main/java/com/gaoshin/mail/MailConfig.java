package com.gaoshin.mail;

public enum MailConfig {
    SmtpHost("smtp.1and1.com"),
    Pop3Host("pop.1and1.com"),
    InboxUserName("*@openandgreen.com"),
    InboxPassword("starword"),
    OutboxUserName("gao@openandgreen.com"),
    OutboxPassword("wordpass"),
    DefaultFrom("gao@openandgreen.com"),
    Company("openandgreen.com"),
    Port("587");

    private String defaultValue = null;

    private MailConfig() {
    }

    private MailConfig(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

}
