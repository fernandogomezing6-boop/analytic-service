package com.cobraflow.debtcollection.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record AssignDebtsRequest(
        @NotEmpty List<Long> debtIds,
        @NotBlank String agentName
) {
}