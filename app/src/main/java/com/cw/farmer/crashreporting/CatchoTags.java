package com.cw.farmer.crashreporting;
public abstract class CatchoTags {

   public static final String REPORT = "REPORT";
    public static final String TITLE = "TITLE";
    public static final String EMAIL = "EMAIL";
    public static final String DESCRIPTION = "DESCRIPTION";
    public  static final String EMAIL_MODE = "EMAIL_MODE";
    public static final String RECIPIENT_EMAIL = "RECIPIENT_EMAIL";
    public static final String PASSWORD = "PASSWORD";
    public static final String SMTP_EMAIL = "SMTP";

    public enum EmailMode {

        G_MAIL_SENDER(1),
        DEFAULT(2);
        private final int value;

        EmailMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}