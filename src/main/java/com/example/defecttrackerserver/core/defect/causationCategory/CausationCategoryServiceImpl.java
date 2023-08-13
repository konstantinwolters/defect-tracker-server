package com.example.defecttrackerserver.core.defect.causationCategory;

import com.example.defecttrackerserver.core.defect.causationCategory.causationCategoryException.CausationCategoryExistsException;
import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CausationCategoryServiceImpl implements CausationCategoryService {
    private final CausationCategoryRepository causationCategoryRepository;
    private final DefectRepository defectRepository;
    private final CausationCategoryMapper causationCategoryMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CausationCategoryDto saveCausationCategory(@Valid CausationCategoryDto causationCategoryDto) {
        if(causationCategoryRepository.findByName(causationCategoryDto.getName()).isPresent())
            throw new CausationCategoryExistsException("CausationCategory already exists with name: " + causationCategoryDto.getName());

        CausationCategory causationCategory = new CausationCategory();
        causationCategory.setName(causationCategoryDto.getName());

        CausationCategory savedCausationCategory = causationCategoryRepository.save(causationCategory);

        return causationCategoryMapper.mapToDto(savedCausationCategory);
    }

    @Override
    public CausationCategoryDto getCausationCategoryById(Integer id) {
        return causationCategoryRepository.findById(id)
                .map(causationCategoryMapper::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("CausationCategory not found with id: " + id));
    }

    @Override
    public List<CausationCategoryDto> getAllCausationCategories() {
        return causationCategoryRepository.findAll()
                .stream()
                .map(causationCategoryMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CausationCategoryDto updateCausationCategory(Integer causationCategoryId, @Valid CausationCategoryDto causationCategoryDto) {
        if(causationCategoryDto.getId() == null)
            throw new IllegalArgumentException("CausationCategory id must not be null");

        CausationCategory causationCategory = causationCategoryRepository.findById(causationCategoryId)
                .orElseThrow(()-> new EntityNotFoundException("CausationCategory not found with id: "
                        + causationCategoryId));

        Optional<CausationCategory> causationCategoryExists = causationCategoryRepository.findByName(causationCategoryDto.getName());
        if(causationCategoryExists.isPresent() && !causationCategoryExists.get().getId().equals(causationCategory.getId()))
            throw new CausationCategoryExistsException("CausationCategory already exists with name: " + causationCategoryDto.getName());

        causationCategory.setName(causationCategoryDto.getName());

        CausationCategory savedCausationCategory = causationCategoryRepository.save(causationCategory);

        return causationCategoryMapper.mapToDto(savedCausationCategory);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCausationCategory(Integer id) {
        CausationCategory causationCategory = causationCategoryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("CausationCategory not found with id: " + id));

        if(!defectRepository.findByCausationCategoryId(id).isEmpty())
            throw new UnsupportedOperationException("CausationCategory cannot be deleted because it is used in Defects");

        causationCategoryRepository.delete(causationCategory);
    }
}
