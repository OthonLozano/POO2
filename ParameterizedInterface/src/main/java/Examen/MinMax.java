package Examen;

//Hacemos que esta clase sea parametrizada utilizando la clase Comparable<T> y asimismo hacemos que esta clase
//implemente la interfaz parametrizada IMaxMin<T>
public class MinMax<T extends Comparable<T>> implements IMaxMin<T> {
    public T[] values; //Creamos un arreglo donde se almacenaran los valores que nos pasen como parametro

    // Se crea un constructor
    public MinMax(T[] values) {
        this.values = values;
    }

    // Sobrescribimos el metodo de la interfaz 'public T min()' el cual nos devolverá el valor minimo,
    // primero verificando si el arreglo no esta vacio o si la longitud es igual a 0, si sucede eso retorna null, si no
    // para a realizar una iteración sobre cada uno de los elementos del arreglo y utilizando el metodo compareTo
    // para evaluar cada uno de los valores, si son menores a esa posicion actual o no
    @Override
    public T min() {
        if (values == null || values.length == 0) {
            return null;
        }

        T minValue = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i].compareTo(minValue) < 0) {
                minValue = values[i];
            }
        }
        return minValue;
    }

    // Sobrescribimos el metodo de la interfaz 'public T max()', el cual nos devolvera el valor maximo de ese arreglo
    // haciendo el mismo procedimiento de min(), pero en este caso evaluando que los valores sean mayores a la posición
    // actual para finalmente retornar el valor que se obtenga
    @Override
    public T max() {
        if (values == null || values.length == 0) {
            return null;
        }

        T maxValue = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i].compareTo(maxValue) > 0) {
                maxValue = values[i];
            }
        }
        return maxValue;
    }
}
