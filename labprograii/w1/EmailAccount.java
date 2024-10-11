package labprograii.w1;
public class EmailAccount {
    private String emailAddress;
    private String password;
    private String fullName;
    private Email[] inbox;

    public EmailAccount(String emailAddress, String password, String fullName) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.fullName = fullName;
        this.inbox = new Email[50];
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean recibirEmail(Email em) {
        for (int i = 0; i < inbox.length; i++) {
            if (inbox[i] == null) {
                inbox[i] = em;
                return true;
            }
        }
        return false;
    }

    public void printInbox() {
        System.out.println("Cuenta: " + emailAddress);
        System.out.println("Usuario: " + fullName);
        System.out.println("\nCorreos recibidos:");
        
        int unreadCount = 0;
        int totalEmails = 0;
        
        for (int i = 0; i < inbox.length; i++) {
            if (inbox[i] != null) {
                totalEmails++;
                if (!inbox[i].isRead()) {
                    unreadCount++;
                }
                System.out.printf("POSICION %d - %s - %s - %s%n", 
                    i + 1, 
                    inbox[i].getSenderEmail(), 
                    inbox[i].getSubject(), 
                    inbox[i].isRead() ? "LEIDO" : "SIN LEER");
            }
        }
        
        System.out.println("\nCorreos sin leer: " + unreadCount);
        System.out.println("Total de correos: " + totalEmails);
    }

    public void leerEmail(int pos) {
        int index = pos - 1;
        if (index >= 0 && index < inbox.length && inbox[index] != null) {
            inbox[index].print();
            inbox[index].leido();
        } else {
            System.out.println("Correo No Existe");
        }
    }

    public void borrarLeidos() {
        for (int i = 0; i < inbox.length; i++) {
            if (inbox[i] != null && inbox[i].isRead()) {
                inbox[i] = null;
            }
        }
    }
}