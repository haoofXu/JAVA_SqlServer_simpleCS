import java.io.BufferedReader;  
import java.io.DataInputStream;  
import java.io.DataOutputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.net.Socket;  
  
public class connectServer {  
    public static final String IP_ADDR = "127.0.0.1";//��������ַ   
    public static final int PORT = 12345;//�������˿ں�
    static String ret = null;
    static String ret2 = null;
      
    public static void main(String[] args) {    
        System.out.println("�ͻ�������...");    
        System.out.println("�����յ����������ַ�Ϊ \"OK\" ��ʱ��, �ͻ��˽���ֹ\n");   
        while (true) {    
            Socket socket = null;  
            try {  
                //����һ�����׽��ֲ��������ӵ�ָ�������ϵ�ָ���˿ں�  
                socket = new Socket(IP_ADDR, PORT);    
                    
                //��ȡ������������    
                DataInputStream input = new DataInputStream(socket.getInputStream());    
                //��������˷�������    
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
                System.out.print("������: \t");    
                String str = new BufferedReader(new InputStreamReader(System.in)).readLine();    
                //out.writeUTF(str);    
                byte[] buf = new byte[31];
                buf = str.getBytes();
                out.write(buf);    
                // ������ʱ������
                buf = new byte[31];
                //	������
                int len = input.read(buf);
                // ����ʵ�����ݳ��ȶ������������
                byte[] truebuf = new byte[len];
                // ����ʵ�ʵ�����
                System.arraycopy(buf,0,truebuf,0,len);
                ret = new String(truebuf);
                
                System.out.println("�������˷��ع�������: " + ret);    
                // ����յ� "OK" ��Ͽ�����    
                if ("OK".equals(ret)) {    
                    System.out.println("�ͻ��˽��ر�����");    
                    Thread.sleep(500);    
                    break;    
                }    
                  
                out.close();  
                input.close();  
            } catch (Exception e) {  
                System.out.println("�ͻ����쳣:" + e.getMessage());   
            } finally {  
                if (socket != null) {  
                    try {  
                        socket.close();  
                    } catch (IOException e) {  
                        socket = null;   
                        System.out.println("�ͻ��� finally �쳣:" + e.getMessage());   
                    }  
                }  
            }  
        }    
    }    
}    