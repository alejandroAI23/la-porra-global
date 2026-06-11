package com.porraglobal.leagues.controller;

import com.porraglobal.leagues.dto.CreateLeagueRequest;
import com.porraglobal.leagues.dto.JoinLeagueRequest;
import com.porraglobal.leagues.dto.LeagueMemberResponse;
import com.porraglobal.leagues.dto.LeagueResponse;
import com.porraglobal.leagues.service.LeagueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leagues")
@RequiredArgsConstructor
@Tag(name = "Leagues", description = "Ligas privadas de predicciones")
public class LeagueController {

    private final LeagueService leagueService;

    @PostMapping
    @Operation(summary = "Crear una liga privada", description = "El creador se convierte en propietario y primer miembro")
    public ResponseEntity<LeagueResponse> create(@Valid @RequestBody CreateLeagueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leagueService.createLeague(request));
    }

    @PostMapping("/{id}/join")
    @Operation(summary = "Unirse a una liga", description = "Requiere el código de invitación de la liga")
    public ResponseEntity<LeagueMemberResponse> join(@PathVariable Long id,
                                                     @Valid @RequestBody JoinLeagueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leagueService.joinLeague(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalles de una liga")
    public LeagueResponse get(@PathVariable Long id) {
        return leagueService.getLeague(id);
    }

    @GetMapping("/{id}/members")
    @Operation(summary = "Listar miembros de una liga")
    public List<LeagueMemberResponse> members(@PathVariable Long id) {
        return leagueService.getMembers(id);
    }
}
