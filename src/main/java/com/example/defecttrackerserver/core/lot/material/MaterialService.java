package com.example.defecttrackerserver.core.lot.material;

import java.util.List;

public interface MaterialService {
    MaterialDto saveMaterial(MaterialDto materialDto);
    MaterialDto getMaterialById(Integer id);
    List<MaterialDto> getAllMaterials();
    MaterialDto updateMaterial(MaterialDto materialDto);
    void deleteMaterial(Integer id);
}
