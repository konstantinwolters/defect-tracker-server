package com.example.defecttrackerserver.core.lot.lot;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lots")
public class LotController {
    private final LotService lotService;

    @PostMapping
    public LotDto saveLot(@RequestBody LotDto lotDto) {
        return lotService.saveLot(lotDto);
    }

    @GetMapping
    public LotDto getLotById(@RequestParam Integer id) {
        return lotService.getLotById(id);
    }

    @GetMapping
    public List<LotDto> getAllLots() {
        return lotService.getAllLots();
    }

    @PutMapping
    public LotDto updateLot(@RequestBody LotDto lotDto) {
        return lotService.updateLot(lotDto);
    }

    @DeleteMapping
    public void deleteLot(@RequestParam Integer id) {
        lotService.deleteLot(id);
    }
}
