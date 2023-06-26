package com.pay.paypilot.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.io.Serializable;


/**
 * A DTO for the {@link com.pay.paypilot.model.User} entity
 */
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class CreateUserRequest implements Serializable {
    @NotBlank(message = "First name cannot be blank")
    private  String firstName;
    @NotBlank(message = "Last name cannot be blank")
    private  String lastName;
    private  String otherName;
    @NotBlank(message = "Email name cannot be blank")
    private  String email;
    @Email(message = "Please enter a valid email address")
    private  String phoneNumber;
    @NotBlank(message = "Password cannot be blank")
    private  String bvn;
    @NotBlank(message = "Confirm password name cannot be blank")
    private  String password;

}
