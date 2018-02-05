import java.io.BufferedReader;  
import java.io.DataInputStream;  
import java.io.DataOutputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.net.Socket;  
  
public class connectServer {  
    public static final String IP_ADDR = "127.0.0.1";//服务器地址   
    public static final int PORT = 12345;//服务器端口号
    static String ret = null;
    static String ret2 = null;
      
    public static void main(String[] args) {    
        System.out.println("客户端启动...");    
        System.out.println("当接收到服务器端字符为 \"OK\" 的时候, 客户端将终止\n");   
        while (true) {    
            Socket socket = null;  
            try {  
                //创建一个流套接字并将其连接到指定主机上的指定端口号  
                socket = new Socket(IP_ADDR, PORT);    
                    
                //读取服务器端数据    
                DataInputStream input = new DataInputStream(socket.getInputStream());    
                //向服务器端发送数据    
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
                System.out.print("请输入: \t");    
                String str = new BufferedReader(new InputStreamReader(System.in)).readLine();    
                //out.writeUTF(str);    
                byte[] buf = new byte[31];
                buf = str.getBytes();
                out.write(buf);    
                // 定义临时缓冲区
                buf = new byte[31];
                //	读数据
                int len = input.read(buf);
                // 根据实际数据长度定义输出缓冲区
                byte[] truebuf = new byte[len];
                // 拷贝实际的数据
                System.arraycopy(buf,0,truebuf,0,len);
                ret = new String(truebuf);
                
                System.out.println("服务器端返回过来的是: " + ret);    
                // 如接收到 "OK" 则断开连接    
                if ("OK".equals(ret)) {    
                    System.out.println("客户端将关闭连接");    
                    Thread.sleep(500);    
                    break;    
                }    
                  
                out.close();  
                input.close();  
            } catch (Exception e) {  
                System.out.println("客户端异常:" + e.getMessage());   
            } finally {  
                if (socket != null) {  
                    try {  
                        socket.close();  
                    } catch (IOException e) {  
                        socket = null;   
                        System.out.println("客户端 finally 异常:" + e.getMessage());   
                    }  
                }  
            }  
        }    
    }    
}    