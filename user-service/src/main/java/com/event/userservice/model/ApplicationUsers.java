package com.event.userservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "application_users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplicationUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @ManyToMany
    @JoinTable(name="follower_table",
            joinColumns = @JoinColumn(name = "followee_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<ApplicationUsers> followers = new HashSet<>();
    @ManyToMany(mappedBy = "followers")
    private Set<ApplicationUsers> following = new HashSet<>();

}
