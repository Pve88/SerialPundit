package test16;

import com.embeddedunveiled.serial.SerialComManager;
import com.embeddedunveiled.serial.SerialComManager.BAUDRATE;
import com.embeddedunveiled.serial.SerialComManager.DATABITS;
import com.embeddedunveiled.serial.SerialComManager.FLOWCONTROL;
import com.embeddedunveiled.serial.SerialComManager.PARITY;
import com.embeddedunveiled.serial.SerialComManager.STOPBITS;
import com.embeddedunveiled.serial.ISerialComEventListener;
import com.embeddedunveiled.serial.SerialComLineEvent;

class EventListener implements ISerialComEventListener{
	@Override
	public void onNewSerialEvent(SerialComLineEvent lineEvent) {
	}
}

public class Test16 {
	public static void main(String[] args) {
		
		SerialComManager scm = new SerialComManager();
		
		// instantiate class which is will implement ISerialComEventListener interface
		EventListener eventListener = new EventListener();
		
		try {
			long handle = scm.openComPort("/dev/ttyUSB0", true, true, false);
			scm.configureComPortData(handle, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			scm.configureComPortControl(handle, FLOWCONTROL.HARDWARE, 'x', 'x', false, false);
			scm.registerLineEventListener(handle, eventListener);
			
			long handle1 = scm.openComPort("/dev/ttyUSB1", true, true, false);
			scm.configureComPortData(handle1, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			scm.configureComPortControl(handle1, FLOWCONTROL.HARDWARE, 'x', 'x', false, false);
			
			// get current active mask
			int mask1 = scm.getEventsMask(eventListener);
			System.out.println("mask before : " + mask1);
			
			// mask CTS, so only changes to CTS line will be reported.
			scm.setEventsMask(eventListener, SerialComManager.CTS);

			// get current active mask
			int mask2 = scm.getEventsMask(eventListener);
			System.out.println("mask after : " + mask2);
			
			// unregister data listener
			scm.unregisterLineEventListener(eventListener);
			
			// close the port releasing handle
			scm.closeComPort(handle);
			scm.closeComPort(handle1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}