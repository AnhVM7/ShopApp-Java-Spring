package com.ptit.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDTO {
  @JsonProperty("fullname")
  private String fullName;

  @JsonProperty("phone_number")
  @NotBlank(message = "Phone number is required")
  private String phoneNumber;

  private String address;

  @NotBlank(message = "password cannot be blank")
  private String password;

  @JsonProperty("retype_password")
  private String retypePassword;

  @JsonProperty("date_of_birth")
  private Date dateOfBirth;

  @JsonProperty("facebook_account_id")
  private int facebookAccountId;

  @JsonProperty("google_account_id")
  private int googleAccountId;

  @JsonProperty("role_id")
  @NotNull(message = "Role Id is required")
  private Long roleId;

  @JsonProperty("is_active")
  private boolean active;
}
