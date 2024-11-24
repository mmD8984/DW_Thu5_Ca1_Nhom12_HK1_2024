package Model;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Product {
    private int id;
    private String productName;
    private String cpu;
    private String ram;
    private String storageCapacity;
    private String screenSize;
    private double price;
    private String batteryCapacity;
    private String os;
    private String manufacturer;

    public Product(int id, String productName, String cpu, String ram, String storageCapacity, String screenSize, double price, String batteryCapacity, String os, String manufacturer) {
        this.id = id;
        this.productName = productName;
        this.cpu = cpu;
        this.ram = ram;
        this.storageCapacity = storageCapacity;
        this.screenSize = screenSize;
        this.price = price;
        this.batteryCapacity = batteryCapacity;
        this.os = os;
        this.manufacturer = manufacturer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(String storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(String batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", cpu='" + cpu + '\'' +
                ", ram='" + ram + '\'' +
                ", storageCapacity='" + storageCapacity + '\'' +
                ", screenSize='" + screenSize + '\'' +
                ", price=" + price +
                ", batteryCapacity='" + batteryCapacity + '\'' +
                ", os='" + os + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}

