package sample.Model;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@MappedSuperclass
public abstract class Human {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false )
    private int id;

    @Column(name = "fname")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "identityNumber")
    private long identityNumber;

    @Column(name = "pword")
    private String password;

    @Column(name = "street")
    private String street;

    @Column(name = "dnumber")
    private String number;

    @Column(name = "town")
    private String town;

    @Column(name = "city")
    private String city;

    public Human() {
    }

    public Human(String name, String surname, long identityNumber, String password, String street, String number, String town, String city) {
        this.name = name;
        this.surname = surname;
        this.identityNumber = identityNumber;
        this.password = password;
        this.street = street;
        this.number = number;
        this.town = town;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(long identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
