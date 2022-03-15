package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Objects;


@DatabaseTable(tableName = "user")
public class User implements Filtration {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String fname;

    @DatabaseField
    private String lname;

    @DatabaseField
    private String password;

    @DatabaseField
    private Role role;

    @DatabaseField(unique = true)
    private String email;

    @DatabaseField(unique = true)
    private String phone;

    @DatabaseField
    private String address;

    @DatabaseField
    private String dateOfRegister;

    @DatabaseField
    private String dateOfBirthday;

    @DatabaseField
    private boolean phoneIsShown;

    @DatabaseField
    private boolean emailIsShown;


    public User() {
    }

    public User(String fname, String lname, String password, Role role, String email, String phone, String address, String dateOfRegister, String dateOfBirthday, boolean phoneIsShown, boolean emailIsShown) {
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dateOfRegister = dateOfRegister;
        this.dateOfBirthday = dateOfBirthday;
        this.phoneIsShown = phoneIsShown;
        this.emailIsShown = emailIsShown;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirthday() {
        return dateOfBirthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDateOfBirthday(String dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }

    public String getDateOfRegister() {
        return dateOfRegister;
    }

    public void setDateOfRegister(String dateOfRegister) {
        this.dateOfRegister = dateOfRegister;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPhoneIsShown() {
        return phoneIsShown;
    }

    public void setPhoneIsShown(boolean phoneIsShown) {
        this.phoneIsShown = phoneIsShown;
    }

    public boolean isEmailIsShown() {
        return emailIsShown;
    }

    public void setEmailIsShown(boolean emailIsShown) {
        this.emailIsShown = emailIsShown;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    @Override
    public ArrayList<String> getQueryParams(){
        ArrayList<String> s = new ArrayList<>();
        s.add("fname");
        s.add("lname");
        s.add("address");
        s.add("dateOfRegister");
        s.add("dateOfBirthday");
        s.add("email");
        s.add("id");
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                phoneIsShown == user.phoneIsShown &&
                emailIsShown == user.emailIsShown &&
                role == user.role &&
                Objects.equals(password, user.password) &&
                Objects.equals(dateOfBirthday, user.dateOfBirthday) &&
                Objects.equals(dateOfRegister, user.dateOfRegister) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(fname, user.fname) &&
                Objects.equals(lname, user.lname) &&
                Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, password, dateOfBirthday, dateOfRegister, email, phone, phoneIsShown, emailIsShown, fname, lname, address);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }


}
