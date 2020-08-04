package com.trustpoint.cases.api;

import com.trustpoint.cases.exception.ResourceNotFoundException;
import com.trustpoint.cases.model.Case;
import com.trustpoint.cases.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RequestMapping("cases")
@RestController
public class CaseController {
    private final CaseService caseService;

    @Autowired
    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @PostMapping
    public Case addCase(@Valid @NotNull @RequestBody Case newcase, @NotEmpty @RequestHeader("X-Subject") String owner) {
        newcase.setOwner(owner);
        return caseService.addCase(newcase);
    }

    @GetMapping
    public List<Case> getCases(@NotEmpty @RequestHeader("X-Subject") String owner, @RequestHeader(value = "business-units", required = false) String businessUnits) {
        return caseService.getCases(owner, businessUnits);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteCase(@PathVariable("id") UUID id) {
        return caseService.deleteCase(id);
    }

    @GetMapping(path = "{id}")
    public Case getPersonByID(@PathVariable("id") UUID id, @NotEmpty @RequestHeader("X-Subject") String owner, @RequestHeader(value = "business-units", required = false) String businessUnits) {
        return caseService.getCaseByID(id, owner, businessUnits).orElse(null);
    }

    @PutMapping(path = "{id}")
    public Case updateCase(@PathVariable("id") UUID id, @Valid @NotNull @RequestBody Case update, @NotEmpty @RequestHeader("X-Subject") String user) {
        return caseService.updateCase(id, update, user)
                .orElseThrow(() -> new ResourceNotFoundException("Case with id " + id.toString() + " not found"));
    }
}
