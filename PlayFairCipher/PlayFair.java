package playfair;

import java.awt.Point;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PlayFair {
    static char [][]charTable;
    static Point []positions;
    public static void main(String []args) throws IOException{
            Scanner scan = new Scanner(System.in);
            StringBuilder text = new StringBuilder();
            FileReader fr = null;
            FileWriter fw = null;
            int flag;
            //message***************
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
            String msg = text.toString().replaceAll(" ", "");

            System.out.print("Enter the key: ");
            String key = scan.nextLine();
            //System.out.print("Enter message: ");
            //String msg = scan.nextLine();
            System.out.print("Change j to i?(y/n): ");
            String jtoi = scan.nextLine();

            boolean changejtoi = jtoi.equalsIgnoreCase("y");

            createTable(key, changejtoi);
            String cipher = encrypt(prepareText(msg, changejtoi));

            System.out.println("Encoded message is :"+cipher);
            try
            {
                   fw = new FileWriter("cipher.txt");
                   fw.write(cipher);
                   fw.close();
            }
            catch(Exception e)
            {
                    System.out.println("Cipher File not found");
            }

            String dec_result = dececrypt(cipher);
            System.out.println("Decoded message is :"+dec_result);
            try
            {
                   fw = new FileWriter("decode.txt");
                   fw.write(dec_result);
                   fw.close();
            }
            catch(Exception e)
            {
                    System.out.println("Decrypt file not found");
            }
	}

	public static void createTable(String key, boolean changejtoi){
		charTable = new char[5][5];
		positions = new Point[26];

		String s = prepareText(key + "ABCDEFGHIJKLMNOPQRSTUVWXYZ", changejtoi);
                System.out.println("Prepared text: "+ s);
		for(int i=0, k=0;i<s.length();i++)
		{
			char ch = s.charAt(i);
			if(positions[ch - 'A'] == null)
			{
				charTable[k/5][k%5] = ch;
				positions[ch - 'A'] = new Point(k%5,k/5);                     
				k++;
			}
		}
                for(int i=0;i<5;i++)
                {
                    for(int j=0;j<5;j++)
                    {
                        System.out.print(charTable[i][j]+"\t");
                    }
                    System.out.println("\n");
                }
		
	
	}

	public static String prepareText(String text, boolean changejtoi){
		text = text.toUpperCase().replaceAll("[^A-Z]","");
		return changejtoi ? text.replace("J","I") : text.replace("I","J");
	}

	public static String encrypt(String text){
		StringBuilder sb = new StringBuilder(text);
               // System.out.println("StrnigBuilder sb: "+ sb);
		for(int i=0;i<sb.length();i=i+2)
		{
			//System.out.println("StrnigBuilder sb after "+i+": "+sb.toString());
			
			 if(i == sb.length()-1)
			{
				if(sb.length() % 2 == 1 )
				{
					sb.append('X');
				}
				else
				{
					sb.append("");
				}
				
			}
			 else{
				 if (sb.charAt(i) == sb.charAt(i + 1)){
                                        sb.insert(i + 1, 'X');
					}
			 }
		}
                System.out.println("Message after grouping: ");
                for(int i=0;i<sb.length();i=i+2)
                {
                    char a = sb.toString().charAt(i);
		    char b = sb.toString().charAt(i+1);
                    System.out.print(a+""+b+"\t");
                }
                System.out.println("\n");
		return compute(sb,1);
	}

	public static String compute(StringBuilder sb,int direction){
		for(int i=0;i<sb.length();i= i+2)
		{
			char a = sb.charAt(i);
			char b = sb.charAt(i+1);
		
			int row1 = positions[a - 'A'].y;
			int row2 = positions[b - 'A'].y;
			int col1 = positions[a - 'A'].x;
			int col2 = positions[b - 'A'].x;

			if(row1 == row2)
			{
				col1 = (col1 + direction) %5;
				col2 = (col2 + direction)%5;
			}
			else if(col1 == col2)
			{
				row1 = (row1 + direction) %5;
				row2 = (row2 + direction)%5;
			}
			else 
			{
				int temp = col1;
				col1 = col2;
				col2 = temp;
			}

			sb.setCharAt(i , charTable[row1][col1]);
			sb.setCharAt(i+1 , charTable[row2][col2]);
		}
	return sb.toString();

	}

	public static String dececrypt(String text){
		StringBuilder sb1 = new StringBuilder(text);
		return compute(sb1,4);

	}

    
}
