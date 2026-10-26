package com.cobraflow.debtcollection.domain;

import com.cobraflow.debtcollection.api.AssignDebtsRequest;
import com.cobraflow.debtcollection.api.DebtResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DebtService {
    private final DebtRepository debtRepository;
    private final AgentRepository agentRepository;

    public DebtService(DebtRepository debtRepository, AgentRepository agentRepository) {
        this.debtRepository = debtRepository;
        this.agentRepository = agentRepository;
    }

    @Transactional(readOnly = true)
    public List<DebtResponse> find(String search, DebtState state) {
        String term = search == null ? null : search.trim().toLowerCase();
        return debtRepository.findAll().stream()
                .filter(debt -> state == null || debt.getState() == state)
                .filter(debt -> term == null || term.isBlank()
                        || debt.getDebtor().toLowerCase().contains(term)
                        || debt.getDocument().toLowerCase().contains(term)
                        || debt.getConcept().toLowerCase().contains(term))
                .sorted((left, right) -> right.getId().compareTo(left.getId()))
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public List<Debt> assign(AssignDebtsRequest request) {
        Agent agent = agentRepository.findByName(request.agentName())
                .orElseThrow(() -> new IllegalArgumentException("Agente no encontrado: " + request.agentName()));
        List<Debt> debts = debtRepository.findAllById(request.debtIds());
        if (debts.size() != request.debtIds().stream().distinct().count()) {
            throw new IllegalArgumentException("Una o más obligaciones no existen");
        }
        debts.forEach(debt -> debt.setAgent(agent));
        return debtRepository.saveAll(debtRepository.findAllById(request.debtIds()));
    }

    public DebtResponse toResponse(Debt debt) {
        return new DebtResponse(debt.getId(), debt.getDebtor(), debt.getDocument(), debt.getConcept(),
                debt.getAmount(), debt.getDays(), debt.getState(), debt.getAgent() == null ? null : debt.getAgent().getName());
    }
}