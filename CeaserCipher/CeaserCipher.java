package ceasercipher;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CeaserCipher {
    public static void main(String[] args) throws IOException {
         Scanner scan = new Scanner(System.in);
         int flag;
         StringBuilder text = new StringBuilder();
         FileReader fr = null;
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
         String str = text.toString().replaceAll(" ", "");
         System.out.println(str);
         System.out.println("Enter key to shift: ");
         int key = scan.nextInt();
         CeaserCipher cc = new CeaserCipher();
         String cipher = null;
         cipher = cc.encrypt(str,key);
         System.out.println("Encrypted data: "+ cipher);
         System.out.println("Decrypted data: "+ cc.decrypt(cipher,key));
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
