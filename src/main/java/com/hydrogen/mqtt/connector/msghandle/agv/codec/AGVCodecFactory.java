package com.hydrogen.mqtt.connector.msghandle.agv.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class AGVCodecFactory implements ProtocolCodecFactory {

    private final AGVEncoder encoder;
    private final AGVDecoder decoder;

    public AGVCodecFactory() {
        encoder = new AGVEncoder();
        decoder = new AGVDecoder();
    }

    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
}