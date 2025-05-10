package mx.uv.fiee.iinf.paradigmas.networks;

import mx.uv.fiee.iinf.paradigmas.networks.models.Persona;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;
import java.util.Random;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.KeyManagerFactory;

/**
 * Sender: clase que crea objetos Persona, los serializa y envía
 * a través de un canal TLS cifrado.
 */
public class Sender {
    // Puerto donde el servidor TLS escuchará las conexiones
    static int PORT = 19000;

    /**
     * Punto de entrada: genera instancias de Persona y las envía continuamente.
     */
    public static void main(String[] args) throws Exception {
        // Inicializa la utilidad de socket TLS
        SocketUtils utils = new SocketUtils();
        // Generador de valores aleatorios para nombre y edad
        Random random = new Random();

        // Flujos para serializar objetos a bytes antes de enviarlos
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        // Bucle infinito: crea, serializa y envía un objeto Persona cada segundo
        while (true) {
            // 1) Crear y poblar un objeto Persona con datos aleatorios
            Persona p = new Persona();
            p.setFullname("Random Name " + random.nextInt(1000));
            p.setAge(random.nextInt(55));
            p.setUuid(UUID.randomUUID().toString());

            // 2) Serializar el objeto en el ByteArrayOutputStream
            oos.writeObject(p);
            oos.flush();

            // 3) Obtener el arreglo de bytes serializados
            byte[] bytes = baos.toByteArray();
            System.out.println("Sending object with uuid: " + p.getUuid());

            // 4) Enviar los bytes cifrados a través del socket TLS
            utils.Send(bytes);

            // 5) Limpiar el buffer para la próxima iteración
            baos.reset();
        }
    }

    /**
     * SocketUtils: clase interna que configura el servidor TLS
     * y envía datos por medio de un SSLSocket.
     */
    private static class SocketUtils {
        // SSLSocket protege los datos con TLS
        private final SSLSocket socket;

        /**
         * Constructor: carga el keystore, configura TLS y acepta una conexión.
         */
        SocketUtils() throws Exception {
            // 1) Cargar el KeyStore que contiene la clave privada y el certificado
            KeyStore ks = KeyStore.getInstance("JKS");
            try (InputStream ksStream = getClass().getResourceAsStream("/server.keystore")) {
                ks.load(ksStream, "paradigmas".toCharArray());
            }

            // 2) Inicializar KeyManagerFactory con el KeyStore
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, "paradigmas".toCharArray());

            // 3) Crear contexto SSL con los KeyManagers
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(kmf.getKeyManagers(), null, null);

            // 4) Crear un SSLServerSocketFactory y abrir el servidor en el puerto
            SSLServerSocketFactory ssf = ctx.getServerSocketFactory();
            SSLServerSocket sslServerSocket = (SSLServerSocket) ssf.createServerSocket(PORT);
            sslServerSocket.setEnabledProtocols(new String[]{"TLSv1.2", "TLSv1.3"});
            System.out.println("Waiting for a TLS connection...");

            // 5) Aceptar la conexión entrante de forma cifrada
            SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();

            // 6) Fuerza el handshake TLS y muestra detalles de la sesión
            sslSocket.startHandshake();
            javax.net.ssl.SSLSession session = sslSocket.getSession();
            System.out.println("Protocolo y versión: " + session.getProtocol());
            System.out.println("Cipher suite:        " + session.getCipherSuite());

            // 7) Asignar el socket para su uso en el envío de datos
            this.socket = sslSocket;
        }

        /**
         * Envía un arreglo de bytes a través del canal TLS.
         * @param data bytes serializados del objeto Persona
         */
        void Send(byte[] data) throws IOException {
            try {
                // Informar longitud y enviar
                System.out.println("Data length: " + data.length);
                socket.getOutputStream().write(data, 0, data.length);
                socket.getOutputStream().flush();
                // Pausa de 1 segundo entre envíos
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error sending data");
            } catch (InterruptedException ie) {
                throw new RuntimeException("Error sending data");
            }
        }
    }
}
