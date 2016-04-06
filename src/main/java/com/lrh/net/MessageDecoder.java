package com.lrh.net;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MessageDecoder extends CumulativeProtocolDecoder
{
	/**
	 * 全部线程 收包头;
	 * */
	protected Map<String, SocketMsgData> all = new HashMap<String, SocketMsgData>();

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception
	{
		// 解码高低字节序
		// in.order(SocketMsgData.Order);
		return loopRead(session, in, out);
	}

	// 循环读取数据;
	protected boolean loopRead(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
	{
		try
		{
			String key = String.valueOf(session.getId());
			SocketMsgData msg = all.get(key);
			if (msg == null)
			{
				msg = new SocketMsgData();
				all.put(key, msg);
			}

			int remaining = in.remaining();
			if (remaining >= msg.readSize)
			{

				if (msg.isReadHead)
				{
					msg = new SocketMsgData();
					all.put(key, msg);

					// 包头是否成功读取；且密钥相符;
					boolean result = msg.readHead(in);
					if (!result)
					{
						session.close(false);
					}
					msg.isReadHead = false;
					msg.readSize = msg.bodySize;

					return loopRead(session, in, out);
				}
				else
				{
					msg.readBody(in);
					msg.isReadHead = true;
					msg.readSize = SocketMsgData.headsize;
					if (msg.convertBodyData())
						out.write(msg);
					else
						return false;
					return true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

}
