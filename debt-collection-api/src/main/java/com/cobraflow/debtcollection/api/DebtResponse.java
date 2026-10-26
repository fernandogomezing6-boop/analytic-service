package com.cobraflow.debtcollection.api;

import com.cobraflow.debtcollection.domain.DebtState;
import java.math.BigDecimal;

public record DebtResponse(
        Long id,
        String debtor,
        String document,
        String concept,
        BigDecimal amount,
        Integer days,
        DebtState state,
        String agent
) {
}