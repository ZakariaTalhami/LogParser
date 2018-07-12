package com.example.demo.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "LogEntries")
@Table(name = "LogEntries")
@Inheritance(strategy = InheritanceType.JOINED)
public class LogEntry {

	@Id
	@Column(name = "log_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "log_lvl")
	private String level;

	@Column(name = "log_process")
	private String process;

	@Column(name = "log_class")
	private String className;

	@Column(name = "log_message", length = 65536)
	private String message;

	@ManyToOne(cascade = CascadeType.MERGE)
	private ServiceModel service;

	@Column(name = "time")
	private Date timestamp;
	
	@Column(name = "time_ms")
	private int mSec;

	public LogEntry() {

	}

	public LogEntry(String time, String level, String process, String className, String message) {
		super();
		setTimestamp(time);
		setmSec(time);
		this.level = level;
		this.process = process;
		this.className = className;
		this.message = message;
	}

	public LogEntry(String time, String level, String process, String className, String message, int serviceId) {
		super();
		setTimestamp(time);
		setmSec(time);
		this.level = level;
		this.process = process;
		this.className = className;
		this.message = message;
		this.service = new ServiceModel(serviceId, "");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ServiceModel getService() {
		return service;
	}

	public void setService(ServiceModel service) {
		this.service = service;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public void setTimestamp(String timestamp) {
//		2018-07-09 11:45:46,529 
//		dd-MMM-yyyy HH:mm:ss
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
		try {
			this.timestamp =formater.parse(timestamp);	
		} catch (Exception e) {
			this.timestamp = null;
		}
		
	}
	
	public int getmSec() {
		return mSec;
	}

	public void setmSec(int mSec) {
		this.mSec = mSec;
	}
	public void setmSec(String mSec) {
		this.mSec = Integer.parseInt(mSec.split(",")[1]);
	}

	@Override
	public String toString() {
		return "LogEntry [id=" + id + ",\n time=" + timestamp+", "+mSec + ",\n level=" + level + ",\n process=" + process
				+ ",\n className=" + className + ",\n message=" + message + ",\n service=" + service + "]";
	}

}
