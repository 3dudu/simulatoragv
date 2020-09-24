package com.hydrogen.mqtt.connector.msghandle.agv.msg;

import java.util.concurrent.atomic.AtomicInteger;

import com.hydrogen.mqtt.connector.msghandle.AGVMsgInterface;

public abstract class AGVBaseMsg   implements AGVMsgInterface{
	
	public static int MSG_HEAD_1 = 0xEB;
	
	public static int MSG_HEAD_2 = 0x90;
	
	private int carid = 0;
	
	public enum CMD{
		CMD_DD(0xDD),
		CMD_D1(0xD1),
		CMD_DF(0xDF),
		CMD_DE(0xDE);
		
		private int cmdcode;
		CMD(int cmd){
            this.cmdcode = cmd;
        }

		public int getCmd() {
			return cmdcode;
		}
		
	}
	
    public int getCarid() {
		return carid;
	}

	public void setCarid(int carid) {
		this.carid = carid;
	}
	
	public int agvid() {
		return this.carid;
	}

	private static int[] CRC8Table = new int[] {
            0,94,188,226,97,63,221,131,194,156,126,32,163,253,31,65,
            157,195,33,127,252,162,64,30, 95,1,227,189,62,96,130,220,
            35,125,159,193,66,28,254,160,225,191,93,3,128,222,60,98,
            190,224,2,92,223,129,99,61,124,34,192,158,29,67,161,255,
            70,24,250,164,39,121,155,197,132,218,56,102,229,187,89,7,
            219,133,103,57,186,228,6,88,25,71,165,251,120,38,196,154,
            101,59,217,135,4,90,184,230,167,249,27,69,198,152,122,36,
            248,166,68,26,153,199,37,123,58,100,134,216,91,5,231,185,
            140,210,48,110,237,179,81,15,78,16,242,172,47,113,147,205,
            17,79,173,243,112,46,204,146,211,141,111,49,178,236,14,80,
            175,241,19,77,206,144,114,44,109,51,209,143,12,82,176,238,
            50,108,142,208,83,13,239,177,240,174,76,18,145,207,45,115,
            202,148,118,40,171,245,23,73,8,86,180,234,105,55,213,139,
            87,9,235,181,54,104,138,212,149,203, 41,119,244,170,72,22,
            233,183,85,11,136,214,52,106,43,117,151,201,74,20,246,168,
            116,42,200,150,21,75,169,247,182,232,10,84,215,137,107,53 };
	
	public AGVBaseMsg(int seq){
		this.cmd = msgCmd();
		if(seq==0) {
			seq = nextSeq();
		}
		this.msgseq = seq;
	}
	
	public int checkCRC() {
		if(this.crc==0) {
			this.crc = CRC8(body);;
		}
		return this.crc;
	}
	
	private static AtomicInteger seq = new AtomicInteger();
	
	private int cmd;

	private int msgseq;
	
	private int crc;
	
	private byte[] body;
	
	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public int getMsgseq() {
		return msgseq;
	}

	public void setMsgseq(int msgseq) {
		this.msgseq = msgseq;
	}

	public int getCrc() {
		return crc;
	}

	public void setCrc(int crc) {
		this.crc = crc;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public int nextSeq() {
		return seq.addAndGet(1);
	}
	
	 public static int CRC8(byte[] buffer)
     {
         try
         {
             return CRC_8(buffer, 0, buffer.length);
         }
         catch (Exception ex)
         { throw ex; }
     }

     public static int CRC_8(byte[] buffer, int off, int len)
     {
         try
         {
             int crc = 0;
             if (buffer == null)
             {
                 return crc;
             }
             if (off < 0 || len < 0 || off + len > buffer.length)
             {
                 return crc;
             }

             for (int i = off; i < len; i++)
             {
                 crc = CRC8Table[crc ^ buffer[i]&0xFF];
             }
             return crc;
         }
         catch (Exception ex)
         { throw ex; }
     }
     
     public static byte[] intToByteArray(int i) {
         byte[] result = new byte[4];
         result[0] = (byte)((i >> 24) & 0xFF);
         result[1] = (byte)((i >> 16) & 0xFF);
         result[2] = (byte)((i >> 8) & 0xFF);
         result[3] = (byte)(i & 0xFF);
         return result;
     }
     

 	public static int byteArrToInt(byte[] arr){
 		int x = ((arr[0] & 0xff) << 24 )|((arr[1]& 0xff) <<16 )|((arr[2] & 0xff)<<8)|(arr[3] & 0xff);
 		return x;
 	}

}
