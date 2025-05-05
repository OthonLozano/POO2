package uv.poo;

import uv.poo.Config.ExecutorConfig;
import uv.poo.IO.UrlReader;
import uv.poo.Download.ImageDownloader;
import uv.poo.Filter.ImageFilter;
import uv.poo.Filter.SepiaFilter;
import uv.poo.Filter.BlackAndWhiteFilter;
import uv.poo.Filter.SharpenFilter;
import uv.poo.Task.FilterTask;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * App: Main de la aplicación.
 * Fases:
 * 1. Configuración de pools de hilos para descarga, filtrado y I/O.
 * 2. Lectura de URLs desde un archivo urls.txt.
 * 3. Envío de tareas de descarga al pool downloadPool.
 *    - Dentro de cada tarea de descarga:
 *      a) Descargar la imagen.
 *      b) Extraer un nombre base para el archivo.
 *      c) Crear las instancias de filtros.
 *      d) Enviar para cada filtro una FilterTask al pool filterPool.
 * 4. Al finalizar el envío de todas las tareas, apagar ordenadamente todos los pools.
 */
public class App {
    public static void main(String[] args) throws IOException {
        // 1. Inicializar los pools de hilos: descarga, filtrado, y escritura en disco.
        var executors = ExecutorConfig.createExecutors();

        // 2. Leer el archivo de URLs (una URL por línea).
        List<String> urls = null;
        try {
            // Path.of("urls.txt") busca el archivo en el directorio de trabajo
            urls = UrlReader.readUrls(Path.of("urls.txt"));
        } catch (IOException e) {
            // Si falla la lectura, envolvemos en RuntimeException para parar la ejecución
            throw new RuntimeException("No se pudo leer urls.txt", e);
        }

        // 3. Para cada URL, crear y enviar una tarea al pool de descargas
        for (String url : urls) {
            executors.downloadPool.submit(() -> {
                try {
                    // 3.a) Descargar la imagen desde la URL
                    BufferedImage original = ImageDownloader.download(url);
                    // 3.b) Extraer un nombre base para el archivo filtrado
                    String baseName = ImageDownloader.extractBaseName(url);

                    // 3.c) Definir los filtros a aplicar
                    ImageFilter[] filters = {
                            new SepiaFilter(),         // tono sepia
                            new BlackAndWhiteFilter(), // escala de grises
                            new SharpenFilter()        // enfoque
                    };

                    // 3.d) Para cada filtro, enviar una FilterTask al pool filterPool
                    for (ImageFilter filter : filters) {
                        // FilterTask aplica el filtro y delega la escritura en ioPool
                        executors.filterPool.submit(
                                new FilterTask(
                                        original,
                                        baseName,
                                        "png",     // extensión de salida
                                        filter,
                                        executors.ioPool
                                )
                        );
                    }
                } catch (Exception e) {
                    // Captura cualquier error en la fase de descarga o filtrado inicial
                    System.err.println("Error procesando URL " + url + ": " + e.getMessage());
                }
            });
        }

        // 4. Después de enviar todas las tareas, solicitar un apagado ordenado de los pools
        //    Esto espera a que todas las tareas pendientes terminen, con timeouts y shutdownNow.
        ExecutorConfig.shutdownAll(executors);
    }
}