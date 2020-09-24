package com.hydrogen.mqtt.connector.msghandle.agv.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg;

public class AGVEncoder extends ProtocolEncoderAdapter {
	private static Logger mLog = LoggerFactory.getLogger(AGVEncoder.class);

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
            throws Exception {
    	AGVBaseMsg msg = (AGVBaseMsg)message;
		byte[] body = msg.encoder();
		IoBuffer buffer = IoBuffer.allocate(8);
		int crc_msg_len = 7;
		if(body!=null && body.length!=0) {
			buffer = IoBuffer.allocate(8+body.length);
			crc_msg_len += body.length;
		}
		
		buffer.setAutoExpand(true);

		buffer.putUnsigned(AGVBaseMsg.MSG_HEAD_1);
		buffer.putUnsigned(AGVBaseMsg.MSG_HEAD_2);
		buffer.putUnsigned(msg.msgCmd());
		buffer.putUnsignedShort(msg.getMsgseq());
		if(body!=null && body.length!=0) {
			buffer.putUnsignedShort(body.length);
			buffer.put(body);
		}else {
			buffer.putUnsignedShort(0);
		}
		byte[] crcMsg = new byte[crc_msg_len];
		System.arraycopy(buffer.array(), 0, crcMsg, 0, crc_msg_len);
		buffer.putUnsigned(AGVBaseMsg.CRC8(crcMsg));
		//设置消息头
		buffer.flip();
		out.write(buffer);
		if(mLog.isDebugEnabled()){
			mLog.debug("encode msg:"+msg.msgCmd()+",seq:"+msg.getMsgseq());
		}
    }
}
