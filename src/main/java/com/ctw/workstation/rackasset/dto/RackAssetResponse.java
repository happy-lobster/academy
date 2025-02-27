package com.ctw.workstation.rackasset.dto;

import com.ctw.workstation.rack.dto.RackResponse;
import com.ctw.workstation.rackasset.entity.RackAsset;

public record RackAssetResponse(
        Long id,
        String tag,
        RackResponse rack
) {
    public static RackAssetResponse from(RackAsset entity) {
        return new RackAssetResponse(
                entity.getId(),
                entity.getTag(),
                RackResponse.from(entity.getRack())
        );
    }
}
