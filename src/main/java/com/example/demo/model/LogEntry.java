package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity(name="LogEntries")
@Table(name="LogEntries")
@Inheritance(strategy = InheritanceType.JOINED)
public class LogEntry {

	@Id
	@Column(name="log_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	//Might change this type to Date
	@Column(name="log_time")
	private String time;

	@Column(name="log_lvl")
	private String level;
	
	
	@Column(name="log_process")
	private String process;
	
	@Column(name = "log_class")
	private String className;
	
	@Column(name="log_message" , length=65536)
	private String message;

	 public LogEntry() {
		// TODO Auto-generated constructor stub
	}	
	
	public LogEntry(String time, String level, String process, String className, String message) {
		super();
		this.time = time;
		this.level = level;
		this.process = process;
		this.className = className;
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	@Override
	public String toString() {
		return "\nLogEntry [\nid=" + id + ",\n time=" + time + ",\n level=" + level + ",\n process=" + process + ",\n className="
				+ className + ",\n message=" + message + "]\n";
	}
	
	
	
	
	
	
	
}
