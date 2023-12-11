package com.example.test.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@JsonIgnoreProperties(value= {"role"})
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="user_id")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<Role> roles;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "performer")
    private Set<Task> tasksPerformed;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author")
    private Set<Task> tasksAssigned;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author")
    private Set<Comment> comments;

    public User(String email, String username, String password, Set<Role> roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public User(String email, String username, String password,
                Set<Role> roles, Set<Task> tasksPerformed, Set<Task> tasksAssigned) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.tasksPerformed = tasksPerformed;
        this.tasksAssigned = tasksAssigned;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Task> getTasksPerformed() {
        return tasksPerformed;
    }

    public void setTasksPerformed(Set<Task> tasksPerformed) {
        this.tasksPerformed = tasksPerformed;
    }

    public Set<Task> getTasksAssigned() {
        return tasksAssigned;
    }

    public void setTasksAssigned(Set<Task> tasksAssigned) {
        this.tasksAssigned = tasksAssigned;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getRolesToString() {
        List<Role> list = getRoles().stream().toList();
        StringBuilder str = new StringBuilder(list.get(0).toString());
        if (list.size() == 2) {
            str.append(" ").append(list.get(1).toString());
        }
        return String.valueOf(str);
    }
}
