import java.io.*;
import java.net.*;

public class Server{

    static  Socket clientSocket = null;
    static  ServerSocket serverSocket = null;
    static  clientThread t[] = new clientThread[10];
    static int curr = 0;



    public static void main(String args[]) {
        int port_number=7777;

        System.out.println("Сервер слушает порт "+port_number);

        try {
            serverSocket = new ServerSocket(port_number);
        }
        catch (IOException e)
        {System.out.println(e);}

        while(true){
            try {
                clientSocket = serverSocket.accept();
                for(int i=0; i<=9; i++){
                    if(t[i]==null)
                    {
                        t[i] = new clientThread(clientSocket,t);
                        t[i].start();
                        break;
                    }
                }
            }
            catch (IOException e) {
                System.out.println(e);}
        }
    }
}

class clientThread extends Thread{

    DataInputStream is = null;
    DataOutputStream os = null;
    Socket clientSocket = null;
    String name = null;
    clientThread t[];
    File file =null;
    FileReader fr=null;
    BufferedReader reader=null;

    public clientThread(Socket clientSocket, clientThread[] t){
        this.clientSocket=clientSocket;
        this.name = this.clientSocket.toString();
        this.t=t;
        System.out.println("Новый клиент: " + this.name);
    }

    public void run()
    {
        String msg = null;
        try{
            file = new File("numbers.txt");
            fr = new FileReader(file);
            reader = new BufferedReader(fr);
            os = new DataOutputStream(clientSocket.getOutputStream());
            int k=0;

            is = new DataInputStream(clientSocket.getInputStream());

            for(int j=0;j<10;++j) {

                if(j==0){
                    k=Integer.valueOf(reader.readLine());
                    os.writeUTF("Привет, текущее число: " + k + "\nВведите ваше число и опреацию, например: '+7'");
                }
                msg = is.readUTF();
                if(msg.equals("exit")) {
                    System.out.println("Клиент " + name + " закрыл соединение");
                    os.writeUTF("stop");
                    break;
                }else {
                    float result = 0;
                    boolean isOk = true;
                    try{
                        int num = Integer.parseInt(msg.substring(1));
                        String operation = String.valueOf(msg.charAt(0));

                        switch (operation) {
                            case "+":
                                result = k + num;
                                break;
                            case "-":
                                result = k - num;
                                break;
                            case "*":
                                result = k * num;
                                break;
                            case "/":
                                result = (float)k / (float)num;
                                break;
                            case "%":
                                result = k % num;
                                break;
                            case "^":
                                double res=Math.pow(k,num);
                                result = (float)res;
                                break;
                        }
                        Server.curr++;
                    }catch (Exception ex){
                        isOk = false;

                        ex.printStackTrace();
                    }

                    if(isOk){
                        int y=k;
                        k=Integer.valueOf(reader.readLine());
                        os.writeUTF(y + msg + " = " + result+ "\n\tСлудующее число: " + k);
                    }
                    else
                        os.writeUTF("неверная команда");
                }

                os.flush();
            }

            for(int i=0; i<=9; i++)
                if (t[i]==this)
                    t[i]=null;

            is.close();
            os.close();
            clientSocket.close();
        }
        catch(IOException e){};
    }
}