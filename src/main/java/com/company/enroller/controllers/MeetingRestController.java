package com.company.enroller.controllers;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;
	
	@Autowired
	ParticipantService participantService;

	@GetMapping
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
	    Meeting meeting = meetingService.findById(id);
	
	    if (meeting == null) { 
		return new ResponseEntity(HttpStatus.NOT_FOUND);
	    } 
	    return new ResponseEntity<Meeting>(meeting, HttpStatus.OK); 
		}

	@PostMapping
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting){
		
		if(meetingService.findById(meeting.getId())!=null) {
			
			return new ResponseEntity<String>("Unable to create. Meeting with Id " + meeting.getId() + " already exists", null);
		}
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") Long id){
		

		Meeting meeting = meetingService.findById(id);
		if(meeting == null) {		
			return new ResponseEntity<String>("Nie ma spotkania o id " + id, HttpStatus.ACCEPTED);
		}
		meetingService.delete(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateMeeting(@PathVariable(value = "id") Long id,  @RequestBody Meeting meeting){
		
		Meeting meetingOriginal = meetingService.findById(id);
		meetingOriginal.setTitle(meeting.getTitle());
		meetingOriginal.setDescription(meeting.getDescription());
		meetingOriginal.setDate(meeting.getDate());
		meetingService.update(meetingOriginal);
		return new ResponseEntity<Meeting>(meetingOriginal, HttpStatus.CREATED);
		
	}
	
	@PostMapping("/{meeting}/{participant}")
	public ResponseEntity<?> addUserToMeeting(@PathVariable(value = "meeting") Long meetingId, 
			@PathVariable(value = "participant") String participant, @RequestBody Meeting meeting){
	
		Meeting meetingData = meetingService.findById(meetingId);
		meetingData.addParticipant(participantService.findByLogin(participant));
		meetingService.add(meetingData);		
		return new ResponseEntity<Meeting>(HttpStatus.OK);
	}
	
	@DeleteMapping("/{meeting}/{participant}")
	public ResponseEntity<?> deleteUserFromMeeting(@PathVariable(value = "meeting") Long meetingId, 
			@PathVariable(value = "participant") String participant){
	
		Meeting meetingData = meetingService.findById(meetingId);
		Participant participantData = participantService.findByLogin(participant);
		if (meetingData == null || participantData == null) { 
					return new ResponseEntity(HttpStatus.NOT_FOUND);
		} 
		meetingData.removeParticipant(participantData);
		meetingService.add(meetingData);		
		return new ResponseEntity<Meeting>(HttpStatus.OK);
	}
	
	@GetMapping("/search/{id}")
	public ResponseEntity<?> getMeetingByParticipant(@PathVariable("id") String login) {
		
		Participant participant = participantService.findByLogin(login);
	
		if (participant == null) { 
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		} 	
		Collection<Meeting> meetings = participant.getMeetings();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK); 
	}
	
	@GetMapping("/title/{id}")
	public ResponseEntity<?> getMeetingByTitle(@PathVariable("id") String text) {
			
		Collection<Meeting> meetings = meetingService.getMeetingsContainingStringInTitle(text);
		if (meetings.isEmpty()) { 
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		} 		
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK); 
	}
	
	@GetMapping("/description/{id}")
	public ResponseEntity<?> getMeetingByDescription(@PathVariable("id") String text) {
				
		Collection<Meeting> meetings = meetingService.getMeetingsContainingStringInDescription(text);
		if (meetings.isEmpty()) { 
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		} 	
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK); 
	}
	@GetMapping("/sorted")
	public ResponseEntity<?> getMeetingsSorted() {
			
		Collection<Meeting> meetings = meetingService.getMeetingsSorted();
		if (meetings.isEmpty()) { 
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		} 
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK); 
	}
	
}
