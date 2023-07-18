package com.example.defecttrackerserver.core.lot.lot;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lots")
public class LotController {
    private final LotService lotService;

    @PostMapping
    public LotDto saveLot(@Valid @RequestBody LotDto lotDto) {
        return lotService.saveLot(lotDto);
    }

    @GetMapping("/{id}")
    public LotDto getLotById(@PathVariable Integer id) {
        return lotService.getLotById(id);
    }

    @GetMapping
    public List<LotDto> getAllLots() {
        return lotService.getAllLots();
    }

    @PutMapping
    public LotDto updateLot(@Valid@RequestBody LotDto lotDto) {
        return lotService.updateLot(lotDto);
    }

    @DeleteMapping("/{id}")
    public void deleteLot(@PathVariable Integer id) {
        lotService.deleteLot(id);
    }
}
