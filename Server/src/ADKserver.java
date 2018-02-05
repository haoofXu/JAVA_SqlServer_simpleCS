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
	public static final int PORT = 12348;//�����Ķ˿ں�     
      
//    public static void main(String[] args) {    
//        System.out.println("����������...\n");    
//        ADKserver server = new ADKserver();    
//        server.init();    
//    }    
    
    //	�������͹̶����ȵİ�
    byte[] sendMsg(String Str){
    	byte[] buff = new byte[1024];
    	byte[] temp = new byte[1024];
    	try{
    		temp = Str.getBytes("UTF-8");
    	}catch (Exception e){
    		System.out.println("���δ�ɹ�: " + e.getMessage());    
    	}
    	//for(int num=0;num<1024;num++){System.out.println(buff[num]);}
    	// 	utf-8 �����ʱ�� һ�������ַ�����3����
    	//	num<Str.length()*3 ��������Ϊת���ֻռһλ
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
    		System.out.println("���δ�ɹ�: " + e.getMessage());    
    	}
    	//	utf-8 �����ʱ�� һ�������ַ�����3����
    	for(int num=0;num<temp.length;num++){buff[num]=temp[num];}
    	for(int num=0;num<1024;num++){System.out.print(buff[num]);}
    	return buff;
    }
    
    public void init() {    
        try {    
            ServerSocket serverSocket = new ServerSocket(PORT);    
            while (true) {    
                // һ���ж���, ���ʾ��������ͻ��˻��������    
                Socket client = serverSocket.accept();    
                // �����������    
                new HandlerThread(client);    
            }    
        } catch (Exception e) {    
            System.out.println("�������쳣: " + e.getMessage());    
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
                // ��ȡ�ͻ�������    
                DataInputStream input = new DataInputStream(socket.getInputStream());  
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
                switch (truebuf[0]){
                //	1
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
                //	2
                case 50:
                	type = 2;
                	System.arraycopy(truebuf,1,userBuf,0,10);
                	System.arraycopy(truebuf,11,passwordBuf,0,20);
                    System.out.println("��½:"); 
                	userStr = new String(userBuf);
                	passwordStr = new String(passwordBuf);
                    System.out.println("�û���:" + userStr); 
                    System.out.println("����:" + passwordStr);
                    break;
                //	3
                case 51:
                	type = 3;
                	break;
               	default:
                	type = 0;
                }
                
                // ��ͻ��˻ظ���Ϣ    
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
                
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
        	        case 1:
        	        	S.sql="insert into accountTable values('" + userStr + "', '"+ passwordStr +"')";
        	        	S.stmt=connection.prepareStatement(S.sql);
        	        	try{
                			S.stmt.execute();
                            out.write(sendMsg("ע��ɹ�"));
                            System.out.println("ע��ɹ���");
        	        	}
        	        	catch(Exception e){
        	        		System.out.println("ע��ʧ��: " + e.getMessage());
                            out.write(sendMsg("ע��ʧ��"));
        	        	}
        	        	break;
        	        //	��½
        	        case 2:
        	        	S.sql="select * from User where User = '" + userStr + "'";
            			S.stmt=connection.prepareStatement(S.sql);
            			S.rs=S.stmt.executeQuery();
            			
                        buf = "123".getBytes("UTF-8");
                        out.write(buf);
        	        	break;
        	        //	��ѯ����
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
        		    System.out.print("SQL Server����ʧ�ܣ�");  
        		}
                  
                out.close();    
                input.close();
            } catch (Exception e) {    
                System.out.println("������ run �쳣: " + e.getMessage());    
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