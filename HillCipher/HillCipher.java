package hillcipher;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class HillCipher{
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        StringBuilder text = new StringBuilder();
        FileReader fr = null;
        FileWriter fw = null;
        int flag;
        
        //Key***********************
        try
        {
            fr = new FileReader("key.txt");
        }
        catch(Exception e)
        {
            System.out.println("File not found");
        }
       while((flag = fr.read()) != -1)
       {
           text.append((char)flag);
       }
       String key = text.toString().replaceAll(" ", "");
       
       int [][]keymatrix = new int[3][3];
       int difference[] = new int[9];
       int dif=0;
       for(int i=0;i<9;i++)
       {
          difference[i] = key.charAt(i) - 'a';
       }
       int count = 0;
       for(int i=0;i<3;i++)
       {
           for(int j=0;j<3;j++)
           {
               keymatrix[i][j] = difference[count];
               count++;
           }
       } 
        System.out.println("Key matrix:");
       for(int i=0;i<3;i++)
       {
           for(int j=0;j<3;j++)
           {
               System.out.print(keymatrix[i][j]+"\t");
           }
           System.out.println("\n");
       }
       
       //message***********
        System.out.print("Enter message: ");
        String msg = scan.next();
        
        int plaintext[][] = new int[3][1];
        for(int i=0;i<3;i++)
        {
            plaintext[i][0] = (int)(msg.charAt(i) - 'a');
        }
        System.out.println("Message matrix: ");
        for(int i=0;i<3;i++)
        {
            System.out.println(plaintext[i][0]);
        }
        int ciphermatrix[][] = new int[3][1];
        
         ////encryption*****
        ciphermatrix = encrypt(keymatrix,plaintext);  
        System.out.println("Encrypted matrix:");
        for(int i=0;i<3;i++)
       {
               System.out.println(ciphermatrix[i][0]+"\t");
       }
        String encryptedtext = "";
        
        for(int i=0;i<3;i++)
       {
           encryptedtext +=(char)(ciphermatrix[i][0] + 'a');
       }
        System.out.println("Encrypted text: "+ encryptedtext);
        try
        {
               fw = new FileWriter("cipher.txt");
               fw.write(encryptedtext);
               fw.close();
        }
        catch(Exception e)
        {
                System.out.println("File not found");
        }
        
       int det = 0;
        for(int i=0;i<3;i++)
        {
            det = det + (keymatrix[0][i] * (keymatrix[1][(i+1)%3] * keymatrix[2][(i+2)%3] - keymatrix[1][(i+2)%3] * keymatrix[2][(i+1)%3]));
        }
        det = det % 26;
        System.out.println("Original determinant:"+det);
        // negative determinant mod 26 ************************
        int rem=0;
        if(det < 0)
        {
            det = det * (-1);
            for(int x =1;x<=26;x++)
            {
                if((x*26)> det)
                {
                    rem = (x*26) - det;
                    det = rem;
                    break;
                }
            }
        }
        System.out.println("After change determinant: "+det);
        if(det == 2 || det == 13 || det % 2 ==0)
        {
            System.out.println("Determinant is 2 or 3 or even Incorrect");
            return;
        }
        
       // inversse of determinant*************************
        for(int i=1;i<=26;i++)
        {
            if((det * i) % 26 == 1)
            {
                det = i;
                break;
            }
        }
        System.out.println("Determinant Inverse: "+ det); // to multiply
        int adj[][] = new int[3][3];
        System.out.println("Adjoint matrix: ");
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                adj[i][j] = ((keymatrix[(j+1)%3][(i+1)%3] * keymatrix[(j+2)%3][(i+2)%3]) - (keymatrix[(j+1)%3][(i+2)%3] * keymatrix[(j+2)%3][(i+1)%3]));
                System.out.print(adj[i][j]+"\t");
            }
            System.out.println("\n");
        }
        rem=0;
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(adj[i][j] < 0)
                {
                    adj[i][j] = adj[i][j]* (-1);
                    for(int x =1;x<=26;x++)
                    {
                        if((x*26)>adj[i][j])
                        {
                            rem = (x*26) - adj[i][j];
                            adj[i][j] = rem;
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("After removing negative number in adjoint matrix");
        for(int i=0;i<3;i++)
       {
           for(int j=0;j<3;j++)
           {
               System.out.print(adj[i][j]+"\t");
           }
           System.out.println("\n");
       }
        //Finding inverse of keymatrix
        int inversematrix[][] = new int[3][3];
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                inversematrix[i][j] = (det * adj[i][j]) % 26;
            }
        }
        System.out.println("Inverse of key matrix: ");
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                System.out.print(inversematrix[i][j]+"\t");
            }
            System.out.println("\n");
        }
        
        int decryptmatrix[][] = new int[3][1];
        decryptmatrix = multiplication(inversematrix, ciphermatrix);
        System.out.println("Plaintext :");
        for(int i=0;i<3;i++)
       {
            System.out.print(decryptmatrix[i][0]+"\t");
       }
        System.out.println("");
        String decryptplaintext = "";
        for(int i=0;i<3;i++)
       {
           decryptplaintext +=(char)(decryptmatrix[i][0] + 'a');
       }
        System.out.println("Final plaintext: "+ decryptplaintext);
        try
        {
               fw = new FileWriter("decrypt.txt");
               fw.write(decryptplaintext);
               fw.close();
        }
        catch(Exception e)
        {
                System.out.println("File not found");
        }
        
        
        
        
        

    } // end of main

    private static int[][] encrypt(int[][] matrix, int[][] plaintext) {
           int multiply[][] = new int[3][0];
           multiply = multiplication(matrix,plaintext);
           return multiply;
    
    }

    private static int[][] multiplication(int[][] matrix, int[][] plaintext) {
        int ciphermat[][] = new int[3][1];
            for(int i=0;i<3;i++)
            {
                for(int j=0;j<3;j++)
                {
                    ciphermat[i][0] = 0;
                    for(int k=0;k<3;k++)
                    {
                        ciphermat[i][0] = ciphermat[i][0] + matrix[i][k] * plaintext[k][0];
                    }
                    ciphermat[i][0] = ciphermat[i][0] % 26; 
                    
                }
            }
            return ciphermat;
            
    }// end of multiplication
    
     
   

   

    
    
}
