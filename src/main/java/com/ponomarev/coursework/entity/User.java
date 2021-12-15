package com.ponomarev.coursework.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usr", uniqueConstraints = @UniqueConstraint(columnNames = {"login"}))
public class User extends BaseEntity implements UserDetails {

    private String login;

    @NotBlank(message = "Type your password correctly")
    @Size(min = 8, message = "Length of password should be > 8 characters")
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    private boolean active;

    @OneToOne
    @JoinColumn(name = "user_info_id")
    private UserInfo information;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<Template> templates;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "saving_account_id")
    private SavingAccount savingAccount;

    public enum Role implements GrantedAuthority{
        USER, ADMIN;

        @Override
        public String getAuthority() {
            return name();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public boolean deleteTemplateById(Long id) {
        return templates.removeIf(e -> e.getId().equals(id));
    }

}
