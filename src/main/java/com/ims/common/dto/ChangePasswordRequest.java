package com.ims.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
