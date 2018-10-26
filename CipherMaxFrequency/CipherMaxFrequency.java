package ciphermaxfrequency;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CipherMaxFrequency {
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
        String str = text.toString().replaceAll(" ", "");
        System.out.println(str);
         System.out.println("Enter key to shift: ");
         int key = scan.nextInt();
         CipherMaxFrequency cmf = new CipherMaxFrequency();
         String cipher = null;
         cipher = cmf.encrypt(str,key);
         System.out.println("Encrypted data: "+ cipher);
         
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
         cipher = cipher.toLowerCase();
         int length = cipher.length();
         int cnt[] = new int[26];
         for(int i=0;i<length;i++)
         {
             cnt[(cipher.charAt(i) - 97) %26]++;
         }
         
         int uniquecnt = (int) cipher.chars().distinct().count();
         System.out.println("Unique count: "+ uniquecnt);
         int rem = 0;
         int choice = 0;
         String newdecrypt = "";
         do
         {
             int max = cnt[0];
             for(int i=0;i<26;i++)
            {
                if(cnt[i] > max)
                {
                    max = cnt[i];
                    rem = i; 
                }      
            }
            rem = rem + 97;
            System.out.println("Highest frequency :"+(char)rem);
            int newkey = 0;

            if((char)rem > 'e')
            {
                newkey = rem - 'e';
            }
            else
            {
                newkey = 'e' - rem;
            }
           
            newdecrypt = cmf.decrypt(cipher, newkey);
            System.out.println("New decrypted :"+ newdecrypt);
            System.out.print("Do you want to continue?(1/0): ");
            choice = scan.nextInt();
            if(choice == 1)
            {
                rem = rem - 97;
                cnt[rem] = 0;
            }
            uniquecnt--;
         }while(choice == 1 && uniquecnt>0);
         if(choice == 0)
         {
         		try
			 {
			 	fw = new FileWriter("decrypt.txt");
			 	fw.write(newdecrypt);
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
