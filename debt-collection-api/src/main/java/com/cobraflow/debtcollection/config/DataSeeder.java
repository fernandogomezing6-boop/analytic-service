package com.cobraflow.debtcollection.config;

import com.cobraflow.debtcollection.domain.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedData(AgentRepository agents, DebtRepository debts) {
        return args -> {
            if (agents.count() > 0) return;
            Agent valentina = agents.save(new Agent("Valentina Ruiz"));
            Agent carlos = agents.save(new Agent("Carlos Medina"));
            Agent ana = agents.save(new Agent("Ana Torres"));
            Agent mateo = agents.save(new Agent("Mateo Silva"));

            debts.saveAll(List.of(
                    debt(1048L, "María Fernanda López", "CC 1032456789", "Crédito personal", "3250000", 42, DebtState.VENCIDA, valentina),
                    debt(1047L, "Constructora Andina SAS", "NIT 900457812", "Factura 0248", "8750000", 18, DebtState.VENCIDA, carlos),
                    debt(1046L, "Javier Andrés Mora", "CC 79654218", "Tarjeta débito", "1240000", 6, DebtState.POR_VENCER, null),
                    debt(1045L, "Comercial del Norte LTDA", "NIT 890124530", "Línea de crédito", "15300000", 2, DebtState.POR_VENCER, null),
                    debt(1044L, "Santiago Herrera", "CC 1022387914", "Préstamo vehículo", "4680000", 0, DebtState.AL_DIA, ana),
                    debt(1043L, "Lucía Pérez Gómez", "CC 52987134", "Crédito personal", "2100000", 31, DebtState.VENCIDA, carlos)
            ));
        };
    }

    private Debt debt(Long id, String debtor, String document, String concept, String amount,
                      int days, DebtState state, Agent agent) {
        Debt debt = new Debt();
        debt.setId(id);
        debt.setDebtor(debtor);
        debt.setDocument(document);
        debt.setConcept(concept);
        debt.setAmount(new BigDecimal(amount));
        debt.setDays(days);
        debt.setState(state);
        debt.setAgent(agent);
        return debt;
    }
}