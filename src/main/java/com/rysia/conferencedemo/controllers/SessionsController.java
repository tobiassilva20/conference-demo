package com.rysia.conferencedemo.controllers;

import com.rysia.conferencedemo.dto.SessionDTO;
import com.rysia.conferencedemo.models.Session;
import com.rysia.conferencedemo.repositories.SessionRepository;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class SessionsController {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @ApiOperation(value = "LIST ALL SESSIONS")
    @GetMapping("/sessions")
    @ResponseBody
    public ResponseEntity<List<Session>> list() {
        List<Session> sessions = this.sessionRepository.findAll();
        return sessions.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<List<Session>>(sessions, HttpStatus.OK);
    }

    @ApiOperation(value = "GET A UNIQUE SESSION")
    @GetMapping("/session/{id}")
    @ResponseBody
    public ResponseEntity<Session> get(@PathVariable(value = "id") Long id) {
        Optional<Session> optionalSession = this.sessionRepository.findById(id);
        return !optionalSession.isPresent() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<Session>(optionalSession.get(), HttpStatus.OK);
    }

    @ApiOperation(value = "CREATE A SESSION")
    @PostMapping("/session")
    public ResponseEntity<Session> create(@RequestBody @Valid final SessionDTO sessionDTO) {
        return new ResponseEntity<Session>(this.sessionRepository.saveAndFlush(convertToEntity(sessionDTO)),
                HttpStatus.CREATED);
    }

    @ApiOperation(value = "UPDATE A SESSION")
    @RequestMapping(value = "/session/{id}", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
    public ResponseEntity<Session> update(@PathVariable(value = "id") Long id, @RequestBody @Valid SessionDTO sessionDTO) {
        Optional<Session> optionalSession = this.sessionRepository.findById(id);
        boolean existsSession = optionalSession.isPresent();
        Session existingSession = new Session();
        if (existsSession) {
            existingSession = optionalSession.get();
            BeanUtils.copyProperties(convertToEntity(sessionDTO), existingSession, "session_id");
        }
        return existsSession ? new ResponseEntity<Session>(this.sessionRepository.saveAndFlush(existingSession), HttpStatus.ACCEPTED)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "DELETE A SESSION")
    @RequestMapping(value = "/session/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        if (Optional.ofNullable(this.sessionRepository.findById(id)).isPresent()) {
            this.sessionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private Session convertToEntity(SessionDTO sessionDTO) {
        Session session = modelMapper.map(sessionDTO, Session.class);
        return session;
    }
}
