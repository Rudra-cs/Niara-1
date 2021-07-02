package com.example.niara.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "customerinfo")
public class Address {
    @PrimaryKey(autoGenerate = true)
    private int customerid;
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "user")
    private int user;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "locality")
    private String locality;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "zipcode")
    private String zipcode;

    @ColumnInfo(name = "mobile")
    private String mobile;

    @ColumnInfo(name = "state")
    private String state;

    public Address(){

    }

    public Address(int id,int user,String name,String locality,String city,String zipcode,String mobile,String state){
        this.city=city;
        this.id=id;
        this.user=user;
        this.name=name;
        this.locality=locality;
        this.zipcode=zipcode;
        this.mobile=mobile;
        this.state=state;

    }

    @Override
    public String toString() {
        return "Address{" +
                "customerid=" + customerid +
                ", id=" + id +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", locality='" + locality + '\'' +
                ", city='" + city + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", mobile='" + mobile + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
