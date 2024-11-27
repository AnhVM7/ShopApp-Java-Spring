package com.ptit.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
public class UserLoginDTO {
  @JsonProperty("phone_number")
  @NotBlank(message = "Phone number is required")
  private String phoneNumber;

  @NotBlank(message = "password cannot be blank")
  private String password;
}
