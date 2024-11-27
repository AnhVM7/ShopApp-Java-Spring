package com.ptit.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ptit.shopapp.models.Role;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponse {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("fullname")
  private String fullName;

  @JsonProperty("phone_number")
  private String phoneNumber;

  @JsonProperty("address")
  private String address;

  @JsonProperty("is_active")
  private boolean active;

  @JsonProperty("date_of_birth")
  private Date dateOfBirth;

  @JsonProperty("facebook_account_id")
  private int facebookAccountId;

  @JsonProperty("google_account_id")
  private int googleAccountId;

  @JsonProperty("role")
  private Role role;
}
