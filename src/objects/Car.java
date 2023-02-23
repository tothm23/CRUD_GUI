package objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Tóth Milán
 */
public class Car {

    /*
    StringProperty  TableView columns need it
    String          TextFields need it
     */
    private final StringProperty id;
    private final StringProperty manufacturer;
    private final StringProperty type;
    private final StringProperty price;

    public Car() {
        id = new SimpleStringProperty(this, "id");
        manufacturer = new SimpleStringProperty(this, "manufacturer");
        type = new SimpleStringProperty(this, "type");
        price = new SimpleStringProperty(this, "price");
    }

    public StringProperty getIdProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty getManufacturerProperty() {
        return manufacturer;
    }

    public String getManufacturer() {
        return manufacturer.get();
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer.set(manufacturer);
    }

    public StringProperty getTypeProperty() {
        return type;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty getPriceProperty() {
        return price;
    }

    public String getPrice() {
        return price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

}
