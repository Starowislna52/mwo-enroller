package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@GetMapping
	public ResponseEntity<?> getParticipants() {
		Collection<Participant> participants = participantService.getAll();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getMeeting(@PathVariable("id") String login) {
	    Participant participant = participantService.findByLogin(login);
	if (participant == null) { 
	return new ResponseEntity(HttpStatus.NOT_FOUND);
	} 

	return new ResponseEntity<Participant>(participant, HttpStatus.OK); 
	}

	//POST http://localhost:8080/participants
	@PostMapping
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant){
		
		if(participantService.findByLogin(participant.getLogin())!=null) {
			
			return new ResponseEntity<String>("Unable to create. Participant with login " + participant.getLogin() + " already exists", null);
		}

		participantService.add(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login){
		
		Participant participant = participantService.findByLogin(login);
		if(participant == null) 
		{
			
			return new ResponseEntity<String>("Nie ma uczestnika o loginie " + login, HttpStatus.ACCEPTED);
		}

		participantService.delete(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
		
	}
	
    
}
