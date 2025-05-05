package uv.poo.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ExecutorConfig: configura y gestiona tres pools de hilos para distintas fases del flujo:
 *  - downloadPool: descarga de imágenes (I/O de red)
 *  - filterPool: aplicación de filtros a las imágenes (procesamiento en CPU)
 *  - ioPool: escritura de imágenes en disco (I/O de archivos)
 *
 * Proporciona métodos para crear estos pools y apagarlos correctamente al finalizar.
 */
public class ExecutorConfig {

    /**
     * Pool de hilos para descargas: permite hasta 4 descargas concurrentes,
     * suficiente para un throughput moderado sin saturar la red.
     */
    public final ExecutorService downloadPool;

    /**
     * Pool de hilos para el filtrado de imágenes: 4 hilos para procesar varias imágenes en paralelo,
     * aprovechando múltiples núcleos de CPU.
     */
    public final ExecutorService filterPool;

    /**
     * Pool de hilos para operaciones de escritura en disco: 2 hilos para evitar exceso de I/O de archivos,
     * equilibrando concurrencia y rendimiento de disco.
     */
    public final ExecutorService ioPool;

    /**
     * Constructor privado: inicializa los tres pools con tamaños de hilo fijos.
     * Usamos Executors.newFixedThreadPool para un número limitado y controlado de hilos.
     *
     */
    private ExecutorConfig() {
        this.downloadPool = Executors.newFixedThreadPool(4);
        this.filterPool   = Executors.newFixedThreadPool(4);
        this.ioPool       = Executors.newFixedThreadPool(2);
    }

    /**
     * Crea y devuelve una nueva instancia de ExecutorConfig,
     * con los pools ya inicializados.
     *
     * @return instancia de configuración de ejecutores.
     */
    public static ExecutorConfig createExecutors() {
        return new ExecutorConfig();
    }

    /**
     * Apaga todos los pools de hilos en orden:
     * 1. Intenta un apagado amable (shutdown), esperando un tiempo límite.
     * 2. Si no finaliza tras el timeout, fuerza un apagado inmediato (shutdownNow).
     *
     * @param cfg configuración que contiene los pools a cerrar.
     */
    public static void shutdownAll(ExecutorConfig cfg) {
        shutdownPool(cfg.downloadPool);
        shutdownPool(cfg.filterPool);
        shutdownPool(cfg.ioPool);
    }

    /**
     * Apaga un ExecutorService siguiendo estos pasos:
     * 1. shutdown(): no acepta nuevas tareas, pero deja terminar las en curso.
     * 2. awaitTermination(60s): espera hasta 60 segundos a que acaben.
     * 3. shutdownNow(): cancela tareas pendientes.
     * 4. awaitTermination(60s) de nuevo: espera a que los hilos respondan.
     *
     * @param pool el ExecutorService a apagar.
     */
    private static void shutdownPool(ExecutorService pool) {
        // Paso 1: no aceptar nuevas tareas
        pool.shutdown();
        try {
            // Paso 2: espera un tiempo límite para que terminen las tareas en curso
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                // Paso 3: fuerza la cancelación de tareas pendientes
                pool.shutdownNow();
                // Paso 4: espera de nuevo a que los hilos finalicen tras el shutdownNow
                if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("El pool no pudo terminar");
                }
            }
        } catch (InterruptedException ie) {
            // Si alguien interrumpe el hilo de gestión, forzar shutdown inmediato
            pool.shutdownNow();
            // Restaurar el estado de interrupción del hilo
            Thread.currentThread().interrupt();
        }
    }
}
