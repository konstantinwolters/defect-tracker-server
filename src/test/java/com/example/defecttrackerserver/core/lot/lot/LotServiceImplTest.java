package com.example.defecttrackerserver.core.lot.lot;

import com.example.defecttrackerserver.TestHelper;
import com.example.defecttrackerserver.core.lot.lot.dto.LotDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LotServiceImplTest {

    @Mock
    private LotRepository lotRepository;

    @Mock
    private LotMapper lotMapper;

    @InjectMocks
    private LotServiceImpl lotService;

    private LotDto lotDto;
    private Lot lot;

    @BeforeEach
    void setUp() {
        lotDto = TestHelper.setUpLotDto();
        lot = TestHelper.setUpLot();
    }

    @Test
    void shouldSaveLot() {
        when(lotRepository.save(any(Lot.class))).thenReturn(lot);
        when(lotMapper.map(any(LotDto.class), any(Lot.class))).thenReturn(lot);
        when(lotMapper.mapToDto(any(Lot.class))).thenReturn(lotDto);

        LotDto result = lotService.saveLot(lotDto);

        assertNotNull(result);
        assertEquals(lot.getId(), result.getId());
        verify(lotRepository, times(1)).save(any(Lot.class));
    }

    @Test
    void shouldReturnLotById() {
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.of(lot));
        when(lotMapper.mapToDto(lot)).thenReturn(lotDto);

        LotDto result = lotService.getLotById(1);

        assertNotNull(result);
        assertEquals(lot.getId(), result.getId());
    }

    @Test
    void shouldReturnAllLots(){
        when(lotRepository.findAll()).thenReturn(Arrays.asList(lot));
        when(lotMapper.mapToDto(lot)).thenReturn(lotDto);

        List<LotDto> result = lotService.getAllLots();

        assertNotNull(result);
        assertEquals(lot.getId(), result.get(0).getId());
    }

    @Test
    void shouldUpdateLocation() {
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.of(lot));
        when(lotRepository.save(any(Lot.class))).thenReturn(lot);
        when(lotMapper.map(any(LotDto.class), any(Lot.class))).thenReturn(lot);

        when(lotMapper.mapToDto(any(Lot.class))).thenReturn(lotDto);

        LotDto result = lotService.updateLot(1, lotDto);

        assertNotNull(result);
        assertEquals(lot.getId(), result.getId());
        verify(lotRepository, times(1)).save(lot);
    }

    @Test
    void shouldDeleteLocation() {
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.of(lot));

        lotService.deleteLot(1);

        verify(lotRepository, times(1)).delete(lot);
    }

    @Test
    void shouldThrowExceptionWhenLotNotFound(){
        when(lotRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> lotService.deleteLot(1));
    }
}
