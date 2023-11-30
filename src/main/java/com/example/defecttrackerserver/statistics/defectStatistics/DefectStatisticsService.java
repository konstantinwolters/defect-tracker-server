package com.example.defecttrackerserver.statistics.defectStatistics;

import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefectStatisticsService {
    private final DefectRepository defectRepository;


}
