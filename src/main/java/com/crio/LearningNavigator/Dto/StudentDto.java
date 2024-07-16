package com.crio.LearningNavigator.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String name;

}
