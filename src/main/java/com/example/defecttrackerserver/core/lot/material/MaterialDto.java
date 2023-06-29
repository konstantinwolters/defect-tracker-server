package com.example.defecttrackerserver.core.lot.material;

import com.example.defecttrackerserver.core.lot.lot.Lot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MaterialDto {
    private Integer id;
    private String name;
}
