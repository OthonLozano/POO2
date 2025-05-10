package mx.uv.fiee.iinf.paradigmas.networks;

import mx.uv.fiee.iinf.paradigmas.networks.models.Persona;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Receiver: clase que establece una conexión TLS segura
 * y recibe objetos serializados de tipo Persona.
 */
public class Receiver {
    // Dirección y puerto del servidor al que nos conectamos
    static String HOST = "localhost";
    static int PORT = 19000;

    public static void main(String[] args) throws Exception {
        // Crea la utilidad de socket y solicita recepción de objetos
        SocketUtils utils = new SocketUtils(HOST, PORT);
        utils.Receive();
    }

    /**
     * SocketUtils gestiona la conexión TLS y la lectura de datos.
     */
    private static class SocketUtils {
        // SSLSocket proporciona canal cifrado
        private final SSLSocket socket;

        /**
         * Constructor: carga truststore, configura TLS y establece conexión.
         * @param address dirección del servidor
         * @param port puerto de conexión
         * @throws Exception si ocurre error al configurar o conectar
         */
        public SocketUtils(String address, int port) throws Exception {
            // 1) Cargar el TrustStore que contiene el certificado del servidor
            KeyStore ts = KeyStore.getInstance("JKS");
            try (InputStream tsStream = getClass().getResourceAsStream("/client.truststore")) {
                ts.load(tsStream, "paradigmas".toCharArray());
            }

            // 2) Inicializar TrustManagerFactory para validar el certificado remoto
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ts);

            // 3) Crear el contexto SSL/TLS con los TrustManagers
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tmf.getTrustManagers(), null);

            // 4) Crear el SSLSocket seguro y conectarse al servidor
            SSLSocketFactory sf = ctx.getSocketFactory();
            socket = (SSLSocket) sf.createSocket(address, port);
            System.out.println("Connected to server via TLS");

            // 5) Forzar y verificar el handshake TLS
            socket.startHandshake();
            javax.net.ssl.SSLSession session = socket.getSession();

            // 6) Imprimir detalles de la sesión TLS negociada
            System.out.println("Protocolo y versión:  " + session.getProtocol());
            System.out.println("Cipher suite:         " + session.getCipherSuite());
        }

        /**
         * Receive: deserializa objetos Persona enviados por el servidor.
         */
        public void Receive() {
            try (
                    // ObjectInputStream envuelve el InputStream cifrado
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())
            ) {
                // Bucle de lectura hasta EOF o error
                while (true) {
                    try {
                        // Leer y castear al tipo Persona
                        Persona p = (Persona) ois.readObject();
                        // Imprimir UUID recibido
                        System.out.println("Received UUID: " + p.getUuid());
                    } catch (ClassNotFoundException e) {
                        // Si falta la clase Persona en el classpath
                        System.err.println("Class not found: " + e.getMessage());
                        break;
                    } catch (java.io.EOFException e) {
                        // Fin de flujo: servidor cerró conexión
                        System.err.println("End of stream reached.");
                        break;
                    }
                }
            } catch (IOException e) {
                // Errores de I/O al leer del socket
                e.printStackTrace();
            }
        }
    }
}
