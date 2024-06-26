package nst.springboot.restexample01.service.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nst.springboot.restexample01.converter.impl.AcademicTitleConverter;
import nst.springboot.restexample01.converter.impl.DepartmentConverter;
import nst.springboot.restexample01.converter.impl.EducationTitleConverter;
import nst.springboot.restexample01.converter.impl.MemberConverter;
import nst.springboot.restexample01.converter.impl.ScientificFieldConverter;
import nst.springboot.restexample01.domain.AcademicTitleHistory;
import nst.springboot.restexample01.domain.Member;
import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.repository.AcademicTitleHistoryRepository;
import nst.springboot.restexample01.repository.AcademicTitleRepository;
import nst.springboot.restexample01.repository.DepartmentRepository;
import nst.springboot.restexample01.repository.EducationTitleRepository;
import nst.springboot.restexample01.repository.MemberRepository;
import nst.springboot.restexample01.repository.ScientificFieldRepository;
import nst.springboot.restexample01.service.MemberService;
import nst.springboot.restexample01.utils.ExceptionMessagesConstants;

@Service
public class MemberServiceImpl implements MemberService {

        private final MemberRepository memberRepository;

        private final MemberConverter memberConverter;

        private final EducationTitleConverter educationTitleConverter;

        private final EducationTitleRepository educationTitleRepository;

        private final AcademicTitleConverter academicTitleConverter;

        private final AcademicTitleRepository academicTitleRepository;

        private final ScientificFieldConverter scientificFieldConverter;

        private final ScientificFieldRepository scientificFieldRepository;

        private final DepartmentConverter departmentConverter;

        private final DepartmentRepository departmentRepository;

        private final AcademicTitleHistoryRepository academicTitleHistoryRepository;

        @Autowired
        public MemberServiceImpl(MemberRepository memberRepository, MemberConverter memberConverter,
                        EducationTitleConverter educationTitleConverter,
                        EducationTitleRepository educationTitleRepository,
                        AcademicTitleConverter academicTitleConverter, AcademicTitleRepository academicTitleRepository,
                        ScientificFieldConverter scientificFieldConverter,
                        ScientificFieldRepository scientificFieldRepository,
                        DepartmentConverter departmentConverter, DepartmentRepository departmentRepository,
                        AcademicTitleHistoryRepository academicTitleHistoryRepository) {
                this.memberRepository = memberRepository;
                this.memberConverter = memberConverter;
                this.educationTitleConverter = educationTitleConverter;
                this.educationTitleRepository = educationTitleRepository;
                this.academicTitleConverter = academicTitleConverter;
                this.academicTitleRepository = academicTitleRepository;
                this.scientificFieldConverter = scientificFieldConverter;
                this.scientificFieldRepository = scientificFieldRepository;
                this.departmentConverter = departmentConverter;
                this.departmentRepository = departmentRepository;
                this.academicTitleHistoryRepository = academicTitleHistoryRepository;
        }

        @Override
        public MemberDto save(MemberDto memberDto) throws IllegalArgumentException {
                if (memberDto == null) {
                        throw new IllegalArgumentException("Member cannot be null!");
                }
                memberDto.setId(null);
                Member saved = memberRepository.save(memberConverter.toEntity(this.fillDto(memberDto)));

                academicTitleHistoryRepository.save(new AcademicTitleHistory(null,
                                new Date(), null, saved, saved.getAcademicTitle(), saved.getScientificField()));

                return memberConverter.toDto(saved);
        }

        @Override
        public List<MemberDto> getAll() {
                return memberRepository.findAll().stream()
                                .map(memberConverter::toDto).toList();
        }

        @Override
        public MemberDto update(MemberDto memberDto) throws IllegalArgumentException, NoSuchElementException {
                if (memberDto == null || memberDto.getId() == null) {
                        throw new IllegalArgumentException("Member Id cannot be null!");
                }
                if(memberRepository.findById(memberDto.getId()).isEmpty()) {
                        throw new NoSuchElementException(ExceptionMessagesConstants.MEMBER_NOT_EXIST);
                }

                Member updated = memberRepository.save(memberConverter.toEntity(this.fillDto(memberDto)));

                academicTitleHistoryRepository.findLastHistory(updated.getId())
                                .ifPresent(history -> {
                                        history.setEndDate(new Date());
                                        academicTitleHistoryRepository.save(history);
                                });
                academicTitleHistoryRepository.save(new AcademicTitleHistory(null,
                                new Date(), null, updated, updated.getAcademicTitle(), updated.getScientificField()));

                return memberConverter.toDto(updated);
        }

        @Override
        public boolean delete(Long id) throws NoSuchElementException {
                Member member = memberRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException(ExceptionMessagesConstants.MEMBER_NOT_EXIST));
                memberRepository.delete(member);
                return true;
        }

        @Override
        public MemberDto findById(Long id) throws NoSuchElementException {
                Member member = memberRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException(ExceptionMessagesConstants.MEMBER_NOT_EXIST));
                return memberConverter.toDto(member);
        }

        private MemberDto fillDto(MemberDto memberDto) {
                memberDto.setEducationTitle(educationTitleConverter.toDto(educationTitleRepository
                                .findById(memberDto.getEducationTitle().getId())
                                .orElseThrow(() -> new NoSuchElementException("Education Title does not exist!"))));
                memberDto.setAcademicTitle(academicTitleConverter.toDto(academicTitleRepository
                                .findById(memberDto.getAcademicTitle().getId())
                                .orElseThrow(() -> new NoSuchElementException("Academic Title does not exist!"))));
                memberDto.setScientificField(scientificFieldConverter.toDto(scientificFieldRepository
                                .findById(memberDto.getScientificField().getId())
                                .orElseThrow(() -> new NoSuchElementException("Scientific Field does not exist!"))));
                memberDto.setDepartment(departmentConverter.toDto(departmentRepository
                                .findById(memberDto.getDepartment().getId())
                                .orElseThrow(() -> new NoSuchElementException("Department does not exist!"))));
                return memberDto;
        }
}
