/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import sun.misc.BASE64Encoder;

/**
 *
 * @author BlueMoon
 */
public class Mail {
    public static String mailServer = "";
    public static String mailAddress = "";
    public static String password = "";
    public static String sendAddress = "";
    public static String sendTheme = "";
    public static String sendBody = "";
    
    static BASE64Encoder encode=new BASE64Encoder();
    
    static Socket socket;
    static Socket socket1;
 
    public static Boolean LoginOK()
    {
        try
        {
            if(!mailAddress.contains("@") || mailAddress.equals("") || mailAddress == null || password.equals("") || password == null)
                return false;
            mailServer = "smtp." + mailAddress.substring(mailAddress.lastIndexOf("@") + 1);
            socket = new Socket(mailServer, 25);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            int result = getResult(in);
            if(result!=220)
            {
                throw new IOException("连接服务器失败");
            }
            result = sendServer("HELO "+mailServer, in, out);
            if(result!=250)
            {
                throw new IOException("注册邮件服务器失败！");
            }
            result = sendServer("AUTH LOGIN",in,out);
            if(result!=334)
            {
                throw new IOException("用户验证失败！");
            }
            result = sendServer(encode.encode(mailAddress.getBytes()),in,out);
            if(result!=334)
            {
            throw new IOException("用户名错误！");
            }
            result=sendServer(encode.encode(password.getBytes()),in,out);
            if(result!=235)
            {
                throw new IOException("验证失败！");
            }
        }
        catch (SocketException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        //send
        try
        {
            if(!mailAddress.contains("@"))
                return false;
            if ((mailAddress.substring(mailAddress.lastIndexOf("@") + 1)).equals("qq.com"))
            {
                mailServer = "pop." + mailAddress.substring(mailAddress.lastIndexOf("@") + 1);
            }
            else
            { 
                mailServer = "pop3." + mailAddress.substring(mailAddress.lastIndexOf("@") + 1);
            }
            socket1 = new Socket(mailServer,110);
            BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream()));
            BufferedReader in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));

            String result1 = getResult1(in1);
            if(!result1.equals("+OK"))
            {
                throw new IOException("连接服务器失败");
            }
            result1 = sendServer1("user "+mailAddress,in1,out1);
            if(!result1.equals("+OK"))
            {
                throw new IOException("用户名错误！");
            }
            result1 = sendServer1("pass "+password,in1,out1);
            if(!result1.equals("+OK"))
            {
                throw new IOException("验证失败！"); 
            }
        }
        catch (SocketException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    private static int sendServer(String str,BufferedReader in,BufferedWriter out) throws IOException
    {
        out.write(str);
        out.newLine();
        out.flush();
        System.out.println("已发送命令:"+str);
        return getResult(in);
    }
    
    public static String sendServer1(String str,BufferedReader in,BufferedWriter out) throws IOException
    {
        out.write(str);
        out.newLine();
        out.flush();
        System.out.println("已发送命令:"+str);
        return getResult1(in);
    }

    public static int getResult(BufferedReader in)
    {
        String line="";
        try
        {
            line=in.readLine();
            System.out.println("服务器返回状态:"+line);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        StringTokenizer st=new StringTokenizer(line," ");
        return Integer.parseInt(st.nextToken());
    }
    
    public static String getResult1(BufferedReader in)
    {
        String line="";
        try
        {
            line=in.readLine();
            System.out.println("服务器返回状态:"+line);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        StringTokenizer st=new StringTokenizer(line," ");
        return st.nextToken();
    }

    public static Boolean SendMail(String sendaddress, String theme, String body)
    {
        try
        {
            int result;
            BufferedWriter out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            result=sendServer("MAIL FROM:<"+mailAddress+">", in, out);
            if(result!=250)
            {
                throw new IOException("指定源地址错误");
            }
            result=sendServer("RCPT TO:<"+sendaddress+">",in,out);
            if(result!=250)
            {
                throw new IOException("指定目的地址错误！");
            }
            result=sendServer("DATA",in,out);
            if(result!=354)
            {
                throw new IOException("不能发送数据");
            }
            out.write("From: "+ mailAddress);
            out.newLine();
            out.write("To: "+ sendaddress);
            out.newLine();
            out.write("Subject: "+ theme);
            out.newLine();
            out.newLine();
            out.write(body);
            out.newLine();
            result=sendServer(".",in,out);
            System.out.println(result);
            if(result!=250)
            {
                throw new IOException("发送数据错误");
            }
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }
    
    public static void Logout()
    {
        try
        {
            int result;
            BufferedWriter out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            result=sendServer("QUIT",in,out);
            if(result!=221)
            {
                throw new IOException("未能正确退出");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            String result1;
            BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream()));
            BufferedReader in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            result1 = sendServer1("quit",in1,out1);
            if(!result1.equals("+OK"))
            {
                throw new IOException("未能正确退出");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
