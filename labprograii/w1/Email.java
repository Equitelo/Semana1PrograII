package labprograii.w1;

public class Email {
    private String senderEmail;
    private String subject;
    private String content;
    private boolean isRead;

    public Email(String senderEmail, String subject, String content) {
        this.senderEmail = senderEmail;
        this.subject = subject;
        this.content = content;
        this.isRead = false;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void leido() {
        this.isRead = true;
    }

    public void print() {
        System.out.println("DE: " + senderEmail);
        System.out.println("ASUNTO: " + subject);
        System.out.println(content);
    }
}
