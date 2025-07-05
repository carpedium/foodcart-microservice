package com.foodcart.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic error response for exceptions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    private String erroCode;
    private String errorMessage;
}
