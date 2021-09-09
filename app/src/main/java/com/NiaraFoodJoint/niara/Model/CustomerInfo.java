package com.NiaraFoodJoint.niara.Model;

public class CustomerInfo {
    private int customerid;
    private int id;
    private int user;
    private String name;
    private String locality;
    private String city;
    private String zipcode;

    public CustomerInfo(){

    }

    public CustomerInfo(int id,int user,String name,String locality,String city,String zipcode,String mobile,String state){
        this.city=city;
        this.id=id;
        this.user=user;
        this.name=name;
        this.locality=locality;
        this.zipcode=zipcode;
        this.mobile=mobile;
        this.state=state;

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

    private String mobile;
    private String state;
}
