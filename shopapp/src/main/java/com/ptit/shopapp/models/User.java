package com.ptit.shopapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fullname", length = 100)
  private String fullName;

  @Column(name = "phone_number", length = 10, nullable = false)
  private String phoneNumber;

  @Column(name = "address", length = 200)
  private String address;

  @Column(name = "password", length = 100, nullable = false)
  private String password;

  @Column(name = "is_active")
  private boolean active;

  @Column(name = "date_of_birth")
  private Date dateOfBirth;

  @Column(name = "facebook_account_id")
  private int facebookAccountId;

  @Column(name = "google_account_id")
  private int googleAccountId;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
    authorityList.add(new SimpleGrantedAuthority("ROLE_" + getRole().getName().toUpperCase()));
    return authorityList;
  }

  @Override
  public String getUsername() {
    return phoneNumber;
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
}
