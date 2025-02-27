package com.ctw.workstation.rackasset.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RackAssetRequest(
        @Size(max = 30)
        @NotBlank(message="tag may not be blank")
        String tag,
        @Min(1)
        Long rackId
) {
}
