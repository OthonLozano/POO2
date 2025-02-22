public class Calculator<T extends Number> {
    T[] num;

    public Calculator(T[] num) {
        this.num = num;
    }

    public double average () {
        double sum = 0;
        for (int i = 0; i < num.length; i++) {
            sum += num[i].doubleValue();
        }

        return sum / num.length;
    }
}
