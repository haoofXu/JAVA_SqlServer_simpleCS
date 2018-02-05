import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;

public class FeiyiUnityServer {
	xuAppTxt xu = new xuAppTxt();
	ServerSocket serverSocket;
	Socket clientSocket;
	ClientThread clientThread;
	Socket client;
	public static final int PORT = 12343;//监听的端口号
	
    public static void main(String[] args) {
    	FeiyiUnityServer server = new FeiyiUnityServer(); 
        System.out.println("服务器启动...\n");
        tThread t = server.new tThread();
        t.start();  
    }
    
    public class tThread extends Thread {
    	@Override
    	public void run() { 

	        try {    
	            serverSocket = new ServerSocket(PORT);   
	            while (true) {    
	                // 一旦有堵塞, 则表示服务器与客户端获得了连接    
	                client = serverSocket.accept();
	            	//	读取客户端数据    
	                DataInputStream input = new DataInputStream(client.getInputStream()); 
	                DataOutputStream out = new DataOutputStream(client.getOutputStream());   
	                
	                System.out.println("有客户端连接。");
	                clientThread = new ClientThread(input, out);
	                clientThread.start();
	            }    
	        } catch (Exception e) {    
	            System.out.println("服务器异常: " + e.getMessage());    
	        }
    	}
    }
    
    
    public class ClientThread extends Thread {
    	boolean keepAlive = true;    	
        private Socket socket;
        private int type = 0;
        DataInputStream input; 
        DataOutputStream out;
        
        public ClientThread(DataInputStream inin, DataOutputStream outout){
        	input = inin;
        	out = outout;
        }
        public void run() {
        	socket = client;
        	
            String userStr = null;
            String passwordStr = null;
            String searchStr = null;

//            // 向客户端回复信息    
//            try {
//                DataOutputStream out = new DataOutputStream(client.getOutputStream());
////                out.write(sendMsg("欢迎登陆"));
////                sleep(50);
////                out.write(sendMsg("欢迎登陆"));
//				System.out.println("欢迎登陆！\n");
//		        
//			} catch (IOException e1) {
//				System.out.println("欢迎失败: " + e1.getMessage());    
//			}
            
            while(keepAlive){
            	try { 
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
                    byte[] searchBuf = new byte[1];
                    switch (truebuf[0]){
                    //	1	注册
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
                    //	2	登录
                    case 50:
                    	type = 2;
                    	System.arraycopy(truebuf,1,userBuf,0,10);
                    	System.arraycopy(truebuf,11,passwordBuf,0,20);
                    	userStr = new String(userBuf);
                    	passwordStr = new String(passwordBuf);
                        System.out.println("登陆:"); 
                        System.out.println("用户名:" + userStr); 
                        System.out.println("密码:" + passwordStr);
                        break;
                    //	3	查询
                    case 51:
                    	System.arraycopy(truebuf,1,searchBuf,0,1);
                    	searchStr = new String(searchBuf);
                        switch (truebuf[1]){
	                    	//	1
	                    	case 49:
	                    		break;
                        	//	2
                        	case 50:
                        		break;
                        	//	3
                        	case 51:
                        		break;

                            //	4	首页查询
                            case 52:
                            	break;
                        }
                    	type = 3;
                    	
                    	break;

                    case 52:
                    	type = 4;
                    	System.arraycopy(truebuf,1,userBuf,0,10);
                    	System.arraycopy(truebuf,11,passwordBuf,0,20);
                        System.out.println("余票查询:"); 
                    	userStr = new String(userBuf);
                    	passwordStr = new String(passwordBuf);
                        System.out.println("起飞点:" + userStr); 
                        System.out.println("目的地:" + passwordStr);
                        break;
                    case 53:
                    	System.arraycopy(truebuf,1,userBuf,0,10);
                    	System.arraycopy(truebuf,11,passwordBuf,0,20);
                        System.out.println("购票:"); 
                    	userStr = new String(userBuf);
                    	passwordStr = new String(passwordBuf);
                        System.out.println("起飞点:" + userStr); 
                        System.out.println("目的地:" + passwordStr);
                    	type = 5;
                    	break;
                    case 54:
                    	System.arraycopy(truebuf,1,userBuf,0,10);
                    	System.arraycopy(truebuf,11,passwordBuf,0,20);
                        System.out.println("添加班次:"); 
                    	userStr = new String(userBuf);
                    	passwordStr = new String(passwordBuf);
                        System.out.println("起飞点:" + userStr); 
                        System.out.println("目的地:" + passwordStr);
                        System.out.println("客容量:" + 100);
                    	type = 6;
                    	break;
                    case 55:
                    	System.arraycopy(truebuf,1,userBuf,0,10);
                    	System.arraycopy(truebuf,11,passwordBuf,0,20);
                        System.out.println("删除班次:"); 
                    	userStr = new String(userBuf);
                    	passwordStr = new String(passwordBuf);
                        System.out.println("起飞点:" + userStr); 
                        System.out.println("目的地:" + passwordStr);
                    	type = 7;
                    	break;
                   	default:
                    	type = 0;
                    }                    
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
            	        //	这里还没写如果用户名重复的检测【待写】
            	        case 1:
            	        	S.sql="insert into accountTable values('" + userStr + "', '"+ passwordStr +"')";
            	        	S.stmt=connection.prepareStatement(S.sql);
            	        	try{
                    			S.stmt.execute();
                                out.write(xu.sendMsg("注册成功"));
                                System.out.println("注册成功！\n");
            	        	}
            	        	catch(Exception e){
            	        		System.out.println("注册失败: " + e.getMessage());
                                out.write(xu.sendMsg("注册失败"));
            	        	}
            	        	break;
            	        //	登陆
            	        //	可以再做登录的状态，如果需要的话【待做】
            	        case 2:
            	        	S.sql="select * from accountTable where Name = '" + userStr + "'";
                			S.stmt=connection.prepareStatement(S.sql);
                			S.rs=S.stmt.executeQuery();
                			try{
                    			while(S.rs.next()){
                    				text = S.rs.getString("Password");
                    			}
                    			if (text.equals(passwordStr)){
                    				out.write(xu.sendMsg("登录成功"));
                    				
                    				if(passwordStr.equals("visitor0000000000000")){
                    					System.out.println("游客登录成功！"); 
                    				}
                    				else{
                    					System.out.println("管理员登录成功！"); 
                    				}
                        		    
                    			}
                    			else{
                    				out.write(xu.sendMsg("密码错误"));
                        		    System.out.println("密码错误！"); 
                    			}
                			}catch(Exception e)  
                    		{  
                    		    e.printStackTrace();  
                    		    System.out.println("无该用户！"); 
                				out.write(xu.sendMsg("无该用户")); 
                    		}
            	        	break;
            	        //	查询余票
            	        case 4:
            	        	S.sql="select * from flight where start = '" + userStr + "' and destination = '" + passwordStr + "'" ;
                			S.stmt=connection.prepareStatement(S.sql);
                			S.rs=S.stmt.executeQuery();
                			while(S.rs.next()){
                				text = String.valueOf("余票：" + S.rs.getInt("count"));
                    			System.out.println(text);
                			}
                           	out.write(xu.sendMsg(text));
                    		out.flush();
                			
            	        	break;
            	        case 5:
            	        	S.sql="select * from flight where start = '" + userStr + "' and destination = '" + passwordStr + "'" ;
                			S.stmt=connection.prepareStatement(S.sql);
                			S.rs=S.stmt.executeQuery();
            	        	try{
                    			while(S.rs.next()){
                    				int ticketNum = S.rs.getInt("count");
                    				if(ticketNum>0){
                    					S.sql="update flight set count =" + (ticketNum-1) + " where start = '" + userStr + "' and destination = '" + passwordStr + "'" ;
                        	        	S.stmt=connection.prepareStatement(S.sql);
                            			S.stmt.execute();
                                        out.write(xu.sendMsg("预定成功"));
                                        System.out.println("预定成功！\n");
                	        		}
                	        		else{
                	        			System.out.println("预定失败: 票已订完！");
                                        out.write(xu.sendMsg("预定失败: 票已订完！"));
                	        		}
                    			}
            	        	}
            	        	catch(Exception e){
            	        		System.out.println("预定失败: " + e.getMessage());
                                out.write(xu.sendMsg("预定失败"));
            	        	}
            	        	break;
            	        case 6:
            	        	S.sql="insert into flight values('" + userStr + "', '"+ passwordStr +"', 100)";
            	        	S.stmt=connection.prepareStatement(S.sql);
            	        	try{
                    			S.stmt.execute();
                                out.write(xu.sendMsg("添加航班成功"));
                                System.out.println("添加航班成功！\n");
            	        	}
            	        	catch(Exception e){
            	        		System.out.println("添加航班失败: " + e.getMessage());
                                out.write(xu.sendMsg("添加航班失败"));
            	        	}
            	        	break;
            	        case 7:
            	        	S.sql="delete from flight where start = '" + userStr + "' and destination = '"+ passwordStr +"'";
            	        	S.stmt=connection.prepareStatement(S.sql);
            	        	try{
                    			S.stmt.execute();
                                out.write(xu.sendMsg("删除航班成功"));
                                System.out.println("删除航班成功！\n");
            	        	}
            	        	catch(Exception e){
            	        		System.out.println("删除航班失败: " + e.getMessage());
                                out.write(xu.sendMsg("删除航班失败"));
            	        	}
            	        	break;
            	        }
            		}catch(Exception e)  
            		{  
            		    e.printStackTrace();  
            		    System.out.println("SQL Server连接失败！");  
            		}
                } catch (Exception e) {    
                    System.out.println("服务器 run 异常: " + e.getMessage());
                    try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
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
}