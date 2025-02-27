package com.ctw.workstation.team.dto;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeamRequest(
        @NotBlank(message="name may not be blank")
        String name,
        @NotBlank(message="product may not be blank")
        String product,
        @Size(max = 30)
        @NotBlank(message="defaultLocation may not be blank")
        String defaultLocation
) { }
