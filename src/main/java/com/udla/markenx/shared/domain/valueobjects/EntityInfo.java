package com.udla.markenx.shared.domain.valueobjects;

import java.util.UUID;

public record EntityInfo(
    UUID id,
    EntityStatus status) {
}
