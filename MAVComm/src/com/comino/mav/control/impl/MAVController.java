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


package com.comino.mav.control.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.lquac.msg_command_long;
import org.mavlink.messages.lquac.msg_msp_command;

import com.comino.mav.comm.IMAVComm;
import com.comino.mav.comm.udp.MAVUdpCommNIO;
import com.comino.mav.control.IMAVController;
import com.comino.msp.main.control.listener.IMAVLinkListener;
import com.comino.msp.main.control.listener.IMAVMessageListener;
import com.comino.msp.main.control.listener.IMSPModeChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;


public class MAVController implements IMAVController {

	protected String peerAddress = null;

	protected static IMAVController controller = null;
	protected IMAVComm comm = null;

	protected ModelCollectorService collector = null;

	protected   boolean isSITL = false;
	protected   DataModel model = null;

	protected   int commError = 0;

	public static IMAVController getInstance() {
		return controller;
	}


	public MAVController() {
		controller = this;
		model = new DataModel();
		collector = new ModelCollectorService(model);
	}


	@Override
	public boolean sendMAVLinkMessage(MAVLinkMessage msg) {

		if(!controller.getCurrentModel().sys.isStatus(Status.MSP_CONNECTED)) {
			System.out.println("Command rejected. No connection.");
			return false;
		}

		try {
			comm.write(msg);
		//	System.out.println("Execute: "+msg.toString());
			return true;
		} catch (IOException e1) {
			commError++;
			System.out.println("Command not executed. "+e1.getMessage());
			return false;
		}

	}

	@Override
	public boolean sendMAVLinkCmd(int command, float...params) {


		msg_command_long cmd = new msg_command_long(255,1);
		cmd.target_system = 1;
		cmd.target_component = 1;
		cmd.command = command;
		cmd.confirmation = 1;

		for(int i=0; i<params.length;i++) {
			switch(i) {
			case 0: cmd.param1 = params[0]; break;
			case 1: cmd.param2 = params[1]; break;
			case 2: cmd.param3 = params[2]; break;
			case 3: cmd.param4 = params[3]; break;
			case 4: cmd.param5 = params[4]; break;
			case 5: cmd.param6 = params[5]; break;
			case 6: cmd.param7 = params[6]; break;

			}
		}

		return sendMAVLinkMessage(cmd);
	}

	public boolean sendMSPLinkCmd(int command, float...params) {

		if(!controller.getCurrentModel().sys.isStatus(Status.MSP_CONNECTED)) {
			System.out.println("Command rejected. No connection.");
			return false;
		}

		msg_msp_command cmd = new msg_msp_command(255,1);
		cmd.command = command;

		for(int i=0; i<params.length;i++) {
			switch(i) {
			case 0: cmd.param1 = params[0]; break;
			case 1: cmd.param2 = params[1]; break;
			case 2: cmd.param3 = params[2]; break;
			case 3: cmd.param4 = params[3]; break;
			case 4: cmd.param5 = params[4]; break;
			case 5: cmd.param6 = params[5]; break;

			}
		}

		try {
			if(!(comm instanceof MAVUdpCommNIO))
				throw new IOException("MSP Commands only via UDP to proxy allowed");

			comm.write(cmd);
			System.out.println("Execute: "+cmd.toString());
			return true;
		} catch (IOException e1) {
			commError++;
			System.out.println("Command rejected: "+e1.getMessage());
			return false;
		}
	}

	@Override
	public boolean connect() {
		return false;
	}

	@Override
	public DataModel getCurrentModel() {
		return comm.getModel();
	}


	@Override
	public Map<Class<?>,MAVLinkMessage> getMavLinkMessageMap() {
		return comm.getMavLinkMessageMap();
	}


	@Override
	public boolean isSimulation() {
		return isSITL;
	}


	@Override
	public boolean isConnected() {
		return true;
	}



	@Override
	public boolean close() {
		collector.stop();
		comm.close();
		return true;
	}


	@Override
	public void addModeChangeListener(IMSPModeChangedListener listener) {
		if(comm!=null)
			comm.addModeChangeListener(listener);

	}


	@Override
	public ModelCollectorService getCollector() {
		return collector;
	}

	@Override
	public void addMAVLinkListener(IMAVLinkListener listener) {
		if(comm!=null)
			comm.addMAVLinkListener(listener);
	}

	@Override
	public void addMAVMessageListener(IMAVMessageListener listener) {
		if(comm!=null)
			comm.addMAVMessageListener(listener);

	}


	@Override
	public void writeLogMessage(LogMessage m) {
		if(comm!=null)
		  comm.writeMessage(m);
		model.msg.set(m);

	}


	@Override
	public int getErrorCount() {
		return commError;
	}


	@Override
	public String getConnectedAddress() {
        return peerAddress;
	}





}
