package uv.poo.IO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * UrlReader: clase responsable de leer un archivo de texto que contiene URLs de imágenes
 * (una URL por línea) y devolverlas como una lista.
 *
 * Esta clase facilita la separación de la lógica de I/ de la lógica de procesamiento.
 */
public class UrlReader {

    /**
     * Lee todas las líneas del archivo especificado, filtra las líneas vacías o en blanco,
     * y devuelve la lista resultante.
     *
     * @param file ruta al archivo de texto con URLs, una URL por línea.
     * @return lista de cadenas con las URLs válidas (no vacías).
     * @throws IOException si ocurre un error al abrir o leer el archivo.
     */
    public static List<String> readUrls(Path file) throws IOException {
        // Files.lines abre un Stream<String> que lee el contenido del archivo línea a línea.
        // El Stream se cierra automáticamente al terminar la operación.
        return Files.lines(file)
                // Eliminar líneas en blanco o que solo contengan espacios
                // devuelve true si la línea NO está en blanco (tiene algún carácter distinto de espacio).
                .filter(line -> !line.isBlank())
                // Recolectar el Stream en una List de Strings
                .toList();
    }
}