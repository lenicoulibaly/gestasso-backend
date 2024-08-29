package rigeldevsolutions.gestasso.grademodule.controller.service;

import org.springframework.context.annotation.Profile;
import rigeldevsolutions.gestasso.grademodule.controller.repositories.GradeRepo;
import rigeldevsolutions.gestasso.grademodule.model.dtos.CreateGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.dtos.GrageMapper;
import rigeldevsolutions.gestasso.grademodule.model.dtos.ReadGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.dtos.UpdateGradeDTO;
import rigeldevsolutions.gestasso.grademodule.model.entities.Grade;
import rigeldevsolutions.gestasso.grademodule.model.enums.Categorie;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.sharedmodule.enums.PersStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service("gradeService")
@RequiredArgsConstructor
public class GradeService implements IGradeService
{
    private final GradeRepo gradeRepo;
    private final GrageMapper grageMapper;
    @Override
    public List<ReadGradeDTO> getAllGrades() {
        return gradeRepo.findAll().stream().map(grageMapper::mapToReadGradeDTO).collect(Collectors.toList());
    }

    @Override
    public ReadGradeDTO getGradeByGradeCode(String gradeCode)
    {
        Grade grade = gradeRepo.findById(gradeCode).orElse(null);
        return grade == null ? null : grageMapper.mapToReadGradeDTO(grade);
    }

    @Override
    public List<ReadGradeDTO> getGradesByCategoryName(String catName)
    {
        Categorie categorie = EnumUtils.getEnum(Categorie.class, catName);
        return gradeRepo.findByCategorie(categorie).stream().map(grageMapper::mapToReadGradeDTO).collect(Collectors.toList());
    }

    //========================================================================================

    //@Override
    public Page<ReadGradeDTO> searchPageOfGrades(String key, int pageNum, int pageSize)
    {
        Page<Grade> grades = gradeRepo.searchPageOfGrades(key, PageRequest.of(pageNum, pageSize));
        List<ReadGradeDTO> gradeDTOS = grades.stream().map(grageMapper::mapToReadGradeDTO).collect(Collectors.toList());
        return new PageImpl<>(gradeDTOS, PageRequest.of(pageNum, pageSize), grades.getTotalElements());
    }

    //@Override @Transactional
    public ReadGradeDTO createGrade(CreateGradeDTO dto)
    {
        Grade grade = gradeRepo.save(grageMapper.mapToGrade(dto));
        return grageMapper.mapToReadGradeDTO(grade);
    }

    //@Override @Transactional
    public ReadGradeDTO updateGrade(UpdateGradeDTO dto) {
        Grade grade = gradeRepo.findById(dto.getGradeCode()).orElse(null);
        if (grade == null) return null;
        grade.setNomGrade(dto.getCategorie() + dto.getRang());
        grade.setCategorie(EnumUtils.getEnum(Categorie.class, dto.getCategorie()));
        grade.setRang(dto.getRang());
        return grageMapper.mapToReadGradeDTO(grade);
    }
}
