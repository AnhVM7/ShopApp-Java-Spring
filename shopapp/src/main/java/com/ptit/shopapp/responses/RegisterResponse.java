package com.ptit.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ptit.shopapp.models.User;
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
public class RegisterResponse {
  @JsonProperty("message")
  private String message;

  @JsonProperty("user")
  private User user;
}
