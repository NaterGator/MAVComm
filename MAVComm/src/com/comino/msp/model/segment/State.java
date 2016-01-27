package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class State extends Segment {
	

	private static final long serialVersionUID = 5910084328085663916L;
	
	public static final int 	STATE_Z_AVAILABLE  = 0;
	public static final int 	STATE_H_AVAILABLE  = 1;
	public static final int		STATE_XY_AVAILABLE = 2;

	// flags

	private int		flags=0;		// flags

	// positioning actual

	public float	x=0;			// relative x-position in m (Roll)
	public float    y=0;			// relative y-position in m (Pitch)
	public float    z=0;			// relative z-position in m (Altitude)
	public float    h=0;			// heading in radiant

	public float    vx=0;			// relative x speed in m/s
	public float 	vy=0;			// relative y speed in m/s
	public float	vz=0;			// relative z speed in m/s
	public float	vh=0;			// relative heading speed in radiant/s
	

	// helpers

	public void  setFlag(int box, boolean val) {
		if(val)
			flags = (short) (flags | (1<<box));
		else
			flags = (short) (flags & ~(1<<box));
	}

	public boolean isStateValid(int ...box) {
		for(int b : box)
			if((flags & (1<<b))==0)
				return false;
		return true;
	}
	


	public State clone() {
		State t = new State();
		t.flags = flags;
		t.x		= x;
		t.y		= y;
		t.z		= z;
		t.h		= h;
		t.vx	= vx;
		t.vy	= vy;
		t.vz	= vz;
		t.vh	= vh;
		return t;
	}

	public void set(State t) {
		flags = t.flags;
		x		= t.x;
		y		= t.y;
		z		= t.z;
		h		= t.h;
		vx		= t.vx;
		vy		= t.vy;
		vz		= t.vz;
		vh		= t.vh;
	}

	public void clear() {
		flags 	= 0;
		x		= 0;
		y		= 0;
		z		= 0;
		h		= 0;
		vx		= 0;
		vy		= 0;
		vz		= 0;
		vh		= 0;
	}
	
	public void print(String header) {
		System.out.printf("%s State: x= %3.2f y=%3.2f z=%3.2f h=%3.2f - vx= %3.2f vy=%3.2f vz=%3.2f vh=%3.2f \n", 
				header,x,y,z,h,vx,vy,vz,vh);
	}

}