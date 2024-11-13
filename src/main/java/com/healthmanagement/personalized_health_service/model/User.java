package com.healthmanagement.personalized_health_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonProperty("username")
    private String username;

    @Column(nullable = false, unique = true)
    @JsonProperty("email")
    private String email;

    @Column(nullable = false)
    @JsonProperty("password")
    private String password;

    @Column
    @JsonProperty("height")
    private Double height;

    @Column
    @JsonProperty("weight")
    private Double weight;

    @Column
    @JsonProperty("age")
    private Integer age;

    @Column
    @JsonProperty("gender")
    private String gender;

    @Column(nullable = false)
    @JsonProperty("healthGoal")
    private String healthGoal;

    @Column
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGoal() {
        return healthGoal;
    }
}
