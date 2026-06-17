package com.example.demo.controllers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(@NotBlank(message = "Имя не может быть пустым")
                          @
                                  Size(min = 3, max = 50, message = "Имя должно быть от 3 до 50 символов")
                          String name,

                          @NotBlank(message = "Пароль не может быть пустым")
                          @Size(min = 6, max = 100, message = "Пароль должен быть от 6 до 100 символов")
                          String password,

                          @NotBlank(message = "Email не может быть пустым")
                          @Email(message = "Некорректный формат email")
                          String email) {

}
