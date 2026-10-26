package com.cobraflow.debtcollection.api;

import com.cobraflow.debtcollection.domain.AgentRepository;
import com.cobraflow.debtcollection.domain.DebtService;
import com.cobraflow.debtcollection.domain.DebtState;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DebtController {
    private final AgentRepository agentRepository;
    private final DebtService debtService;

    public DebtController(AgentRepository agentRepository, DebtService debtService) {
        this.agentRepository = agentRepository;
        this.debtService = debtService;
    }

    @GetMapping("/debts")
    public List<DebtResponse> debts(@RequestParam(required = false) String search,
                                    @RequestParam(required = false) DebtState state) {
        return debtService.find(search, state);
    }

    @GetMapping("/agents")
    public List<AgentResponse> agents() {
        return agentRepository.findAll(Sort.by("name")).stream()
                .map(agent -> new AgentResponse(agent.getId(), agent.getName()))
                .toList();
    }

    @PostMapping("/debts/assignments")
    public ResponseEntity<List<DebtResponse>> assign(@Valid @RequestBody AssignDebtsRequest request) {
        return ResponseEntity.ok(debtService.assign(request).stream()
                .map(debtService::toResponse).toList());
    }
}