package com.example.defecttrackerserver.core.lot.material;

import java.util.List;

/**
 * Service interface for managing {@link Material}.
 */
interface MaterialService {
    MaterialDto saveMaterial(MaterialDto materialDto);
    MaterialDto getMaterialById(Integer id);
    List<MaterialDto> getAllMaterials();
    MaterialDto updateMaterial(Integer materialId, MaterialDto materialDto);
    void deleteMaterial(Integer id);
}
