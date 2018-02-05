import java.io.UnsupportedEncodingException;

public class xuAppTxt {
	public byte[][] sendData;
	public String sendStr;
	public byte[] receiveData = new byte[31];
	public String receiveStr;
	public int type;
	
	//	将字符串分包
    public byte[][] DevideStr(String Str){
		//	字符串存入
    	byte[] strTemp = new byte[Str.length()];
		try {
			strTemp = Str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//	包的个数（34+1=35）
		int num = strTemp.length/150+1;
		//	剩下的长度 放到最后一个包里面（1）
		int last = strTemp.length%150;
		System.out.println("个数:" + num + "\t剩余:" + last + "\t总长:" + strTemp.length); 
		//	每一个包
		//	[序号][正文]
    	byte[][] buff = new byte[num][153];
        // 拷贝字符,留最后一个包最后操作  0 ~ 33(35-2)
    	for(int n=0;n<num-1;n++){
            System.arraycopy(strTemp,n*150,buff[n],3,150);
            //	153的前3位放序号
            buff[n][0] = (byte)(n/100);
            buff[n][1] = (byte)(n%100/10);
            buff[n][2] = (byte)(n%10/1);
//            for (int m =0;m<153;m++){
//        		System.out.print(buff[n][m] + " "); 
//            }
//            System.out.println(); 
    	}
    	//	包34
    	buff[num-1][0] = (byte)((num-1)/100);
        buff[num-1][1] = (byte)((num-1)/10);
        buff[num-1][2] = (byte)((num-1)%10/1);
    	System.arraycopy(strTemp,(num-1)*150,buff[num-1],3,last);
//    	for (int m =0;m<153;m++){
//    		System.out.print(buff[num-1][m] + " ");
//        } 
    	return buff;
	}
	
	//	组合分开的包
    public String LinkByteArr(byte[][] buff, int txtLenth){
		String Str = null;
		//	包的个数
		int num = buff.length;
		System.out.println("包个数：" + num);
		
		//	存到一个 byte[] 里面
		byte[] temp = new byte[buff.length* txtLenth];
		for (int n=0;n<num;n++){
			System.arraycopy(buff[n],3,temp,n*150,150);
		}
		//System.out.println(" \t组合后：" + temp);
		try {
			Str = new String(temp, "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(" \t组合后：" + Str);
		return Str;
	}
	
	//	用来发送固定长度的包
    public byte[] sendMsg(String Str){
    	byte[] buff = new byte[153];
    	byte[] temp = new byte[153];
    	try{
    		temp = Str.getBytes("UTF-8");
    	}catch (Exception e){
    		System.out.println("封包未成功: " + e.getMessage());    
    	}
    	//for(int num=0;num<1024;num++){System.out.println(buff[num]);}
    	// 	utf-8 封包的时候 一个中文字符换成3比特
    	//	num<Str.length()*3 不行是因为转义符只占一位
    	//for(int num=0;num<temp.length;num++){buff[num]=temp[num];}
    	buff[0] = (byte)9;
    	buff[1] = (byte)9;
    	buff[2] = (byte)9;
    	System.arraycopy(temp, 0, buff, 3, temp.length);
    	return buff;
    }
    
    public byte[] sendMsg(String Str, String Str2){
    	String linkUp = Str+"\0"+Str2;
    	byte[] buff = new byte[153];
    	byte[] temp = new byte[153];
    	try{
    		temp = linkUp.getBytes("UTF-8");
    	}catch (Exception e){
    		System.out.println("封包未成功: " + e.getMessage());    
    	}
    	//	utf-8 封包的时候 一个中文字符换成3比特
    	//for(int num=0;num<temp.length;num++){buff[num]=temp[num];}
    	//for(int num=0;num<1024;num++){System.out.print(buff[num]);}
    	buff[0] = (byte)9;
    	buff[1] = (byte)9;
    	buff[2] = (byte)9;
    	System.arraycopy(temp, 0, buff, 3, temp.length);
    	return buff;
    }
}
