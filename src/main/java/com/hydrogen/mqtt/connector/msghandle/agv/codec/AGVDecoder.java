package com.hydrogen.mqtt.connector.msghandle.agv.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVMessageFactory;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVMsgInterface;

public class AGVDecoder  extends CumulativeProtocolDecoder {

	private static Logger mLog = LoggerFactory.getLogger(AGVDecoder.class);

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		// 至少8字节
		if (in.remaining() < 8) {
			return false;
		}
		int start = in.position();
		int a = in.getUnsigned();
		if (a == AGVBaseMsg.MSG_HEAD_1) {
			a = in.getUnsigned();
			if (a == AGVBaseMsg.MSG_HEAD_2) {
				int cmdcode = in.getUnsigned();
				if(cmdcode==0XDE){
					mLog.debug("decode msg:" + cmdcode);
				}
				int msgseq = in.getUnsignedShort();
				int msglegth = in.getUnsignedShort();
				if (in.remaining() >= msglegth + 1) {
					byte[] body = null;
					if(msglegth>0) {
						body = new byte[msglegth];
						in.get(body, 0, msglegth);
					}
					in.position(start);
					byte[] crcMsg = new byte[msglegth + 7];
					in.get(crcMsg, 0, msglegth + 7);

					int _crc = AGVBaseMsg.CRC8(crcMsg);
					int crc = in.getUnsigned();
					if (_crc != crc) {
						return true;
					}
					AGVMsgInterface msg = AGVMessageFactory.getInstance().buildMsg(cmdcode, msgseq, body);
					if (null != msg) {
						if (mLog.isDebugEnabled()) {
							mLog.debug("decode msg:" + msg.msgCmd() + ",seq:" + msgseq);
						}
						out.write(msg);
					}
					return true;
				} else {
					in.position(start);
					return false;
				}

			} else {
				in.position(start+1);
				return true;
			}
		} else {
			return true;
		}

	}


}
