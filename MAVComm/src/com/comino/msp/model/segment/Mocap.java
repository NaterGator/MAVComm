/****************************************************************************
 *
 *   Copyright (c) 2016 Eike Mansfeld ecm@gmx.de. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 ****************************************************************************/

package com.comino.msp.model.segment;

import com.comino.msp.model.segment.generic.Segment;

public class Mocap extends Segment {

	private static final long serialVersionUID = 270248566309263309L;

	public static final int MOCAP_VALID = 1;
	public static final int MOCAP_COLLISION_WARNING = 2;

	public float vx = 0;
	public float vy = 0;
	public float vz = 0;

	public int   flags = 0;
	public float fps = 0;


	public void set(Mocap a) {
		vx = a.vx;
		vy = a.vy;
		vz = a.vz;

		flags = a.flags;
		fps   = a.fps;
	}

	public Mocap clone() {
		Mocap a = new Mocap();
		a.vx = vx;
		a.vy = vy;
		a.vz = vz;

		a.flags = flags;
		a.fps = fps;
		return a;
	}


	public void clear() {
		vx = 0;
		vy = 0;
		vz = 0;

		flags = 0;
		fps = 0;
	}

	public void  setStatus(int box, boolean val) {
		if(val)
			flags = (int) (flags | (1<<box));
		else
			flags = (int) (flags & ~(1<<box));
	}

	public boolean isStatus(int ...box) {
		for(int b : box)
		  if((flags & (1<<b))==0)
            return false;
		return true;
	}



}
