package crud_gui;

import objects.DB;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import objects.Car;

/**
 *
 * @author Tóth Milán
 */
public class CRUD_GUIController implements Initializable {

    @FXML
    private Label txtExit;
    @FXML
    private TextField txtType;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtManufacturer;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private TableView<Car> tablebase;
    @FXML
    private TableColumn<Car, String> idColumn;
    @FXML
    private TableColumn<Car, String> manufacturerColumn;
    @FXML
    private TableColumn<Car, String> typeColumn;
    @FXML
    private TableColumn<Car, String> priceColumn;

    private DB db;
    private PreparedStatement pst;
    private Connection con;
    int index;
    int id;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("");

        db = new DB("jdbc:mysql://localhost:3306/cars", "root", "");
        db.importDriver();
        db.connect();
        con = db.getCon();

        setTableData();
    }

    @FXML
    private void addRecord(ActionEvent event) {

        if (inputCheck()) {

            String type = txtType.getText();
            String manufacturer = txtManufacturer.getText();
            int price = Integer.parseInt(txtPrice.getText());

            try {
                // Prepares the query
                pst = con.prepareStatement("INSERT INTO `cars` (`id`, `manufacturer`, `type`, `price`) VALUES (NULL, ?, ?, ?)");

                // Sets the values into the query
                pst.setString(1, manufacturer);
                pst.setString(2, type);
                pst.setInt(3, price);

                // Executes the query
                pst.executeUpdate();

                // Informs the user
                showAlert("Car adding", Alert.AlertType.INFORMATION, "Car added");

                txtManufacturer.setText("");
                txtPrice.setText("");
                txtType.setText("");
                txtManufacturer.requestFocus();

            } catch (SQLException err) {
                // SHOW ERROR BOX
                System.err.println(err.getMessage());
            }

            setTableData();
        }
    }

    @FXML
    private void updateRecord(ActionEvent event) {

        // If no cell is selected
        if (tablebase.getSelectionModel().getSelectedIndex() == -1) {
            showAlert("Cell selection", Alert.AlertType.ERROR, "No cell selected!");
        } else {
            inputCheck();

            String type = txtType.getText();
            String manufacturer = txtManufacturer.getText();
            int price = Integer.parseInt(txtPrice.getText());

            // Gets the choosed cells index
            index = tablebase.getSelectionModel().getSelectedIndex();
            id = Integer.parseInt(String.valueOf(tablebase.getItems().get(index).getId()));

            try {
                // Prepares the query
                pst = con.prepareStatement("UPDATE `cars` SET `manufacturer` = ?, `type` = ?, `price` = ? WHERE `cars`.`id` = ?");

                // Sets the values into the query
                pst.setString(1, manufacturer);
                pst.setString(2, type);
                pst.setInt(3, price);
                pst.setInt(4, id);

                // Executes the query
                pst.executeUpdate();

                // Informs the user
                showAlert("Car updating", Alert.AlertType.INFORMATION, "Car updated");

                txtManufacturer.setText("");
                txtPrice.setText("");
                txtType.setText("");
                txtManufacturer.requestFocus();

            } catch (SQLException err) {
                // SHOW ERROR BOX
                System.err.println(err.getMessage());
            }

            setTableData();
        }
    }

    @FXML
    private void deleteRecord(ActionEvent event) {

        // If no cell is selected
        if (tablebase.getSelectionModel().getSelectedIndex() == -1) {
            showAlert("Cell selection", Alert.AlertType.ERROR, "No cell selected!");
        } else {
            try {
                // Prepares the query
                pst = con.prepareStatement("DELETE FROM `cars` WHERE `cars`.`id` = ?");

                // Sets the values into the query
                pst.setInt(1, id);

                // Executes the query
                pst.executeUpdate();

                // Informs the user
                showAlert("Car removing", Alert.AlertType.INFORMATION, "Car removed");

                txtManufacturer.setText("");
                txtPrice.setText("");
                txtType.setText("");
                txtManufacturer.requestFocus();

            } catch (SQLException err) {
                // SHOW ERROR BOX
                System.err.println(err.getMessage());
            }

            setTableData();
        }
    }

    @FXML
    private void exit(MouseEvent event) {
        db.disconnect();
        System.exit(0);
    }

    /**
     * Display an alertbox
     */
    private void showAlert(String title, Alert.AlertType type, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /**
     * Displays the result in the table
     */
    private void setTableData() {
        ObservableList<Car> cars = FXCollections.observableArrayList();

        try {
            // Prepares the query
            pst = con.prepareStatement("SELECT * FROM `cars`");

            // Stores the result
            ResultSet rs = pst.executeQuery();

            // Fills the ObservableList with the results 
            while (rs.next()) {
                Car car = new Car();

                car.setId(rs.getString("id"));
                car.setManufacturer(rs.getString("manufacturer"));
                car.setType(rs.getString("type"));
                car.setPrice(rs.getString("price"));

                cars.add(car);
            }

            // Fills the items with ObservableList
            tablebase.setItems(cars);

            // Sets the table cells value
            idColumn.setCellValueFactory(f -> f.getValue().getIdProperty());
            manufacturerColumn.setCellValueFactory(f -> f.getValue().getManufacturerProperty());
            typeColumn.setCellValueFactory(f -> f.getValue().getTypeProperty());
            priceColumn.setCellValueFactory(f -> f.getValue().getPriceProperty());

        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }

        tablebase.setRowFactory(value -> {
            TableRow<Car> row = new TableRow<>();

            // Handles the mouse click event
            row.setOnMouseClicked(event -> {

                // When I click the tables cells, the result will appear in the TextFields
                if (event.getClickCount() == 1 && (!row.isEmpty())) {

                    // Gets the choosed cells index
                    index = tablebase.getSelectionModel().getSelectedIndex();

                    id = Integer.parseInt(String.valueOf(tablebase.getItems().get(index).getId()));
                    txtManufacturer.setText(tablebase.getItems().get(index).getManufacturer());
                    txtType.setText(tablebase.getItems().get(index).getType());
                    txtPrice.setText(tablebase.getItems().get(index).getPrice());
                }

            });

            return row;

        });
    }

    /**
     * Validates the inputs
     */
    private boolean inputCheck() {

        boolean valid = true;

        if (txtManufacturer.getText().equals("")) {
            showAlert("Empty cell", Alert.AlertType.ERROR, "Empty Manufacturer!");
            valid = false;
        } else if (txtType.getText().equals("")) {
            showAlert("Empty cell", Alert.AlertType.ERROR, "Empty Type!");
            valid = false;
        } else if (!txtPrice.getText().equals("")) {
            try {
                Integer.parseInt(txtPrice.getText());
            } catch (NumberFormatException e) {
                showAlert("Invalid value", Alert.AlertType.ERROR, "Invalid Price!");
                valid = false;
            }
        } else {
            showAlert("Empty cell", Alert.AlertType.ERROR, "Empty Price!");
            valid = false;
        }

        return valid;
    }

}
