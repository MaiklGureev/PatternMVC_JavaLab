package sample;

import java.util.Arrays;

public class Model {

    private double[] valuesX = new double[]{};
    private double[][] valuesXY = new double[][]{};

    public void add(double x){
        valuesX = addValueToArray(x, valuesX);
        valuesXY = calculateFunctionValues(valuesX);
    }

    public void update(double newValue, int index){
        valuesX = updateValueInArray(newValue,valuesX,index);
        valuesXY = calculateFunctionValues(valuesX);
    }

    public void del(int index){
        valuesX = delValueInArray(valuesX, index);
        valuesXY = calculateFunctionValues(valuesX);
    }

    private double[][] calculateFunctionValues(double[] table) {
        double x, y;
        double[][] values = new double[table.length][2];
        for (int a = 0; a < table.length; a++) {
            x = table[a];
            y = x * x + 6 * x + 10;
            values[a][0] = x;
            values[a][1] = y;
        }
        return values;
    }

    private double[] addValueToArray(double x, double[] doubles) {
        doubles = Arrays.copyOf(doubles, doubles.length + 1);
        doubles[doubles.length - 1] = x;
        return doubles;
    }

    private double[] updateValueInArray(double newValue, double[] doubles, int index) {
        doubles[index] = newValue;
        return doubles;
    }


    private double[] delValueInArray(double[] doubles, int index) {
        double[] newDoubles = Arrays.copyOf(doubles, doubles.length - 1);
        System.arraycopy(doubles, index + 1, newDoubles, index, doubles.length - index - 1);
        return newDoubles;
    }

    public double[] getValuesX() {
        return valuesX;
    }

    public double[][] getValuesXY() {
        return valuesXY;
    }


}
