package nst.springboot.restexample01.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nst.springboot.restexample01.dto.MemberDto;
import nst.springboot.restexample01.service.MemberService;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberDto> save(@RequestBody MemberDto member) throws IllegalArgumentException {
        return ResponseEntity.ok(memberService.save(member));
    }

    @GetMapping
    public ResponseEntity<List<MemberDto>> getAll() {
        return ResponseEntity.ok(memberService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> findById(@PathVariable Long id) throws NoSuchElementException {
        return ResponseEntity.ok(memberService.findById(id));
    }

    @PatchMapping
    public ResponseEntity<MemberDto> update(@RequestBody MemberDto member) throws NoSuchElementException {
        return ResponseEntity.ok(memberService.update(member));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws IllegalArgumentException, NoSuchElementException {
        memberService.delete(id);
        return ResponseEntity.ok("Member removed!");
    }

}
