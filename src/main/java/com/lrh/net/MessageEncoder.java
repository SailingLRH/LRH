package com.lrh.net;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class MessageEncoder extends ProtocolEncoderAdapter
{

	@Override
	public void encode(IoSession paramIoSession, Object message, ProtocolEncoderOutput out) throws Exception
	{
		byte[] bts = (byte[]) message;
		IoBuffer buf = IoBuffer.allocate(bts.length);
		buf.put(bts);
		buf.flip();
		out.write(buf);
	}

}
