package com.org.verdaflow.rest.entity;

import static org.joda.time.DateTimeZone.UTC;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;

@Entity
@Table(name = "registered_scheduler")
@NamedQuery(name = "RegisteredScheduler.findAll", query = "SELECT m FROM RegisteredScheduler m")
public class RegisteredScheduler implements Serializable {

	private static final long serialVersionUID = -6666637843591286669L;

	public static final String MASTER_SCHEDULER_TYPE = "M";

	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = "scheduler_name")
	@Length(max = 32)
	private String schedulerName;

	@Column(name = "scheduler_type")
	@Length(max = 1)
	private String schedulerType;

	@Column(name = "heart_beat")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime heartbeat = DateTime.now(UTC);

	RegisteredScheduler(final String schedulerType) {
		this.schedulerType = schedulerType;
	}

	public RegisteredScheduler(Integer id, String schedulerName, String schedulerType, DateTime heartbeat) {
		this.id = id;
		this.schedulerName = schedulerName;
		this.schedulerType = schedulerType;
		this.heartbeat = heartbeat;
	}

	public RegisteredScheduler() {
	}

	public static RegisteredScheduler newInstanceOfMasterScheduler() {
		return new RegisteredScheduler(MASTER_SCHEDULER_TYPE);
	}

	public Integer getId() {
		return this.id;
	}

	public String getSchedulerName() {
		return this.schedulerName;
	}

	public String getSchedulerType() {
		return this.schedulerType;
	}

	public DateTime getHeartbeat() {
		return this.heartbeat;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

	public void setSchedulerType(String schedulerType) {
		this.schedulerType = schedulerType;
	}

	public void setHeartbeat(DateTime heartbeat) {
		this.heartbeat = heartbeat;
	}
}
