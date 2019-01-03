
import java.io.*;
import java.util.*;

public class TextUI implements Observer {

    private Client client;

    private boolean quit = false;

    public TextUI(Client client) {
        this.client = client;
    }

    public void ui() {
        Scanner sc = new Scanner(System.in);
        BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));
        String option, fileName;
        char c;
        String[] words;
        int num;
        String username, password;
        Scanner ler = new Scanner(System.in);

        System.out.println("\n=== BEM-VINDO! ===\n");

        while (true) {

            do {

                System.out.println();
                System.out.println("0 - Quit");
                System.out.println();
                System.out.println("1 - Register");
                System.out.println("2 - Login");
                System.out.println();
                System.out.print("> ");

                option = sc.next();

                if (option.length() >= 1) {
                    c = option.charAt(0);
                } else {
                    c = ' ';
                }

            } while (c < '0' || c > '2');

            switch (c) {

                case '0':
                    quit = true;
                    client.quit();
                    return;

                case '1':
                    boolean sucess = false;
                    System.out.println("Username: ");
                    username = ler.nextLine();
                    System.out.println("Password: ");
                    password = ler.nextLine();

                    sucess = client.register(username, password);
                    if (sucess == true) {
                        System.out.println("Registo efetuado com sucesso!");
                        uiLogin();
                    } else {
                        System.out.println("Registo falhou!");
                    }

                    return;

                case '2':

                    System.out.println("Username: ");
                    username = ler.nextLine();
                    System.out.println("Password: ");
                    password = ler.nextLine();

                    sucess = client.doLogin(username, password);

                    if (sucess == true) {
                        System.out.println("Registo efetuado com sucesso!");
                    } else {
                        System.out.println("Registo falhou!");
                    }

                    return;

                default:
                    return;

            }

        }

    }

    public void uiLogin() {
        Scanner sc = new Scanner(System.in);
        BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));
        String option, fileName;
        char c;
        String[] words;
        int num;
        String username, password;
        Scanner ler = new Scanner(System.in);

        System.out.println("\n=== BEM-VINDO! ===\n");

        while (true) {

            do {

                System.out.println();
                System.out.println("0 - Quit");
                System.out.println();
                System.out.println("1 - Send Message");
                System.out.println("2 - Request File");
                System.out.println();
                System.out.print("> ");

                option = sc.next();

                if (option.length() >= 1) {
                    c = option.charAt(0);
                } else {
                    c = ' ';
                }

            } while (c < '0' || c > '2');

            switch (c) {

                case '0':
                    quit = true;
                    client.quit();
                    return;

                case '1':
                    boolean sucess = false;
                    System.out.println("Message: ");
                    String msg = ler.nextLine();

                    client.sendMsg(msg);

                    return;

                case '2':

                    System.out.println("File Name: ");
                    fileName = ler.nextLine();
                    client.requestFileServer(fileName);
                default:
                    return;

            }

        }

    }

    @Override
    public void update(Observable o, Object arg) {
        List<String> list = (List<String>) arg;
        System.out.print("\n\n\n\n\n");
        System.out.print(list);
    }
}
