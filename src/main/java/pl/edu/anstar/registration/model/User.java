package pl.edu.anstar.registration.model;

//import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

//Pod baze danych na potem
//@Data
//@Builder(toBuilder = true)
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//@Document("users")
public class User {
    //    @Id
    private String id;
    // @Indexed(unique = true)
    private String name;
    private String surname;
    private String email;
    private String password;

    //Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    //Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

