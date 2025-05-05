package uv.poo.Task;

import uv.poo.Filter.ImageFilter;
import uv.poo.IO.ImageWriter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * FilterTask: Runnable que aplica un filtro a una imagen en memoria y luego
 * delega la escritura en disco a un pool de I/O.
 */
public class FilterTask implements Runnable {
    /**
     * Imagen original a procesar (antes de filtrar).
     */
    private final BufferedImage src;
    /**
     * Nombre base extraído de la URL (p.ej. "id3_5000x3333").
     */
    private final String baseName;
    /**
     * Extensión del archivo de salida (p.ej. "png").
     */
    private final String ext;
    /**
     * Filtro a aplicar (implementación de ImageFilter: sepia, bw, sharpen).
     */
    private final ImageFilter filter;
    /**
     * Pool de ExecutorService para operaciones de escritura en disco.
     */
    private final ExecutorService ioPool;

    /**
     * Constructor de FilterTask.
     *
     * @param src       BufferedImage original
     * @param baseName  nombre base para el archivo de salida
     * @param ext       extensión del archivo (sin punto)
     * @param filter    filtro a aplicar
     * @param ioPool    pool de hilos para escritura en disco
     */
    public FilterTask(BufferedImage src,
                      String baseName,
                      String ext,
                      ImageFilter filter,
                      ExecutorService ioPool) {
        this.src      = src;
        this.baseName = baseName;
        this.ext      = ext;
        this.filter   = filter;
        this.ioPool   = ioPool;
    }

    /**
     * Método que se ejecuta al lanzar la tarea:
     * 1. Aplica el filtro a la imagen (operación CPU-bound).
     * 2. Crea un Runnable que escribe el resultado usando ImageWriter,
     *    y lo envía al pool de I/O para no bloquear el hilo de filtrado.
     */
    @Override
    public void run() {
        // Paso 1: aplicar el filtro
        BufferedImage out = filter.apply(src);

        // Paso 2: delegar la escritura en disco a otro hilo
        ioPool.submit(() -> {
            try {
                // ImageWriter escribe la imagen filtrada en el directorio configurado
                ImageWriter.write(out,
                        baseName,
                        filter.name(),
                        ext);
            } catch (IOException e) {
                // En caso de error al escribir, informar por consola de error
                System.err.println("Error guardando "
                        + filter.name()
                        + ": "
                        + e.getMessage());
            }
        });
    }
}
