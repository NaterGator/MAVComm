/**
 * Generated class : msg_set_attitude_target
 * DO NOT MODIFY!
 **/
package org.mavlink.messages.lquac;
import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkCRC;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/**
 * Class msg_set_attitude_target
 * Set the vehicle attitude and body angular rates.
 **/
public class msg_set_attitude_target extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_SET_ATTITUDE_TARGET = 82;
  private static final long serialVersionUID = MAVLINK_MSG_ID_SET_ATTITUDE_TARGET;
  public msg_set_attitude_target() {
    this(1,1);
}
  public msg_set_attitude_target(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_SET_ATTITUDE_TARGET;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 39;
}

  /**
   * Timestamp in milliseconds since system boot
   */
  public long time_boot_ms;
  /**
   * Attitude quaternion (w, x, y, z order, zero-rotation is 1, 0, 0, 0)
   */
  public float[] q = new float[4];
  /**
   * Body roll rate in radians per second
   */
  public float body_roll_rate;
  /**
   * Body roll rate in radians per second
   */
  public float body_pitch_rate;
  /**
   * Body roll rate in radians per second
   */
  public float body_yaw_rate;
  /**
   * Collective thrust, normalized to 0 .. 1 (-1 .. 1 for vehicles capable of reverse trust)
   */
  public float thrust;
  /**
   * System ID
   */
  public int target_system;
  /**
   * Component ID
   */
  public int target_component;
  /**
   * Mappings: If any of these bits are set, the corresponding input should be ignored: bit 1: body roll rate, bit 2: body pitch rate, bit 3: body yaw rate. bit 4-bit 6: reserved, bit 7: throttle, bit 8: attitude
   */
  public int type_mask;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  time_boot_ms = (int)dis.getInt()&0x00FFFFFFFF;
  for (int i=0; i<4; i++) {
    q[i] = (float)dis.getFloat();
  }
  body_roll_rate = (float)dis.getFloat();
  body_pitch_rate = (float)dis.getFloat();
  body_yaw_rate = (float)dis.getFloat();
  thrust = (float)dis.getFloat();
  target_system = (int)dis.get()&0x00FF;
  target_component = (int)dis.get()&0x00FF;
  type_mask = (int)dis.get()&0x00FF;
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+39];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putInt((int)(time_boot_ms&0x00FFFFFFFF));
  for (int i=0; i<4; i++) {
    dos.putFloat(q[i]);
  }
  dos.putFloat(body_roll_rate);
  dos.putFloat(body_pitch_rate);
  dos.putFloat(body_yaw_rate);
  dos.putFloat(thrust);
  dos.put((byte)(target_system&0x00FF));
  dos.put((byte)(target_component&0x00FF));
  dos.put((byte)(type_mask&0x00FF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 39);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[45] = crcl;
  buffer[46] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_SET_ATTITUDE_TARGET : " +   "  time_boot_ms="+time_boot_ms+  "  q="+q+  "  body_roll_rate="+body_roll_rate+  "  body_pitch_rate="+body_pitch_rate+  "  body_yaw_rate="+body_yaw_rate+  "  thrust="+thrust+  "  target_system="+target_system+  "  target_component="+target_component+  "  type_mask="+type_mask;}
}