package com.lrh.net;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;



import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;


public class SocketMsgData
{
	// 读取的大小;
	public int readSize = SocketMsgData.headsize;

	// 是否读包头
	public boolean isReadHead = true;

	// 包头的数据大小
	static public final byte headsize = 8;

	// 包体长度
	public int bodySize;

	// 全局定义字节序;
	static public final ByteOrder Order = ByteOrder.LITTLE_ENDIAN;

	protected ByteBuffer _data = null;

	public static byte[] startFlag = new byte[] { (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD };

	public static int byte2int(byte[] res)
	{
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
				| ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	}
	 public static byte[] int2byte(int res) {  
		 byte[] targets = new byte[4];  
		   
		 targets[0] = (byte) (res & 0xff);// 最低位   
		 targets[1] = (byte) ((res >> 8) & 0xff);// 次低位   
		 targets[2] = (byte) ((res >> 16) & 0xff);// 次高位   
		 targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。   
		 return targets;   
		 } 
	// 包体数据
	protected byte[] bytes;

	public SocketMsgData()
	{

	}


	public static void main(String[] args)
	{
//		byte[] arr = new byte[4];
//		arr[0] = 48;
//		arr[1] = 2;
//		arr[2] = 0;
//		arr[3] = 0;
//		System.out.println(byte2int(arr));
//		System.out.println(int2byte(560)[3]);
//		int a = 990415619;
//		byte[] it=intToByteArray(a);
//		System.out.println();
//		System.out.println(byte2int(it));
		
	}

	/**
	 * 读取包头;
	 * 
	 * @param in
	 * @return 是否成功；
	 */
	public boolean readHead(IoBuffer in)
	{
		if (in.remaining() >= headsize)
		{
			String i = in.getHexDump();
			in.position();
			byte a0 = in.get();
			byte a1 = in.get();
			byte a2 = in.get();
			byte a3 = in.get();
			byte[] intarray = new byte[4];
			intarray[0] = in.get();
			intarray[1] = in.get();
			intarray[2] = in.get();
			intarray[3] = in.get();
			bodySize = byte2int(intarray);
			if (a0 == startFlag[0] && a1 == startFlag[1] && a2 == startFlag[2] && a3 == startFlag[3])
			{
				return true;
			}
			return false;
		}
		return true;

	}

	// 读取包体
	public void readBody(IoBuffer in)
	{
		// 将缓冲区数据读到包体;
		if (in.remaining() >= bodySize)
		{
			bytes = new byte[bodySize];
			in.get(bytes);
		}
	}

	// 包体原始数据包;
	public ByteBuffer getData()
	{
		if (_data == null)
		{
			_data = ByteBuffer.wrap(bytes);
			_data.order(ByteOrder.LITTLE_ENDIAN);
		}
		return _data;
	}

	/**
	 * 将包体信息转到json对象
	 * 
	 * @return
	 */
	public boolean convertBodyData()
	{
		try
		{
			String str = new String(getData().array(),"utf-8");
			System.out.println(str);
			_json = JSONObject.parseObject(str);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * 写入字节的缓冲字节数组; 2048 byte;
	 */
	protected byte[] _writeBytes;

	protected ByteBuffer writeBuf;

	public void send(IoSession session)
	{
		_writeBytes = new byte[1024 * 20];
		if (writeBuf == null)
		{
			writeBuf = ByteBuffer.wrap(_writeBytes);
			writeBuf.order(ByteOrder.LITTLE_ENDIAN);
		}
		writeBuf.put(SocketMsgData.startFlag[0]);
		writeBuf.put(SocketMsgData.startFlag[1]);
		writeBuf.put(SocketMsgData.startFlag[2]);
		writeBuf.put(SocketMsgData.startFlag[3]);
		String data = toString();
		byte[] dataArray = data.getBytes();
		writeBuf.put(int2byte(dataArray.length));
		writeBuf.put(dataArray);

		short _len = (short) writeBuf.position();
		byte[] bytes = new byte[_len];
		// 字节拷贝;
		System.arraycopy(writeBuf.array(), 0, bytes, 0, _len);
		writeBuf.position(_len);
		/*
		 * IoBuffer buf = IoBuffer.allocate(bytes.length); buf.put(bytes); buf.flip();
		 */
		if (session == null || session.isClosing())
		{
			return;
		}
		System.out.println("发送消息："+toString());
		session.write(bytes);
	}

	public SocketMsgData(String mainType, String subType, Integer pkgNo)
	{
		try
		{
			// 这里的JSON包要替换成调用C写的函数库！
			_json = new JSONObject();
			_json.put("mainType", mainType);
			_json.put("subType", subType);
			_json.put("pkgNo", pkgNo);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("finally")
	public String getMainType()
	{
		String mainType = "";
		try
		{
			mainType = _json.getString("mainType");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		finally
		{
			return mainType;
		}
	}

	@SuppressWarnings("finally")
	public String getSubType()
	{
		String subType = "";
		try
		{
			subType = _json.getString("subType");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		finally
		{
			return subType;
		}
	}

	@SuppressWarnings("finally")
	public String getPkgNo()
	{
		String pkgNo = "";
		try
		{
			pkgNo = _json.getString("pkgNo");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		finally
		{
			return pkgNo;
		}
	}

	public SocketMsgData(String jsonStr)
	{
		try
		{
			_json =  JSONObject.parseObject(jsonStr);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private JSONObject _json;

	public String toString()
	{
		if (_json != null)
			return _json.toString();
		return "";
	}

	public boolean has(String key)
	{
		return _json.containsKey(key);
	}

	public void send(PlayerConnect connect)
	{
		send(connect.getSession());
	}

	public void write(String key, List list) throws JSONException
	{
		JSONArray array = new JSONArray();
		for (Object object : list)
		{
			array.add(object);
		}
		_json.put(key, array);
	}

	public void writeJSONArray(String key, JSONArray array) throws JSONException
	{
		_json.put(key, array);
	}

	public void write(String key, Object value) throws JSONException
	{
		_json.put(key, value);
	}

	public void writeJSONObject(String key, JSONObject value) throws JSONException
	{
		_json.put(key, value);
	}

	public void write(String key, Map map) throws JSONException
	{
		_json.put(key, map);
	}

	public void write(String key, String value) throws JSONException
	{
		_json.put(key, value);
	}

	public void write(String key, int value) throws JSONException
	{
		_json.put(key, value);
	}

	public void write(String key, long value) throws JSONException
	{
		_json.put(key, value);
	}

	public void write(String key, boolean value) throws JSONException
	{
		_json.put(key, value);
	}

	/**
	 * 根据实际情况，转JSONObject或JSONArray
	 * 
	 * @param key
	 * @return
	 * @throws JSONException
	 */
	public Object get(String key) throws JSONException
	{
		return _json.containsKey(key) ? _json.get(key) : null;
	}

	public int getInt(String key) throws JSONException
	{
		return _json.containsKey(key) ? _json.getIntValue(key) : null;
	}

	public boolean getBoolean(String key) throws JSONException
	{
		return _json.getBoolean(key);
	}

	public String getString(String key) throws JSONException
	{
		return _json.containsKey(key) ? _json.getString(key) : "";
	}

	public long getLong(String key)
	{
		try
		{
			return _json.getLong(key);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return -1;
		}
	}

}
