package model;

public class Customers {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private String division;
    private String country;

    public Customers(int id, String name,String address, String postalCode, String phone, String division, String country){
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
        this.country = country;
    }

    public int getId() {
        return id;
    }
    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public String postalCode(){
        return postalCode;
    }
    private String getPhone(){
        return phone;
    }
    private String getDivision() {
        return division;
    }
    private String getCountry(){
        return country;
    }
}
