package com.hydrogen.mqtt.connector.nettyserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hydrogen.mqtt.connector.msghandle.agv.msg.AGVBaseMsg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class AGVMessageEncoder extends MessageToByteEncoder<AGVBaseMsg> {
	private static Logger LOG = LoggerFactory.getLogger(AGVMessageEncoder.class);


	@Override
	protected void encode(ChannelHandlerContext ctx, AGVBaseMsg msg, ByteBuf out) throws Exception {
		byte[] body = msg.encoder();
		int crc_msg_len = 7;
		if(body!=null && body.length!=0) {
			crc_msg_len += body.length;
		}
		out.writeByte(AGVBaseMsg.MSG_HEAD_1);
		out.writeByte(AGVBaseMsg.MSG_HEAD_2);
		out.writeByte(msg.msgCmd());
		out.writeShort(msg.getMsgseq());
		if(body!=null && body.length!=0) {
			out.writeShort(body.length);
			out.writeBytes(body);
		}else {
			out.writeShort(0);
		}
		byte[] crcMsg = new byte[crc_msg_len];
		out.readBytes(crcMsg);
		out.writeBytes(crcMsg);
		out.writeByte(AGVBaseMsg.CRC8(crcMsg));
		//设置消息头
		LOG.debug("encode msg:"+msg.msgCmd()+",seq:"+msg.getMsgseq());
	
	}

}
