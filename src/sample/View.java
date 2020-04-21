package sample;

public class View {

    private double[] valuesX = new double[]{};
    private double[][] valuesXY = new double[][]{};

    private Model model;

    public View(Model model) {
        this.model = model;
    }

    public double[] getValuesX() {
        valuesX = model.getValuesX();
        return valuesX;
    }

    public double[][] getValuesXY() {
        valuesXY = model.getValuesXY();
        return valuesXY;
    }

}
