import java.io.BufferedReader;  
import java.io.DataInputStream;  
import java.io.DataOutputStream;  
import java.io.InputStreamReader;  
import java.net.ServerSocket;  
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;  
  
public class ADKserver {  
	public static final int PORT = 12348;//监听的端口号     
      
//    public static void main(String[] args) {    
//        System.out.println("服务器启动...\n");    
//        ADKserver server = new ADKserver();    
//        server.init();    
//    }    
    
    //	用来发送固定长度的包
    byte[] sendMsg(String Str){
    	byte[] buff = new byte[1024];
    	byte[] temp = new byte[1024];
    	try{
    		temp = Str.getBytes("UTF-8");
    	}catch (Exception e){
    		System.out.println("封包未成功: " + e.getMessage());    
    	}
    	//for(int num=0;num<1024;num++){System.out.println(buff[num]);}
    	// 	utf-8 封包的时候 一个中文字符换成3比特
    	//	num<Str.length()*3 不行是因为转义符只占一位
    	for(int num=0;num<temp.length;num++){buff[num]=temp[num];}
    	return buff;
    }
    
    byte[] sendMsg(String Str, String Str2){
    	String linkUp = Str+"\0"+Str2;
    	byte[] buff = new byte[1024];
    	byte[] temp = new byte[1024];
    	try{
    		temp = linkUp.getBytes("UTF-8");
    	}catch (Exception e){
    		System.out.println("封包未成功: " + e.getMessage());    
    	}
    	//	utf-8 封包的时候 一个中文字符换成3比特
    	for(int num=0;num<temp.length;num++){buff[num]=temp[num];}
    	for(int num=0;num<1024;num++){System.out.print(buff[num]);}
    	return buff;
    }
    
    public void init() {    
        try {    
            ServerSocket serverSocket = new ServerSocket(PORT);    
            while (true) {    
                // 一旦有堵塞, 则表示服务器与客户端获得了连接    
                Socket client = serverSocket.accept();    
                // 处理这次连接    
                new HandlerThread(client);    
            }    
        } catch (Exception e) {    
            System.out.println("服务器异常: " + e.getMessage());    
        }    
    }    
    
    private class HandlerThread implements Runnable {    
        private Socket socket;
        private int type = 0;
        public HandlerThread(Socket client) {    
            socket = client;    
            new Thread(this).start();    
        }    
    
        public void run() {

            String userStr = null;
            String passwordStr = null;
            try {    
                // 读取客户端数据    
                DataInputStream input = new DataInputStream(socket.getInputStream());  
                // 定义临时缓冲区
                byte[] buf = new byte[31];
                //	读数据
                int len = input.read(buf);
                // 根据实际数据长度定义输出缓冲区
                byte[] truebuf = new byte[len];
                // 拷贝实际的数据
                System.arraycopy(buf,0,truebuf,0,len);
                
                
                
                
                String clientInputStr = new String(truebuf);
                // 处理客户端数据    
                System.out.println("客户端发过来的内容:" + clientInputStr);  
                
                byte[] userBuf = new byte[10];
                byte[] passwordBuf = new byte[20];
                switch (truebuf[0]){
                //	1
                case 49:
                	type = 1;
                	System.arraycopy(truebuf,1,userBuf,0,10);
                	System.arraycopy(truebuf,11,passwordBuf,0,20);
                	userStr = new String(userBuf);
                	passwordStr = new String(passwordBuf);
                    System.out.println("注册:"); 
                    System.out.println("用户名:" + userStr); 
                    System.out.println("密码:" + passwordStr);
                	break;
                //	2
                case 50:
                	type = 2;
                	System.arraycopy(truebuf,1,userBuf,0,10);
                	System.arraycopy(truebuf,11,passwordBuf,0,20);
                    System.out.println("登陆:"); 
                	userStr = new String(userBuf);
                	passwordStr = new String(passwordBuf);
                    System.out.println("用户名:" + userStr); 
                    System.out.println("密码:" + passwordStr);
                    break;
                //	3
                case 51:
                	type = 3;
                	break;
               	default:
                	type = 0;
                }
                
                // 向客户端回复信息    
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
                
                //	查询数据库
                String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";  
                String dbURL="jdbc:sqlserver://localhost:1433;DatabaseName=feiyi";  
        		String userName="sa";
        		String userPwd="Bloodyxu113";  
        		try{
        		    Class.forName(driverName);  
        		    System.out.println("加载驱动成功！");  
        		    Connection connection=DriverManager.getConnection(dbURL,userName,userPwd);  
        		    System.out.println("连接数据库成功！");  
        	        DBVar S=new DBVar();
        	        
        			String text = null;
        	        switch(type){
        	        //	注册
        	        case 1:
        	        	S.sql="insert into accountTable values('" + userStr + "', '"+ passwordStr +"')";
        	        	S.stmt=connection.prepareStatement(S.sql);
        	        	try{
                			S.stmt.execute();
                            out.write(sendMsg("注册成功"));
                            System.out.println("注册成功！");
        	        	}
        	        	catch(Exception e){
        	        		System.out.println("注册失败: " + e.getMessage());
                            out.write(sendMsg("注册失败"));
        	        	}
        	        	break;
        	        //	登陆
        	        case 2:
        	        	S.sql="select * from User where User = '" + userStr + "'";
            			S.stmt=connection.prepareStatement(S.sql);
            			S.rs=S.stmt.executeQuery();
            			
                        buf = "123".getBytes("UTF-8");
                        out.write(buf);
        	        	break;
        	        //	查询新闻
        	        case 3:
        	        	S.sql="select * from content where num = '" + clientInputStr + "'";
            			S.stmt=connection.prepareStatement(S.sql);
            			S.rs=S.stmt.executeQuery();
            			while(S.rs.next()){
            				text = S.rs.getString("context");
            			}
            			System.out.print(text);
            			out.writeUTF(text); 
        	        	break;
        	        }
        		}catch(Exception e)  
        		{  
        		    e.printStackTrace();  
        		    System.out.print("SQL Server连接失败！");  
        		}
                  
                out.close();    
                input.close();
            } catch (Exception e) {    
                System.out.println("服务器 run 异常: " + e.getMessage());    
            } finally {    
                if (socket != null) {    
                    try {    
                        //socket.close();    
                    } catch (Exception e) {    
                        socket = null;    
                        System.out.println("服务端 finally 异常:" + e.getMessage());    
                    }    
                }    
            }   
        }    
    }    
}    