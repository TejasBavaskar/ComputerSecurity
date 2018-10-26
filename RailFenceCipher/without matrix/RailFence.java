package railfencecipher;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class RailFence {
    static FileReader fr = null;
    static FileWriter fw = null;
    public static void main(String []args) throws IOException{
        Scanner scan = new Scanner(System.in);
        StringBuilder text = new StringBuilder();
        System.out.print("Enter key:");
        int key = scan.nextInt();
        
        int flag;
        
        //********Message********
        try
        {
            fr = new FileReader("message.txt");
        }
        catch(Exception e)
        {
            System.out.println("Message File not found");
        }
       while((flag = fr.read()) != -1)
       {
           text.append((char)flag);
       }
       fr.close();
       String msg = text.toString().replaceAll(" ", "");
        System.out.println("Original message: "+ msg);
       String ciphertext = encrypt(msg, key);
       System.out.println("Encrypted: "+ciphertext);
       String decrypttext = decrypt(ciphertext, key);
       System.out.println("Decrypted: "+decrypttext);
  
    }

    private static String encrypt(String msg, int key) {
        StringBuilder newsb = new StringBuilder();
        char ch;
        int swap=0; 
        int move=0;
        int firstindex = (2 * key) -3 ;
        int secondindex=0;
        int count=0;
        int strindex = 0;
        while(move < key)
        {
            while(strindex<msg.length())
            {
                    ch = msg.charAt(strindex);
                    strindex++;
                 
                    newsb.append(ch);
                    if(secondindex == 0)
                    {
                        secondindex = firstindex;
                    }
                    if(firstindex<0)
                    {
                        firstindex =secondindex;
                    }
                    if(swap == 0)
                    {
                        strindex = strindex + firstindex;
                        swap=1;
                    }
                    else{
                        strindex = strindex + secondindex;
                        swap = 0;
                    }
    
            }
            if(secondindex == (2*key)-3)
            {
                secondindex = -1;
            }
            swap = 0;
            move++;
            firstindex = firstindex - 2;
            secondindex = secondindex +2;
            strindex = move;
            
        }
        try
            {
                   fw = new FileWriter("cipher.txt");
                   fw.write(newsb.toString());
                   fw.close();
            }
            catch(Exception e)
            {
                    System.out.println("File not found");
            }
        
        
        return newsb.toString();   
    }

    private static String decrypt(String ciphertext, int key) {
       // System.out.println("In decrypt: "+ciphertext);
        StringBuilder newsb1 = new StringBuilder();
        char ch;
        int pointer = 0;
        int swap=0; 
        int move=0;
        int firstindex = (2 * key) -3 ;
        int secondindex=0;
        int count=0;
        int strindex = 0;
        char []plaintext = new char[ciphertext.length()];
        while(move < key)
        {
            while(strindex<ciphertext.length() && pointer<ciphertext.length())
            {
                    ch = ciphertext.charAt(count);
                    count++;
                    plaintext[strindex] = ch;
                    strindex++;
                    
                    
                    if(secondindex == 0)
                    {
                        secondindex = firstindex;
                    }
                    if(firstindex<0)
                    {
                        firstindex =secondindex;
                    }
                    if(swap == 0)
                    {
                        pointer=pointer+firstindex+1;
                        if(pointer<ciphertext.length())
                        {
                         strindex = strindex + firstindex;   
                        }
                        swap=1;
                    }
                    else{
                        pointer=pointer+secondindex+1;
                        if(pointer<ciphertext.length())
                        {
                            strindex = strindex + secondindex;
                        }
                        swap = 0;                        
                    }
    
            }
            //strindex = strindex -1;
            strindex = 0;
            if(secondindex == ((2*key)-3))
            {
                secondindex = -1;
            }
            swap = 0;
            move++;
            firstindex = firstindex - 2;
            secondindex = secondindex +2;
            pointer = move;
            strindex = strindex + move;
            
        }
        //System.out.println(plaintext[12]);
        String returnstr = new String(plaintext);
        try
            {
                   fw = new FileWriter("decrypt.txt");
                   fw.write(returnstr);
                   fw.close();
            }
            catch(Exception e)
            {
                    System.out.println("File not found");
            }
        
        return returnstr;        
    }

}
