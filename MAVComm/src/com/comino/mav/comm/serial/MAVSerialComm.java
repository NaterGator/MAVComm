package com.comino.mav.comm.serial;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAV_CMD;
import org.mavlink.messages.MAV_MODE_FLAG;
import org.mavlink.messages.lquac.msg_command_long;
import org.mavlink.messages.lquac.msg_heartbeat;

import com.comino.mav.comm.IMAVComm;
import com.comino.mav.mavlink.IMAVLinkMsgListener;
import com.comino.mav.mavlink.MAVLinkToModelParser;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.collector.ModelCollectorService;
import com.comino.msp.model.segment.Message;
import com.comino.msp.model.segment.Status;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;


public class MAVSerialComm implements IMAVComm {

//	private static final int BAUDRATE  = 57600;
	private static final int BAUDRATE  = 921600;


	private SerialPort 			serialPort;
	private String	        port;

	private DataModel 			model = null;

	private MAVLinkToModelParser parser = null;

	private static IMAVComm com = null;
	

	public static IMAVComm getInstance(DataModel model) {
		if(com==null) 
			com = new MAVSerialComm(model);
		return com;
	}

	private MAVSerialComm(DataModel model) {
		this.model = model; int i=0;
		String[] list = SerialPortList.getPortNames();

		if(list.length>0) {
			for(i=0;i<list.length;i++) {
				if(list[i].contains("tty.SLAB") || list[i].contains("tty.usb") || list[i].contains("AMA0")) {
					break;
				}		
			}

			port = list[i];
		}
		else
			port ="/dev/tty.SLAB_USBtoUART";

		serialPort = new SerialPort(port);
		parser = new MAVLinkToModelParser(model,new SerialPortChannel(serialPort), this);
		parser.start();

	}

	/* (non-Javadoc)
	 * @see com.comino.px4.control.serial.IPX4Comm#open()
	 */
	@Override
	public boolean open() {
		while(!open(port ,BAUDRATE,8,1,0)) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {	}
		}
		return true;
	}



	/* (non-Javadoc)
	 * @see com.comino.px4.control.serial.IPX4Comm#getModel()
	 */
	@Override
	public DataModel getModel() {
		return model;
	}


	/* (non-Javadoc)
	 * @see com.comino.px4.control.serial.IPX4Comm#getMessageList()
	 */
	@Override
	public List<Message> getMessageList() {
		return parser.getMessageList();
	}
	
	
	@Override
	public Map<Class<?>,MAVLinkMessage> getMavLinkMessageMap() {
		return parser.getMavLinkMessageMap();
	}

	/* (non-Javadoc)
	 * @see com.comino.px4.control.serial.IPX4Comm#close()
	 */
	@Override
	public void close() {
		try {
			serialPort.closePort();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	private boolean open(String portName, int baudRate, int dataBits, int stopBits, int parity) {
		
		try {
			serialPort.openPort();
			serialPort.setParams(baudRate, dataBits, stopBits, parity);

		} catch (Exception e2) {
			try {
				serialPort.closePort();
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			//System.err.println(e2.getMessage());
			return false;
		}

		System.out.println("Connected to "+serialPort.getPortName());
		model.sys.setStatus(Status.MSP_CONNECTED, true);
		return true;

	}

	/* (non-Javadoc)
	 * @see com.comino.px4.control.serial.IPX4Comm#write(org.mavlink.messages.MAVLinkMessage)
	 */
	@Override
	public void write(MAVLinkMessage msg) throws IOException {
		try {
			serialPort.writeBytes(msg.encode());
		} catch (SerialPortException e) {
			throw new IOException(e.getMessage());
		} 
	}
	
	@Override
	public void registerProxyListener(IMAVLinkMsgListener listener) {
		parser.registerProxyListener(listener);
		
	}


	public static void main(String[] args) {
		IMAVComm comm = new MAVSerialComm(new DataModel());
		comm.open();


		long time = System.currentTimeMillis();


		try {


			ModelCollectorService colService = new ModelCollectorService(comm.getModel());
			colService.start();


		//	while(System.currentTimeMillis()< (time+30000)) {
			
			while(true) {
				
				
				msg_command_long cmd = new msg_command_long(255,1);
				cmd.target_system = 1;
				cmd.target_component = 1;
				cmd.command = MAV_CMD.MAV_CMD_DO_SET_MODE;
				cmd.confirmation = 0;
				
				cmd.param1 = MAV_MODE_FLAG.MAV_MODE_FLAG_CUSTOM_MODE_ENABLED;
				cmd.param2 = 2;
						
				
				try {
					comm.write(cmd);
					System.out.println("Execute: "+cmd.toString());
				} catch (IOException e1) {
				    System.err.println(e1.getMessage());
				}	

				msg_heartbeat msg = 	(msg_heartbeat) comm.getMavLinkMessageMap().get(msg_heartbeat.class);
				if(msg!=null)
			      System.out.println(msg.custom_mode);
//				//		comm.getModel().state.print("NED:");
				//	System.out.println("REM="+comm.model.battery.p+" VOLT="+comm.model.battery.b0+" CURRENT="+comm.model.battery.c0);
				//   System.out.println("ANGLEX="+comm.model.attitude.aX+" ANGLEY="+comm.model.attitude.aY+" "+comm.model.sys.toString());
				Thread.sleep(2000);
			}

//			colService.stop();
//			comm.close();
//
//			System.out.println(colService.getModelList().size()+" models collected");


			//			for(int i=0;i<colService.getModelList().size();i++) {
			//				DataModel m = colService.getModelList().get(i);
			//				System.out.println(m.attitude.aX);
			//			}


		} catch (Exception e) {
			comm.close();
			// TODO Auto-generated catch block
			e.printStackTrace();

		}




	}

	


}