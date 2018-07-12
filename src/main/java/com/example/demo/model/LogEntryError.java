package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name="LogEntryError")
public class LogEntryError extends LogEntry{

		@Column(name="exception" , length=65536)
		private String exception;

		public LogEntryError() {
			// TODO Auto-generated constructor stub
		}
		public LogEntryError(String exception) {
			super();
			this.exception = exception;
		}
		public LogEntryError(String time, String level, String process, String className, String message , String exception) {
			super(time, level, process, className, message);
			this.exception = exception;
		}
		public LogEntryError(String time, String level, String process, String className, String message , String exception, int serviceId) {
			super(time, level, process, className, message,serviceId);
			this.exception = exception;
		}


		public String getException() {
			return exception;
		}

		public void setException(String exception) {
			this.exception = exception;
		}

		@Override
		public String toString() {
			return super.toString()+"LogEntryError [exception=" + exception + "]";
		}
		
		
}
