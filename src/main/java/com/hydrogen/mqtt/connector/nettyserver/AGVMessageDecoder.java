package com.hydrogen.mqtt.connector.nettyserver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hydrogen.mqtt.connector.msghandle.AGVMsgInterface;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg;
import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVMessageFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class AGVMessageDecoder extends ByteToMessageDecoder{
	private static Logger LOG = LoggerFactory.getLogger(AGVMessageDecoder.class);
	
	ByteBuf tempMsg = Unpooled.buffer();

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		// 合并报文
		ByteBuf message = null;
		int tmpMsgSize = tempMsg.readableBytes();
		// 如果暂存有上一次余下的请求报文，则合并
		if (tmpMsgSize > 0) {
			message = Unpooled.buffer();
			message.writeBytes(tempMsg);
			message.writeBytes(in);
		} else {
			message = in;
		}
		int size = message.readableBytes();
		if (size < 8) {
			tempMsg.clear();
			tempMsg.writeBytes(message.readBytes(size));
		} else {
			message.markReaderIndex();
			short a = message.readUnsignedByte();
			if (a == AGVBaseMsg.MSG_HEAD_1) {
				a = message.readUnsignedByte();
				if (a == AGVBaseMsg.MSG_HEAD_2) {
					short cmdcode = message.readUnsignedByte();
					int msgseq = message.readUnsignedShort();
					int msglegth = message.readUnsignedShort();
					if (size - 8 >= msglegth) {
						byte[] body = null;
						if (msglegth > 0) {
							body = new byte[msglegth];
							message.readBytes(body);
						}
						message.resetReaderIndex();
						byte[] crcMsg = new byte[msglegth + 7];
						message.readBytes(crcMsg, 0, msglegth + 7);

						int _crc = AGVBaseMsg.CRC8(crcMsg);
						int crc = message.readUnsignedByte();
						if (_crc == crc) {
							AGVMsgInterface msg = AGVMessageFactory.getInstance().buildMsg(cmdcode, msgseq, body);
							if (null != msg) {
								LOG.debug("decode msg:" + msg.msgCmd() + ",seq:" + msgseq);
								out.add(msg);
							}
						}

					} else {
						tempMsg.clear();
						tempMsg.writeBytes(message);
					}
				} else {
					tempMsg.clear();
					tempMsg.writeBytes(message);
				}
			}
		}
			
	}

}
