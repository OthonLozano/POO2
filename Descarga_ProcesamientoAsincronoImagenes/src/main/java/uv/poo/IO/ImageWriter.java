package uv.poo.IO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Clase responsable de guardar imágenes filtradas en disco,
 * asegurando que el directorio de salida exista.
 */
public class ImageWriter {
    // Directorio donde se guardarán las imágenes procesadas
    private static final Path OUTPUT_DIR = Path.of("imagenes_filtradas");

    /**
     * Escribe la imagen en el directorio OUTPUT_DIR con el nombre compuesto.
     * Crea el directorio si no existe.
     *
     * @param img        BufferedImage a guardar
     * @param baseName   nombre base extraído de la URL
     * @param filterName sufijo del filtro (bw, sepia, sharpen)
     * @param ext        extensión del archivo ("png")
     * @throws IOException si falla la creación de directorio o escritura de la imagen
     */
    public static void write(BufferedImage img,
                             String baseName,
                             String filterName,
                             String ext) throws IOException {
        // Asegurarse de que exista el directorio de salida
        if (Files.notExists(OUTPUT_DIR)) {
            // Crea directorios padres si es necesario
            Files.createDirectories(OUTPUT_DIR);
        }

        // Construir el nombre completo del archivo: baseName_filterName.ext
        // Ejemplo: id3_5000x3333_sepia.png
        Path outFile = OUTPUT_DIR.resolve(baseName + "_" + filterName + "." + ext);

        // Escribir la imagen en disco:
        // - img: objeto BufferedImage filtrado
        // - ext: formato de salida (png)
        // - outFile.toFile(): representa el archivo destino
        ImageIO.write(img, ext, outFile.toFile());

        // Confirmación por consola para saber que se guardó correctamente
        System.out.println("Guardado: " + outFile);
    }
}
