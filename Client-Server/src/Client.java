import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] ar) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Введите IP: ");
        String address = sc.nextLine();// это IP-адрес компьютера, где исполняется наша серверная программа.
        System.out.print("Введите порт: ");
        int serverPort = Integer.parseInt(sc.nextLine());

        try {
            InetAddress ipAddress = InetAddress.getByName(address); // создаем объект который отображает вышеописанный IP-адрес.
            Socket socket = new Socket(ipAddress, serverPort); // создаем сокет используя IP-адрес и порт сервера.
            System.out.println("Клиент запущен!");

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String line = null;
            boolean first_time = true;

            while(true){
                if(first_time)
                    System.out.println(in.readUTF());

                System.out.println("Введите команду");
                line = sc.nextLine(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.
                System.out.println("Отправка сообщения '" + line + "'  на сервер...");
                out.writeUTF(line); // отсылаем введенную строку текста серверу.
                out.flush(); // заставляем поток закончить передачу данных.
                line = in.readUTF(); // ждем пока сервер отошлет строку текста.
                System.out.println("Сервер ответил : " + line);

                first_time = false;

                if(line.equals("stop"))
                    break;
            }

            in.close();
            out.close();
            socket.close();
            System.exit(0);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}