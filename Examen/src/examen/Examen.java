1. Echo Server (Servidor de eco)
Descripción: El servidor simplemente devuelve lo que recibe el cliente.

Código del servidor (EchoServer.java):

java
Copiar código
import java.io.*;
import java.net.*;

public class EchoServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor de eco en espera de conexiones...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                     
                    String clientMessage;
                    while ((clientMessage = in.readLine()) != null) {
                        System.out.println("Cliente: " + clientMessage);
                        out.println(clientMessage);  // Enviar el mismo mensaje al cliente
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
Código del cliente (EchoClient.java):

java
Copiar código
import java.io.*;
import java.net.*;

public class EchoClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String message;
            System.out.println("Escribe un mensaje para enviar al servidor: ");
            while ((message = userInput.readLine()) != null) {
                out.println(message);  // Enviar mensaje al servidor
                System.out.println("Servidor: " + in.readLine());  // Recibir respuesta
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
2. Cálculo de Operaciones Matemáticas
Descripción: El servidor evalúa la operación matemática que recibe del cliente.

Código del servidor (MathServer.java):

java
Copiar código
import java.io.*;
import java.net.*;

public class MathServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12346)) {
            System.out.println("Servidor de operaciones matemáticas en espera...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String operation;
                    while ((operation = in.readLine()) != null) {
                        try {
                            double result = evalOperation(operation);
                            out.println(result);
                        } catch (Exception e) {
                            out.println("Error en la operación");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double evalOperation(String operation) {
        String[] parts = operation.split("\\+|\\-|\\*|\\/");  // Se puede expandir para otras operaciones
        double num1 = Double.parseDouble(parts[0]);
        double num2 = Double.parseDouble(parts[1]);
        
        if (operation.contains("+")) return num1 + num2;
        else if (operation.contains("-")) return num1 - num2;
        else if (operation.contains("*")) return num1 * num2;
        else if (operation.contains("/")) return num1 / num2;
        else throw new IllegalArgumentException("Operación desconocida");
    }
}
Código del cliente (MathClient.java):

java
Copiar código
import java.io.*;
import java.net.*;

public class MathClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12346);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String operation;
            System.out.println("Introduce una operación matemática (ej. 5+3): ");
            while ((operation = userInput.readLine()) != null) {
                out.println(operation);  // Enviar operación al servidor
                System.out.println("Resultado: " + in.readLine());  // Recibir resultado
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
3. Transmisión de Archivos
Descripción: El servidor envía un archivo al cliente.

Código del servidor (FileServer.java):

java
Copiar código
import java.io.*;
import java.net.*;

public class FileServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12347)) {
            System.out.println("Servidor de archivos en espera de conexiones...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedInputStream fileInput = new BufferedInputStream(new FileInputStream("documento.txt"));
                     OutputStream out = clientSocket.getOutputStream()) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileInput.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    System.out.println("Archivo enviado al cliente.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
Código del cliente (FileClient.java):

java
Copiar código
import java.io.*;
import java.net.*;

public class FileClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12347);
             InputStream in = socket.getInputStream();
             FileOutputStream fileOutput = new FileOutputStream("received_file.txt")) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                fileOutput.write(buffer, 0, bytesRead);
            }
            System.out.println("Archivo recibido.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
4. Generación de Frases Aleatorias
Descripción: El servidor envía una frase aleatoria cada vez que se conecta el cliente.

Código del servidor (RandomPhraseServer.java):

java
Copiar código
import java.io.*;
import java.net.*;
import java.util.*;

public class RandomPhraseServer {
    private static final String[] PHRASES = {
        "La práctica hace al maestro.",
        "Más vale tarde que nunca.",
        "Quien mucho abarca, poco aprieta.",
        "La perseverancia es la clave del éxito."
    };

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12348)) {
            System.out.println("Servidor de frases aleatorias en espera...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                     
                    Random rand = new Random();
                    out.println(PHRASES[rand.nextInt(PHRASES.length)]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
Código del cliente (RandomPhraseClient.java):

java
Copiar código
import java.io.*;
import java.net.*;

public class RandomPhraseClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12348);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Frase del servidor: " + in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
5. Obtener la Hora del Servidor
Descripción: El servidor devuelve la hora actual.

Código del servidor (TimeServer.java):

java
Copiar código
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12349)) {
            System.out.println("Servidor de hora en espera...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    out.println("La hora actual es " + time);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
Código del cliente (TimeClient.java):

java
Copiar código
import java.io.*;
import java.net.*;

public class TimeClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12349);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
6. Conversión de Texto
Descripción: El servidor convierte el texto recibido a mayúsculas o minúsculas.

Código del servidor (TextConversionServer.java):

java
Copiar código
import java.io.*;
import java.net.*;

public class TextConversionServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12350)) {
            System.out.println("Servidor de conversión de texto en espera...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String text;
                    while ((text = in.readLine()) != null) {
                        out.println(text.toUpperCase());  // Conversión a mayúsculas
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
Código del cliente (TextConversionClient.java):

java
Copiar código
import java.io.*;
import java.net.*;

public class TextConversionClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12350);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String text;
            System.out.println("Escribe un texto para convertir: ");
            while ((text = userInput.readLine()) != null) {
                out.println(text);  // Enviar texto al servidor
                System.out.println("Texto convertido: " + in.readLine());  // Recibir respuesta
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
7. Consulta de Información del Sistema
Descripción: El servidor devuelve la información del sistema.

Código del servidor (SystemInfoServer.java):

java
Copiar código
import java.io.*;
import java.net.*;
import java.net.InetAddress;

public class SystemInfoServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12351)) {
            System.out.println("Servidor de información del sistema en espera...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    InetAddress inetAddress = InetAddress.getLocalHost();
                    String hostName = inetAddress.getHostName();
                    String ipAddress = inetAddress.getHostAddress();
                    String os = System.getProperty("os.name");

                    out.println("Host: " + hostName + ", IP: " + ipAddress + ", OS: " + os);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
Código del cliente (SystemInfoClient.java):

java
Copiar código
import java.io.*;
import java.net.*;

public class SystemInfoClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12351);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
