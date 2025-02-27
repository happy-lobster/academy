package com.ctw.workstation.teammember.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeamMemberRequest(
        @Min(1)
        Long teamId,
        @Size(max=30)
        @NotBlank(message="name may not be blank")
        String name,
        @Size(max=8)
        @NotBlank(message="ctwId may not be blank")
        String ctwId
) { }
