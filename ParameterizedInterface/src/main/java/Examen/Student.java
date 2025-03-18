package Examen;

/* Implementamos Comparable<Student> para poder sobrescribir el metodo compareTo(Student o) y utilizarlo para nuesto
   beneficio*/
public class Student implements Comparable<Student> {
    private String name;
    private int age;

    public Student (String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString () {
        return "Student {" +
                "name='" + name + '\'' +
                ", age=" + age +
                " }";
    }

    //Aqui sobrescribimos el metodo compareTo para poder comparar dos estudiantes de acuerdo a su edad
    @Override
    public int compareTo(Student o) {
        return this.age - o.age;
    }
}
