import java.io.*;
import java.util.*;

public class TextUI implements Observer{
    private Client client;
    
    private boolean quit = false;

    public TextUI(Client client) 
    {
        this.client = client;
    }
    
    public void ui()
    {
        Scanner sc = new Scanner(System.in);
        BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));
        String option, fileName;
        char c;
        String [] words;
        int num;
        String username, password;
        
        System.out.println("\n=== BEM-VINDO! ===\n");
  
        
        
        while (true){
            
            do{
                
                System.out.println();
                System.out.println("0 - Quit");
                System.out.println();
                System.out.println("1 - Register");
                System.out.println("2 - Login");
                System.out.println();
                System.out.print("> ");

                option = sc.next();
                
                if(option.length() >= 1)
                    c = option.charAt(0);
                else
                    c = ' ';
                
            }while(c < '0' || c > '2');
            
            switch(c){
                
                case '0':
                    quit = true;
                    return;

                case '1':
                    
                    System.out.println("Username: ");
                    Scanner ler = new Scanner(System.in);
                    username = ler.nextLine();
                    System.out.println("Password: ");
                    Scanner ler2 = new Scanner(System.in);
                    password = ler2.nextLine();
                    
                    return;
                
                case '2':
                    return;
                
                    
                    
                default:
                    return;
                    
            } 
            
        } 
    
    } 

    @Override
    public void update(Observable o, Object arg) {
        
    }
}
