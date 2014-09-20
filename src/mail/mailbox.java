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
import java.util.*;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 *
 * @author BlueMoon
 */
public class mailbox {
    public int number = 0;
    Socket socket1;
    public mailbox()
    {
        try
        {
            socket1 = Mail.socket1;
            BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream()));
            BufferedReader in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            out1.write("stat");
            out1.newLine();
            out1.flush();
            System.out.println("已发送命令:"+"stat");
            String line="";
            line=in1.readLine();
            System.out.println("服务器返回状态:"+line);
            StringTokenizer st = new StringTokenizer(line," ");
            st.nextToken();
            number = Integer.parseInt(st.nextToken());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void renew()
    {
        try
        {
            BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream()));
            BufferedReader in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            out1.write("stat");
            out1.newLine();
            out1.flush();
            System.out.println("已发送命令:"+"stat");
            String line="";
            line=in1.readLine();
            System.out.println("服务器返回状态:"+line);
            StringTokenizer st = new StringTokenizer(line," ");
            st.nextToken();
            number = Integer.parseInt(st.nextToken());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public String open(int x)
    {
        try
        {
            socket1 = Mail.socket1;
            BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream()));
            BufferedReader in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            out1.write("RETR " + x);
            out1.newLine();
            out1.flush();
            System.out.println("已发送命令:"+"RETR "+ x);
            String line = "";
            String tmp = in1.readLine();
            StringTokenizer st = new StringTokenizer(tmp," ");
            if (!st.nextToken().equals("+OK"))
            {
                return null;
            }
            while(!(tmp = in1.readLine()).equals("."))
            {
                System.out.println("服务器返回状态:"+tmp);
                line += tmp;
                line += "\n";
            }
            return line;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    
}
