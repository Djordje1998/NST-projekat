package nst.springboot.restexample01.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nst.springboot.restexample01.converter.DtoEntityConverter;
import nst.springboot.restexample01.domain.AcademicTitleHistory;
import nst.springboot.restexample01.dto.AcademicTitleHistoryDto;

@Component
public class AcademicTitleHistoryConverter implements DtoEntityConverter<AcademicTitleHistoryDto, AcademicTitleHistory> {

    private final AcademicTitleConverter academicTitleConverter;

    private final ScientificFieldConverter scientificFieldConverter;

    private final MemberConverter memberConverter;

    @Autowired
    public AcademicTitleHistoryConverter(AcademicTitleConverter academicTitleConverter,
            ScientificFieldConverter scientificFieldConverter, MemberConverter memberConverter) {
        this.academicTitleConverter = academicTitleConverter;
        this.scientificFieldConverter = scientificFieldConverter;
        this.memberConverter = memberConverter;
    }

    @Override
    public AcademicTitleHistoryDto toDto(AcademicTitleHistory e) {
        return new AcademicTitleHistoryDto(
                e.getId(),
                e.getStartDate(),
                e.getEndDate(),
                memberConverter.toDto(e.getMember()),
                academicTitleConverter.toDto(e.getAcademicTitle()),
                scientificFieldConverter.toDto(e.getScientificField()));
    }

    @Override
    public AcademicTitleHistory toEntity(AcademicTitleHistoryDto t) {
        return new AcademicTitleHistory(
                t.getId(),
                t.getStartDate(),
                t.getEndDate(),
                memberConverter.toEntity(t.getMember()),
                academicTitleConverter.toEntity(t.getAcademicTitle()),
                scientificFieldConverter.toEntity(t.getScientificField()));
    }

}
