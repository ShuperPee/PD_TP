
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Client {
    
    
    
    
    public void requestFile(String fileName, File localDirectory, int ClientPort, Socket socketToClient, String addr){
        
        String localFilePath = null;
        FileOutputStream localFileOutputStream = null;
        PrintWriter pout;
        InputStream in;
        byte []fileChunk = new byte[5000]; // CRIAR VARIAVEL ESTATICA
        int nbytes;
        
        if(!localDirectory.exists()){
            System.out.println("A directoria " + localDirectory + " nao existe!");
            return;
        }
        
        if(!localDirectory.isDirectory()){
            System.out.println("O caminho " + localDirectory + " nao se refere a uma directoria!");
            return;
        }
        
        if(!localDirectory.canWrite()){
            System.out.println("Sem permissoes de escrita na directoria " + localDirectory);
            return;
        }
        
        try{
            
            try{

                localFilePath = localDirectory.getCanonicalPath()+File.separator+fileName;
                localFileOutputStream = new FileOutputStream(localFilePath);
                System.out.println("Ficheiro " + localFilePath + " criado.");

            }catch(IOException e){

                if(localFilePath == null){
                    System.out.println("Ocorreu a excepcao {" + e +"} ao obter o caminho canonico para o ficheiro local!");   
                }else{
                    System.out.println("Ocorreu a excepcao {" + e +"} ao tentar criar o ficheiro " + localFilePath + "!");
                }

                return;
            }

            try{
                
                socketToClient = new Socket(addr, ClientPort);
                
                socketToClient.setSoTimeout(5*1000); // CRIAR VARIAVEL ESTATICA TIMEOUT
                
                in = socketToClient.getInputStream();
                pout = new PrintWriter(socketToClient.getOutputStream(), true);
                
                pout.println(fileName);
                pout.flush();

                while((nbytes = in.read(fileChunk)) > 0){                    
                    //System.out.println("Recebido o bloco n. " + ++contador + " com " + nbytes + " bytes.");
                    localFileOutputStream.write(fileChunk, 0, nbytes);
                    //System.out.println("Acrescentados " + nbytes + " bytes ao ficheiro " + localFilePath+ ".");                    
                }                    
                
                System.out.println("Transferencia concluida.");

            }catch(UnknownHostException e){
                 System.out.println("Destino desconhecido:\n\t"+e);
            }catch(NumberFormatException e){
                System.out.println("O porto do servidor deve ser um inteiro positivo:\n\t"+e);
            }catch(SocketTimeoutException e){
                System.out.println("Não foi recebida qualquer bloco adicional, podendo a transferencia estar incompleta:\n\t"+e);
            }catch(SocketException e){
                System.out.println("Ocorreu um erro ao nível do socket TCP:\n\t"+e);
            }catch(IOException e){
                System.out.println("Ocorreu um erro no acesso ao socket ou ao ficheiro local " + localFilePath +":\n\t"+e);
            }
            
        }finally{
            
            if(socketToClient != null){
                try {
                    socketToClient.close();
                } catch (IOException ex) {}
            }
            
            if(localFileOutputStream != null){
                try{
                    localFileOutputStream.close();
                }catch(IOException e){}
            }
            
        }  
    }

    public static void main(String[] args) {
        InetAddress Addr;
        int Port;
        Socket s;
        if (args.length != 3) {
            System.out.println("Sintaxe: java Client Server_addr Server_port Dir");
            return;
        }
        try {
            Addr = InetAddress.getByName(args[0]);
            Port = Integer.parseInt(args[1]);
            s = new Socket(Addr, Port);
        } catch (UnknownHostException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }

    }
}
