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
	public static final int PORT = 12343;//�����Ķ˿ں�
	
    public static void main(String[] args) {
    	FeiyiUnityServer server = new FeiyiUnityServer(); 
        System.out.println("����������...\n");
        tThread t = server.new tThread();
        t.start();  
    }
    
    public class tThread extends Thread {
    	@Override
    	public void run() { 

	        try {    
	            serverSocket = new ServerSocket(PORT);   
	            while (true) {    
	                // һ���ж���, ���ʾ��������ͻ��˻��������    
	                client = serverSocket.accept();
	            	//	��ȡ�ͻ�������    
	                DataInputStream input = new DataInputStream(client.getInputStream()); 
	                DataOutputStream out = new DataOutputStream(client.getOutputStream());   
	                
	                System.out.println("�пͻ������ӡ�");
	                clientThread = new ClientThread(input, out);
	                clientThread.start();
	            }    
	        } catch (Exception e) {    
	            System.out.println("�������쳣: " + e.getMessage());    
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

//            // ��ͻ��˻ظ���Ϣ    
//            try {
//                DataOutputStream out = new DataOutputStream(client.getOutputStream());
////                out.write(sendMsg("��ӭ��½"));
////                sleep(50);
////                out.write(sendMsg("��ӭ��½"));
//				System.out.println("��ӭ��½��\n");
//		        
//			} catch (IOException e1) {
//				System.out.println("��ӭʧ��: " + e1.getMessage());    
//			}
            
            while(keepAlive){
            	try { 
                    // ������ʱ������
                    byte[] buf = new byte[31];
                    //	������
                    int len = input.read(buf);
                    // ����ʵ�����ݳ��ȶ������������
                    byte[] truebuf = new byte[len];
                    // ����ʵ�ʵ�����
                    System.arraycopy(buf,0,truebuf,0,len);
                    String clientInputStr = new String(truebuf);
                    // ����ͻ�������    
                    System.out.println("�ͻ��˷�����������:" + clientInputStr);  
                    
                    byte[] userBuf = new byte[10];
                    byte[] passwordBuf = new byte[20];
                    byte[] searchBuf = new byte[1];
                    switch (truebuf[0]){
                    //	1	ע��
                    case 49:
                    	type = 1;
                    	System.arraycopy(truebuf,1,userBuf,0,10);
                    	System.arraycopy(truebuf,11,passwordBuf,0,20);
                    	userStr = new String(userBuf);
                    	passwordStr = new String(passwordBuf);
                        System.out.println("ע��:"); 
                        System.out.println("�û���:" + userStr); 
                        System.out.println("����:" + passwordStr);
                    	break;
                    //	2	��¼
                    case 50:
                    	type = 2;
                    	System.arraycopy(truebuf,1,userBuf,0,10);
                    	System.arraycopy(truebuf,11,passwordBuf,0,20);
                    	userStr = new String(userBuf);
                    	passwordStr = new String(passwordBuf);
                        System.out.println("��½:"); 
                        System.out.println("�û���:" + userStr); 
                        System.out.println("����:" + passwordStr);
                        break;
                    //	3	��ѯ
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

                            //	4	��ҳ��ѯ
                            case 52:
                            	break;
                        }
                    	type = 3;
                    	
                    	break;

                    case 52:
                    	type = 4;
                    	System.arraycopy(truebuf,1,userBuf,0,10);
                    	System.arraycopy(truebuf,11,passwordBuf,0,20);
                        System.out.println("��Ʊ��ѯ:"); 
                    	userStr = new String(userBuf);
                    	passwordStr = new String(passwordBuf);
                        System.out.println("��ɵ�:" + userStr); 
                        System.out.println("Ŀ�ĵ�:" + passwordStr);
                        break;
                    case 53:
                    	System.arraycopy(truebuf,1,userBuf,0,10);
                    	System.arraycopy(truebuf,11,passwordBuf,0,20);
                        System.out.println("��Ʊ:"); 
                    	userStr = new String(userBuf);
                    	passwordStr = new String(passwordBuf);
                        System.out.println("��ɵ�:" + userStr); 
                        System.out.println("Ŀ�ĵ�:" + passwordStr);
                    	type = 5;
                    	break;
                    case 54:
                    	System.arraycopy(truebuf,1,userBuf,0,10);
                    	System.arraycopy(truebuf,11,passwordBuf,0,20);
                        System.out.println("��Ӱ��:"); 
                    	userStr = new String(userBuf);
                    	passwordStr = new String(passwordBuf);
                        System.out.println("��ɵ�:" + userStr); 
                        System.out.println("Ŀ�ĵ�:" + passwordStr);
                        System.out.println("������:" + 100);
                    	type = 6;
                    	break;
                    case 55:
                    	System.arraycopy(truebuf,1,userBuf,0,10);
                    	System.arraycopy(truebuf,11,passwordBuf,0,20);
                        System.out.println("ɾ�����:"); 
                    	userStr = new String(userBuf);
                    	passwordStr = new String(passwordBuf);
                        System.out.println("��ɵ�:" + userStr); 
                        System.out.println("Ŀ�ĵ�:" + passwordStr);
                    	type = 7;
                    	break;
                   	default:
                    	type = 0;
                    }                    
                    //	��ѯ���ݿ�
                    String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";  
                    String dbURL="jdbc:sqlserver://localhost:1433;DatabaseName=feiyi";  
            		String userName="sa";
            		String userPwd="Bloodyxu113";  
            		try{
            		    Class.forName(driverName);  
            		    System.out.println("���������ɹ���");  
            		    Connection connection=DriverManager.getConnection(dbURL,userName,userPwd);  
            		    System.out.println("�������ݿ�ɹ���");  
            	        DBVar S=new DBVar();
            			String text = null;
            	        switch(type){
            	        //	ע��
            	        //	���ﻹûд����û����ظ��ļ�⡾��д��
            	        case 1:
            	        	S.sql="insert into accountTable values('" + userStr + "', '"+ passwordStr +"')";
            	        	S.stmt=connection.prepareStatement(S.sql);
            	        	try{
                    			S.stmt.execute();
                                out.write(xu.sendMsg("ע��ɹ�"));
                                System.out.println("ע��ɹ���\n");
            	        	}
            	        	catch(Exception e){
            	        		System.out.println("ע��ʧ��: " + e.getMessage());
                                out.write(xu.sendMsg("ע��ʧ��"));
            	        	}
            	        	break;
            	        //	��½
            	        //	����������¼��״̬�������Ҫ�Ļ���������
            	        case 2:
            	        	S.sql="select * from accountTable where Name = '" + userStr + "'";
                			S.stmt=connection.prepareStatement(S.sql);
                			S.rs=S.stmt.executeQuery();
                			try{
                    			while(S.rs.next()){
                    				text = S.rs.getString("Password");
                    			}
                    			if (text.equals(passwordStr)){
                    				out.write(xu.sendMsg("��¼�ɹ�"));
                    				
                    				if(passwordStr.equals("visitor0000000000000")){
                    					System.out.println("�ο͵�¼�ɹ���"); 
                    				}
                    				else{
                    					System.out.println("����Ա��¼�ɹ���"); 
                    				}
                        		    
                    			}
                    			else{
                    				out.write(xu.sendMsg("�������"));
                        		    System.out.println("�������"); 
                    			}
                			}catch(Exception e)  
                    		{  
                    		    e.printStackTrace();  
                    		    System.out.println("�޸��û���"); 
                				out.write(xu.sendMsg("�޸��û�")); 
                    		}
            	        	break;
            	        //	��ѯ��Ʊ
            	        case 4:
            	        	S.sql="select * from flight where start = '" + userStr + "' and destination = '" + passwordStr + "'" ;
                			S.stmt=connection.prepareStatement(S.sql);
                			S.rs=S.stmt.executeQuery();
                			while(S.rs.next()){
                				text = String.valueOf("��Ʊ��" + S.rs.getInt("count"));
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
                                        out.write(xu.sendMsg("Ԥ���ɹ�"));
                                        System.out.println("Ԥ���ɹ���\n");
                	        		}
                	        		else{
                	        			System.out.println("Ԥ��ʧ��: Ʊ�Ѷ��꣡");
                                        out.write(xu.sendMsg("Ԥ��ʧ��: Ʊ�Ѷ��꣡"));
                	        		}
                    			}
            	        	}
            	        	catch(Exception e){
            	        		System.out.println("Ԥ��ʧ��: " + e.getMessage());
                                out.write(xu.sendMsg("Ԥ��ʧ��"));
            	        	}
            	        	break;
            	        case 6:
            	        	S.sql="insert into flight values('" + userStr + "', '"+ passwordStr +"', 100)";
            	        	S.stmt=connection.prepareStatement(S.sql);
            	        	try{
                    			S.stmt.execute();
                                out.write(xu.sendMsg("��Ӻ���ɹ�"));
                                System.out.println("��Ӻ���ɹ���\n");
            	        	}
            	        	catch(Exception e){
            	        		System.out.println("��Ӻ���ʧ��: " + e.getMessage());
                                out.write(xu.sendMsg("��Ӻ���ʧ��"));
            	        	}
            	        	break;
            	        case 7:
            	        	S.sql="delete from flight where start = '" + userStr + "' and destination = '"+ passwordStr +"'";
            	        	S.stmt=connection.prepareStatement(S.sql);
            	        	try{
                    			S.stmt.execute();
                                out.write(xu.sendMsg("ɾ������ɹ�"));
                                System.out.println("ɾ������ɹ���\n");
            	        	}
            	        	catch(Exception e){
            	        		System.out.println("ɾ������ʧ��: " + e.getMessage());
                                out.write(xu.sendMsg("ɾ������ʧ��"));
            	        	}
            	        	break;
            	        }
            		}catch(Exception e)  
            		{  
            		    e.printStackTrace();  
            		    System.out.println("SQL Server����ʧ�ܣ�");  
            		}
                } catch (Exception e) {    
                    System.out.println("������ run �쳣: " + e.getMessage());
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
                            System.out.println("����� finally �쳣:" + e.getMessage());    
                        }    
                    }    
                }   
            }
            
        }    
    }    
}