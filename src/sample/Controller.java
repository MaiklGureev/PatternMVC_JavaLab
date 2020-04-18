package sample;

import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sun.awt.dnd.SunDropTargetContextPeer;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<TableValue> tableView;

    @FXML
    private TableColumn<TableValue, Double> tableColumnX;

    @FXML
    private TableColumn<TableValue, Double> tableColumnY;

    @FXML
    private Button addButton;

    @FXML
    private Button delButton;

    @FXML
    private Button updateButton;

    @FXML
    private StackedAreaChart<?, ?> areaChart;

    @FXML
    private NumberAxis axisX;

    @FXML
    private NumberAxis axisY;

    double[] valuesX = new double[]{};
    double[][] valuesXY = new double[][]{};
    XYChart.Series series = new XYChart.Series<>();

    @FXML
    void initialize() {
        tableColumnX.setCellValueFactory(new PropertyValueFactory<>("x"));
        tableColumnY.setCellValueFactory(new PropertyValueFactory<>("y"));
        TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();

        series.setName("y = x²+6x+10");
        axisX.setLabel("x value");
        axisY.setLabel("y value");
        addDataOnChart(areaChart, series);

        addButton.setOnAction(event -> {
            Random random = new Random();
            double x = random.nextInt(50);
            valuesX = addValueToArray(x, valuesX);
            updateTable();
        });

        updateButton.setOnAction(event -> {

            int index = selectionModel.getSelectedIndex();
            double selectedValue = 0;

            if (index != -1) {
                Stage newWindow = new Stage();
                FlowPane root = new FlowPane();
                TextField textArea = new TextField();
                Button add = new Button("Update");
                Button close = new Button("Close");

                selectedValue = valuesX[index];

                textArea.setText(String.valueOf(selectedValue));
                add.setOnAction(event1 -> {
                    double newValue = Double.valueOf(textArea.getText());
                    valuesX[index] = newValue;
                    updateTable();
                    newWindow.close();
                });

                close.setOnAction(event1 -> {
                    newWindow.close();
                });

                root.setPadding(new Insets(10));
                root.setVgap(5);
                root.setHgap(5);
                root.getChildren().addAll(textArea, add, close);
                Scene secondScene = new Scene(root, 200, 100);

                // New window (Stage)

                newWindow.setTitle("Edit value");
                newWindow.setScene(secondScene);
                newWindow.show();
            }

        });

        delButton.setOnAction(event -> {
            int index = selectionModel.getSelectedIndex();
            if (index != -1) {
                valuesX = delValueInArray(valuesX, index);
                resetChart();
                updateTable();
            }
        });

    }

    public XYChart.Series addValueOnSeries(double[][] doubles, int count, XYChart.Series series) {
        series.getData().removeAll();
        for (int a = 0; a < count; a++) {
            series.getData().add(new XYChart.Data<Number, Number>(doubles[a][0], doubles[a][1]));
        }
        return series;
    }

    public void addDataOnChart(StackedAreaChart areaChart, XYChart.Series series) {
        areaChart.getData().add(series);
    }

    public double[][] calculateFunctionValues(double[] table) {
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

    public double[] addValueToArray(double x, double[] doubles) {
        doubles = Arrays.copyOf(doubles, doubles.length + 1);
        doubles[doubles.length - 1] = x;
        return doubles;
    }

    public double[] updateValueInArray(double newValue, double[] doubles, int index) {
        doubles[index] = newValue;
        return doubles;
    }


    public double[] delValueInArray(double[] doubles, int index) {
        double[] newDoubles = Arrays.copyOf(doubles, doubles.length - 1);
        System.arraycopy(doubles, index + 1, newDoubles, index, doubles.length - index - 1);
        return newDoubles;
    }

    private ObservableList<TableValue> addValuesToList(double[][] values, int count) {
        ObservableList<TableValue> list = FXCollections.observableArrayList();
        for (int a = 0; a < count; a++) {
            TableValue value = new TableValue(values[a][0], values[a][1]);
            list.add(value);
        }
        return list;
    }

    public void updateTable() {
        valuesXY = calculateFunctionValues(valuesX);
        addValueOnSeries(valuesXY, valuesX.length, series);
//        axisX.autosize();
//        axisY.autosize();
        tableView.getItems().clear();
        tableView.getItems().addAll(addValuesToList(valuesXY, valuesX.length));
    }

    public void resetChart() {
        areaChart.getData().remove(series);
        axisX.resize(areaChart.getWidth(),areaChart.getHeight());
        axisY.resize(areaChart.getWidth(),areaChart.getHeight());
        axisX.setLabel("x value");
        axisY.setLabel("y value");
        series = new XYChart.Series<>();
        series.setName("y = x²+6x+10");
        addDataOnChart(areaChart, series);
    }

    public class TableValue {
        public double x;
        public double y;

        public TableValue() {
        }

        public TableValue(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

    }


}
