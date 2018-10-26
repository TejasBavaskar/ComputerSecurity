package railfencecipher;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class RailFenceCipher {
    static StringBuilder cipher = new StringBuilder();
    static FileReader fr = null;
    static FileWriter fw = null;
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        StringBuilder text = new StringBuilder();
        
        
        int flag;
        
        //********Message********
        try
        {
            fr = new FileReader("message.txt");
        }
        catch(Exception e)
        {
            System.out.println("File not found");
        }
       while((flag = fr.read()) != -1)
       {
           text.append((char)flag);
       }
       fr.close();
       String msg = text.toString().replaceAll(" ", "");
       
       //******key********
       System.out.println("Enter key: ");
       int key = scan.nextInt();
       //***encryption***
       encryption(msg,key);
       
       //***decryption***
      
       decryption(cipher.toString(),key);
      //  System.out.println("Length:"+new1.length());
       
       
       
       
    }// end of main   
    
    public static char[][] CreateTable(String msg, int key){
        ///******** Table Creation************
       char Table[][] = new char[key][msg.length()];
       int rowcount = 0;
       int count = 0;
        for(int j=0;j<msg.length();j++)
        {
           if(count == rowcount)
           {
               Table[rowcount][j] = msg.charAt(j);
               count++;
               if(count == key)
               {
                   rowcount--;
               }
               else
               {
                   rowcount++;
               }
           }
           else
           {
               Table[rowcount][j] = msg.charAt(j);
               rowcount--;
               if(rowcount < 0)
               {
                   rowcount = rowcount + 2;
                   count = rowcount;
               }
           }            
        }
        return Table;
    }

    private static void encryption(String msg, int key) {
        
        char Table[][] = new char[key][msg.length()];
        Table = CreateTable(msg,key);
        System.out.println("Matrix:");
        for(int i=0;i<key;i++)
        {
            for(int j=0;j<msg.length();j++)
            {
                System.out.print(Table[i][j]+"\t");
                if(Character.isLetter(Table[i][j]))
                {
                    cipher.append(Table[i][j]);
                }
            }
            
            System.out.print("\n");
        }
        System.out.println("Encrypted text: "+cipher);
        try
        {
               fw = new FileWriter("cipher.txt");
               fw.write(cipher.toString());
               fw.close();
        }
        catch(Exception e)
        {
                System.out.println("File not found");
        }
    }

    private static void decryption(String msg, int key) {
       char Table[][] = new char[key][msg.length()];
       StringBuilder decryptmsg = new StringBuilder();
       int rowcount = 0;
       int count = 0;
       //***putting * in zig zag manner
        for(int j=0;j<msg.length();j++)
        {
           if(count == rowcount)
           {
               Table[rowcount][j] = '*';
               count++;
               if(count == key)
               {
                   rowcount--;
               }
               else
               {
                   rowcount++;
               }
           }
           else
           {
               Table[rowcount][j] = '*';
               rowcount--;
               if(rowcount < 0)
               {
                   rowcount = rowcount + 2;
                   count = rowcount;
               }
           }            
        }
        System.out.println("Decryption matrix: ");
        for(int i=0;i<key;i++)
        {
            for(int j=0;j<msg.length();j++)
            {
                System.out.print(Table[i][j]+"\t");
            }
            System.out.print("\n");
        }
        int index = 0;
        for(int i=0;i<key;i++)
        {
            for(int j=0;j<msg.length();j++)
            {
                if(Table[i][j] == '*')
                {
                    Table[i][j] = cipher.charAt(index);
                    index++;
                  
                            
                }
            }
        }
        System.out.println("Decryption matrix: ");
        for(int i=0;i<key;i++)
        {
            for(int j=0;j<msg.length();j++)
            {
                System.out.print(Table[i][j]+"\t");
            }
            System.out.print("\n");
        }
        //reading final decrypted matrix
        rowcount = 0;
        count = 0;
        StringBuilder finalsb = new StringBuilder();
        for(int j=0;j<msg.length();j++)
        {
           if(count == rowcount)
           {
               finalsb.append(Table[rowcount][j]);
               count++;
               if(count == key)
               {
                   rowcount--;
               }
               else
               {
                   rowcount++;
               }
           }
           else
           {
               finalsb.append(Table[rowcount][j]);
               rowcount--;
               if(rowcount < 0)
               {
                   rowcount = rowcount + 2;
                   count = rowcount;
               }
           }            
        }
        System.out.println("Decrypted text: "+ finalsb.toString());
        try
        {
               fw = new FileWriter("decrypt.txt");
               fw.write(finalsb.toString());
               fw.close();
        }
        catch(Exception e)
        {
                System.out.println("File not found");
        } 
    }// end of decryption
}
