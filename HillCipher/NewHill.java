package hillcipher;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class NewHill{
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        StringBuilder text = new StringBuilder();
        StringBuilder text2 = new StringBuilder();
        StringBuilder encryptsb = new StringBuilder();
        
        StringBuilder finalsb = new StringBuilder();
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
       fr.close();
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
      //  System.out.print("Enter message: ");
      // String msg = scan.next();
        //*****************************
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
            text2.append((char)flag);
        }
        String tempmsg = text2.toString().replaceAll(" ", "");
        System.out.println("Message :"+tempmsg);
        System.out.println("tempmsg length" + tempmsg.length());
        
        String msg = "";
        for(int p=0;p<tempmsg.length();p=p+3)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(tempmsg.charAt(p));
            sb.append(tempmsg.charAt(p+1));
            sb.append(tempmsg.charAt(p+2));
            msg = sb.toString();
            System.out.println("Original Message: "+ msg);
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
            encryptsb.append(encryptedtext);
            

           int det = 0;
            for(int i=0;i<3;i++)
            {
                det = det + (keymatrix[0][i] * (keymatrix[1][(i+1)%3] * keymatrix[2][(i+2)%3] - keymatrix[1][(i+2)%3] * keymatrix[2][(i+1)%3]));
            }
            det = det % 26;
            System.out.println("Original determinant: "+det);
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

           // inversse of determinant
            for(int i=1;i<=26;i++)
            {
                if((det * i) % 26 == 1)
                {
                    det = i;
                    break;
                }
            }
            System.out.println("Inverse Determinant: "+ det); // to multiply
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
            System.out.println("decrypted: "+ decryptplaintext);
            System.out.println("***********************************");
            finalsb = finalsb.append(decryptplaintext);
            
        }
        System.out.println("*******************************************************************************");
        System.out.println("Encrypted text: "+ encryptsb);
            try
            {
                   fw = new FileWriter("cipher.txt");
                   fw.write(encryptsb.toString());
                   fw.close();
            }
            catch(Exception e)
            {
                    System.out.println("File not found");
            }
        System.out.println("Final Plaintext: "+finalsb);
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
