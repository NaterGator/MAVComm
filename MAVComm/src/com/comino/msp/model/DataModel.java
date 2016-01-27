package com.comino.msp.model;

import java.io.Serializable;

import com.comino.msp.model.segment.Attitude;
import com.comino.msp.model.segment.Battery;
import com.comino.msp.model.segment.GPS;
import com.comino.msp.model.segment.Imu;
import com.comino.msp.model.segment.Raw;
import com.comino.msp.model.segment.Rc;
import com.comino.msp.model.segment.Servo;
import com.comino.msp.model.segment.State;
import com.comino.msp.model.segment.Status;
import com.comino.msp.model.segment.Telemetry;
import com.comino.msp.model.segment.generic.Segment;


// Consolidated device data model after IMU


public class DataModel extends Segment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3439530621929819600L;


	public   Battery     battery  = null;
	public   Attitude   attitude  = null;
	public 	 Imu			 imu  = null;
	public   State		   state  = null;
	public   State	 target_state = null;
	public   Telemetry telemetry  = null;
	public	 GPS	         gps  = null;
	public	 Raw			 raw  = null;
	public   Status          sys  = null;
	public	 Servo			servo = null;
	public	 Rc				  rc  = null;


	public DataModel()  {
		this.battery   		= new Battery();
		this.attitude  		= new Attitude();
		this.imu       		= new Imu();
		this.state     		= new State();
		this.target_state 	= new State();
		this.telemetry 		= new Telemetry();
		this.gps       		= new GPS();
		this.raw            = new Raw();
		this.sys       		= new Status();
		this.servo			= new Servo();
		this.rc             = new Rc();


	}

	public DataModel(DataModel m) {
		this.copy(m);
	}

	public void copy(DataModel m) {	
		this.battery   		= m.battery.clone();	
		this.attitude 		= m.attitude.clone();	
		this.imu       		= m.imu.clone();
		this.state     		= m.state.clone();
		this.target_state   = m.target_state.clone();
		this.telemetry 		= m.telemetry.clone();
		this.gps       		= m.gps.clone();
		this.raw            = m.raw.clone();
		this.sys       		= m.sys.clone();
		this.servo			= m.servo.clone();
		this.rc             = m.rc.clone();
	}
	
	
	public void set(DataModel m) {
		this.battery.set(m.battery);
		this.attitude.set(m.attitude);
		this.imu.set(m.imu);
		this.state.set(m.state);
		this.target_state.set(m.target_state);
		this.telemetry.set(m.telemetry);
		this.gps.set(m.gps);
		this.raw.set(raw);
		this.sys.set(m.sys);
		this.servo.set(m.servo);
		this.rc.set(m.rc);
	}

	public DataModel clone() {
		return new DataModel(this);
	}
	
	public void clear() {
		this.battery.clear();
		this.attitude.clear();
		this.imu.clear();
		this.state.clear();
		this.target_state.clear();
		this.telemetry.clear();
		this.gps.clear();
		this.servo.clear();
		this.rc.clear();
	}
}