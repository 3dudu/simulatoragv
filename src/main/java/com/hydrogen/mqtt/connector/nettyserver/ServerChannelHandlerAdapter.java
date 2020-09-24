package com.hydrogen.mqtt.connector.nettyserver;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hydrogen.mqtt.connector.msghandle.AGVMsgHandlerInterface;
import com.hydrogen.mqtt.connector.msghandle.AGVMsgInterface;
import com.hydrogen.mqtt.connector.msghandle.AbstractMsgHandlerFactory;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class ServerChannelHandlerAdapter extends ChannelInboundHandlerAdapter {
    private AbstractMsgHandlerFactory msgHandlerFactory;
	/**
	 * 日志处理
	 */
	private Logger logger = LoggerFactory.getLogger(ServerChannelHandlerAdapter.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
		String remoteAddress = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();

		if (obj != null && obj instanceof AGVMsgInterface) {
			AGVMsgInterface msg = (AGVMsgInterface) obj;
			logger.debug("rec from " + remoteAddress + ",msg:" + msg.msgCmd());
			AGVMsgInterface message = processPacket(msg, ctx);
			if (message != null) {
				ctx.writeAndFlush(message);
			}
		}
	}

	private AGVMsgInterface processPacket(AGVMsgInterface msg, ChannelHandlerContext ctx) {
		//处理消息
		//否则，交给消息Handler去处理
		int msgId = msg.msgCmd();
		AGVMsgHandlerInterface handler = msgHandlerFactory.getHandler(msgId);
		if(null == handler){
			logger.error("未知消息，没有注册消息处理器");
			return null;
		} else {
			//直接交给消息handler去处理
			return handler.process(msg,ctx);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

		cause.printStackTrace();
		ctx.close();

	}
	public void register(AbstractMsgHandlerFactory msgHandlerFactory) {
		this.msgHandlerFactory = msgHandlerFactory;
		this.msgHandlerFactory.regHandler();
	}
}
