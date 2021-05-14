package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);	
		return query.list();
		
		
	}

    public Meeting findById(Long id) {
		
		Meeting meeting = (Meeting) connector.getSession().get(Meeting.class, id);
		return meeting;
		
	}
	
	public void add(Meeting meeting) {
		
		Transaction transaction = this.connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
		
	}
	
	public void update(Meeting meeting) {
		
		Transaction transaction = this.connector.getSession().beginTransaction();
		connector.getSession().update(meeting);;
		transaction.commit();
		
	}

	public void delete(Meeting meeting) {
		
		Transaction transaction = this.connector.getSession().beginTransaction();
		connector.getSession().delete(meeting);
		transaction.commit();
			
	}
	
	public Collection<Meeting> getMeetingsContainingStringInTitle(String text) {
		
		String hql = "FROM Meeting where title like:id";
		Query query = connector.getSession().createQuery(hql).setParameter("id", text);	
		return query.list();
		
	}

	public Collection<Meeting> getMeetingsContainingStringInDescription(String text) {
		
		String hql = "FROM Meeting where description like:id";
		Query query = connector.getSession().createQuery(hql).setParameter("id", text);	
		return query.list();
		
	}

	public Collection<Meeting> getMeetingsSorted() {
		
		String hql = "FROM Meeting order by title";
		Query query = connector.getSession().createQuery(hql);			
		return query.list();
		
	}
	
}
