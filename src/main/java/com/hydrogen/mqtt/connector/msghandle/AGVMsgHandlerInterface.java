package com.hydrogen.mqtt.connector.msghandle;

import org.apache.mina.core.session.IoSession;

import io.netty.channel.ChannelHandlerContext;

public interface AGVMsgHandlerInterface{
	public AGVMsgInterface process(AGVMsgInterface taskMsg, IoSession session);
	public int getHandlerId();
	public AGVMsgInterface process(AGVMsgInterface msg, ChannelHandlerContext ctx);
}
