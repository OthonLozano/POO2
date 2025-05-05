package uv.poo.Download;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Encargada de descargar imágenes desde una URL y extraer un nombre base para el archivo.
 */
public class ImageDownloader {

    /**
     * Descarga una imagen desde la URL proporcionada.
     *
     * @param urlStr URL de la imagen
     * @return BufferedImage descargada
     * @throws IOException si ocurre un error de lectura
     */
    public static BufferedImage download(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        return ImageIO.read(url);
    }

    /**
     * Genera un nombre base a partir de la ruta de la URL.
     * Si la ruta es del tipo /id/{id}/{width}/{height}, retorna "id{id}_{width}x{height}";
     * en caso contrario, usa el último segmento de la ruta o "image" si está vacío.
     *
     * @param urlStr URL de la imagen
     * @return nombre base
     */
    public static String extractBaseName(String urlStr) {
        try {
            URI uri = new URI(urlStr);
            String[] parts = uri.getPath().split("/");
            if (parts.length >= 5 && "id".equals(parts[1])) {
                return "id" + parts[2] + "_" + parts[3] + "x" + parts[4];
            }
        } catch (URISyntaxException e) {
            // Si la URL no cumple el formato URI esperado, registramos la advertencia y usamos el fallback
            System.err.println("Aviso: URI inválida '" + urlStr + "': " + e.getMessage());
        }
        return null;
    }
}
