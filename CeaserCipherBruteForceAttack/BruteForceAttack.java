package bruteforceattack;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;

public class BruteForceAttack {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        int flag;
        StringBuilder text = new StringBuilder();

        FileReader fr = null;
        FileWriter fw = null;
        try
         {
             fr = new FileReader("sample.txt");
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
        String str = text.toString().replaceAll(" ", "");
        System.out.println(str);
        System.out.println("Enter key to shift: ");
        int key = scan.nextInt();
        BruteForceAttack bf = new BruteForceAttack();
        String cipher = null;
        cipher = bf.encrypt(str,key);
         
         try
         {
         	fw = new FileWriter("cipher.txt");
         	fw.write(cipher);
         	fw.close();
         }
         catch(Exception e)
         {
         	 System.out.println("File not found");
         }
         
         System.out.println("Encrypted data: "+ cipher);         
         System.out.println("Decrypting with key = 1");
         int comp = 0;
         int ch =5;
         String result = "";
         do
         {
            comp++;
            key = comp;
            result = bf.decrypt(cipher,key);
            System.out.println("Decrypted data :"+result);  
            System.out.print("Do you want to continue with key ="+(comp+1)+" ? (1/0)");
            ch = scan.nextInt();
         }while(ch == 1);
         if(ch == 0)
         {
            try
             {
                    fw = new FileWriter("decrypt.txt");
                    fw.write(result);
                    fw.close();
             }
             catch(Exception e)
             {
                     System.out.println("File not found");
             }
         }         
    }

   private String encrypt(String text, int key) {
        StringBuilder result = new StringBuilder();
        for(int i=0;i<text.length();i++)
        {
            if(Character.isUpperCase(text.charAt(i)))
            {
               char ch = (char) (((int)text.charAt(i) + key - 65)% 26 + 65);
               result.append(ch);              
            }
            else
            {
                char ch = (char)(((int)text.charAt(i) + key - 97)%26+97);
                result.append(ch);
            }
        }
        return result.toString();
    }

    private String decrypt(String cipher, int key) {
        StringBuilder result = new StringBuilder();
        for(int i=0;i<cipher.length();i++)
        {
            if(Character.isUpperCase(cipher.charAt(i)))
            {
               char ch = (char) (((int)cipher.charAt(i) - key + 65)% 26 + 65);
               result.append(ch);              
            }
            else
            {
                int c = cipher.charAt(i);
                c = c - (key %26);
                if(c<'a')
                    c=c+26;
                result.append((char)c);
            }
        }
    return result.toString();
    }
}
