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

    Model model = new Model();
    View view =  new View(model);

    XYChart.Series series = new XYChart.Series<>();

    @FXML
    void initialize() {
        tableColumnX.setCellValueFactory(new PropertyValueFactory<>("x"));
        tableColumnY.setCellValueFactory(new PropertyValueFactory<>("y"));
        TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();

        resetChart();

        addButton.setOnAction(event -> {
            Random random = new Random();
            double x = random.nextInt(50);
            model.add(x);
            updateTableAndSeries();
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

                selectedValue = model.getValuesX()[index];

                textArea.setText(String.valueOf(selectedValue));
                add.setOnAction(event1 -> {
                    try{
                    double newValue = Double.valueOf(textArea.getText());
                    model.update(newValue,index);
                    resetChart();
                    updateTableAndSeries();
                    newWindow.close();
                    }
                    catch (NumberFormatException e){
                        textArea.setText("Ошибка ввода числа!");
                    }

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
                model.del(index);
                resetChart();
                updateTableAndSeries();
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

    private ObservableList<TableValue> addValuesToList(double[][] values, int count) {
        ObservableList<TableValue> list = FXCollections.observableArrayList();
        for (int a = 0; a < count; a++) {
            TableValue value = new TableValue(values[a][0], values[a][1]);
            list.add(value);
        }
        return list;
    }

    public void updateTableAndSeries() {
        addValueOnSeries(view.getValuesXY(),view.getValuesX().length,series);
        tableView.getItems().clear();
        tableView.getItems().addAll(addValuesToList(view.getValuesXY(),view.getValuesX().length));
    }

    public void resetChart() {
        areaChart.getData().remove(series);

        axisX.setLabel("x value");
        axisY.setLabel("y value");
        series = new XYChart.Series<>();
        series.setName("y = x²+6x+10");
        areaChart.getData().add(series);
        axisY.autosize();
        axisX.autosize();
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
