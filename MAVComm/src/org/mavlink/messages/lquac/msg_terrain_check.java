/**
 * Generated class : msg_terrain_check
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
 * Class msg_terrain_check
 * Request that the vehicle report terrain height at the given location. Used by GCS to check if vehicle has all terrain data needed for a mission.
 **/
public class msg_terrain_check extends MAVLinkMessage {
  public static final int MAVLINK_MSG_ID_TERRAIN_CHECK = 135;
  private static final long serialVersionUID = MAVLINK_MSG_ID_TERRAIN_CHECK;
  public msg_terrain_check() {
    this(1,1);
}
  public msg_terrain_check(int sysId, int componentId) {
    messageType = MAVLINK_MSG_ID_TERRAIN_CHECK;
    this.sysId = sysId;
    this.componentId = componentId;
    length = 8;
}

  /**
   * Latitude (degrees *10^7)
   */
  public long lat;
  /**
   * Longitude (degrees *10^7)
   */
  public long lon;
/**
 * Decode message with raw data
 */
public void decode(ByteBuffer dis) throws IOException {
  lat = (int)dis.getInt();
  lon = (int)dis.getInt();
}
/**
 * Encode message with raw data and other informations
 */
public byte[] encode() throws IOException {
  byte[] buffer = new byte[8+8];
   ByteBuffer dos = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
  dos.put((byte)0xFE);
  dos.put((byte)(length & 0x00FF));
  dos.put((byte)(sequence & 0x00FF));
  dos.put((byte)(sysId & 0x00FF));
  dos.put((byte)(componentId & 0x00FF));
  dos.put((byte)(messageType & 0x00FF));
  dos.putInt((int)(lat&0x00FFFFFFFF));
  dos.putInt((int)(lon&0x00FFFFFFFF));
  int crc = MAVLinkCRC.crc_calculate_encode(buffer, 8);
  crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[messageType], crc);
  byte crcl = (byte) (crc & 0x00FF);
  byte crch = (byte) ((crc >> 8) & 0x00FF);
  buffer[14] = crcl;
  buffer[15] = crch;
  return buffer;
}
public String toString() {
return "MAVLINK_MSG_ID_TERRAIN_CHECK : " +   "  lat="+lat+  "  lon="+lon;}
}