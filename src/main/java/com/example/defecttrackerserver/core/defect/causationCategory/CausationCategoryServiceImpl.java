package com.example.defecttrackerserver.core.defect.causationCategory;

import com.example.defecttrackerserver.core.defect.defect.DefectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CausationCategoryService}.
 */
@Service
@RequiredArgsConstructor
class CausationCategoryServiceImpl implements CausationCategoryService {
    private final CausationCategoryRepository causationCategoryRepository;
    private final DefectRepository defectRepository;
    private final CausationCategoryMapper causationCategoryMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CausationCategoryDto saveCausationCategory(@Valid CausationCategoryDto causationCategoryDto) {
        if(causationCategoryRepository.findByName(causationCategoryDto.getName()).isPresent())
            throw new DuplicateKeyException("CausationCategory already exists with name: " + causationCategoryDto.getName());

        CausationCategory causationCategory = new CausationCategory();
        causationCategory.setName(causationCategoryDto.getName());

        CausationCategory savedCausationCategory = causationCategoryRepository.save(causationCategory);

        return causationCategoryMapper.mapToDto(savedCausationCategory);
    }

    @Override
    public CausationCategoryDto getCausationCategoryById(Integer id) {
        CausationCategory causationCategory = findCausationCategoryById(id);
        return causationCategoryMapper.mapToDto(causationCategory);
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
        CausationCategory causationCategory = findCausationCategoryById(causationCategoryId);

        Optional<CausationCategory> causationCategoryExists = causationCategoryRepository.findByName(causationCategoryDto.getName());
        if(causationCategoryExists.isPresent() && !causationCategoryExists.get().getId().equals(causationCategory.getId()))
            throw new DuplicateKeyException("CausationCategory already exists with name: " + causationCategoryDto.getName());

        causationCategory.setName(causationCategoryDto.getName());

        CausationCategory savedCausationCategory = causationCategoryRepository.save(causationCategory);

        return causationCategoryMapper.mapToDto(savedCausationCategory);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCausationCategory(Integer id) {
        CausationCategory causationCategory = findCausationCategoryById(id);

        if(!defectRepository.findByCausationCategoryId(id).isEmpty())
            throw new UnsupportedOperationException("CausationCategory cannot be deleted because it is used in Defects");

        //'Undefined' must not be deleted because it is a default value
        if(causationCategory.getName().equals("Undefined"))
            throw new UnsupportedOperationException("CausationCategory 'Undefined' cannot be deleted because" +
                    "it is a default value.");

        causationCategoryRepository.delete(causationCategory);
    }

    private CausationCategory findCausationCategoryById(Integer id) {
        return causationCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CausationCategory not found with id: " + id));
    }
}
