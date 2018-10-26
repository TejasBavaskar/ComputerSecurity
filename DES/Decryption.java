package des;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Decryption {
    //To store 48 bit 16 different keys
    
    static StringBuilder[] key_48 = new StringBuilder[16];
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        StringBuilder PlainText = new StringBuilder();
        StringBuilder Key_64 = new StringBuilder();
        StringBuilder IP = new StringBuilder();
        StringBuilder LPT = new StringBuilder();
        StringBuilder RPT = new StringBuilder();
        FileReader fr1 = null;
        FileReader fr2 = null;
        FileWriter fw = null;
        int flag1;
        int flag2;
        //64 bit plaintext
        try
        {
            fr1 = new FileReader("decrypted.txt");
            fr2 = new FileReader("key.txt");
        }
        catch(Exception e)
        {
            System.out.println("File not found");
        }
       while((flag1 = fr1.read()) != -1)
       {
           PlainText.append((char)flag1);
       }
       fr1.close();
       while((flag2 = fr2.read()) != -1)
       {
           Key_64.append((char)flag2);
       }
       fr2.close();

     //****Key Generator**********************************
       KeyGenerator(Key_64);
       System.out.println("Keys: ");
       for(int i=0;i<16;i++)
       {
           System.out.println(""+(i+1)+": " + key_48[i]);
       }
     //*** Initial Permutaion and Left, Right part*****
       InitialPermutaion(PlainText,IP,LPT,RPT);
     //*** 16 rounds******
           Rounds_16(LPT, RPT,1);


    }
    private static void KeyGenerator(StringBuilder Key_64) {
        StringBuilder K_plus = new StringBuilder();
        StringBuilder C0 = new StringBuilder();
        StringBuilder D0 = new StringBuilder();
        StringBuilder C1 = new StringBuilder();
        StringBuilder D1 = new StringBuilder();
        int[] arr = {57 ,  49 ,   41  , 33  ,  25   , 17 ,   9,
               1  , 58  ,  50 ,  42  ,  34  ,  26  , 18,
              10  ,  2  ,  59 ,  51  ,  43  ,  35  , 27,
              19  , 11  ,   3 ,  60  ,  52  ,  44  , 36,
              63  , 55  ,  47 ,  39  ,  31  ,  23 ,  15,
               7  , 62  ,  54 ,  46  ,  38  ,  30 ,  22,
              14  ,  6  ,  61 ,  53  ,  45  ,  37 ,  29,
              21  , 13 ,    5  , 28  ,  20  ,  12 ,   4};
        
        for(int i=0;i<arr.length;i++)
        {
            K_plus.append(Key_64.charAt(arr[i]-1));
        }
    
        for(int i=0;i<28;i++)
        {
            C0.append(K_plus.charAt(i));
        }
        for(int i=28;i<56;i++)
        {
            D0.append(K_plus.charAt(i));
        }
        
        //***** Circular Shift*******
        for(int i=1; i<=16;i++)
        {
            if(i == 1 || i == 2 || i == 9 || i == 16)
            {
                C1 = circular_shift_left(C0,1);
                D1 = circular_shift_left(D0,1);
                Key_48_Generator(C1,D1,i);
                ///************
                //***************
                ////********* remember to 1 replace my i
                C0 = C1;
                D0 = D1;
            }
            else
            {
                C1 = circular_shift_left(C0,2);
                D1 = circular_shift_left(D0,2);
                Key_48_Generator(C1,D1,i);
                C0 = C1;
                D0 = D1;
            }
        }
    }
    private static StringBuilder circular_shift_left(StringBuilder sb, int shiftby) {
        final int length = sb.length();
     //   if( length == 0 ) return "";
        final int offset = ((shiftby % length) + length) % length; // get a positive offset

        String str =  sb.substring( offset, length ) + sb.substring( 0, offset );
        StringBuilder c_shift = new StringBuilder(str);
        return c_shift;
    }
     private static void Key_48_Generator(StringBuilder Cn, StringBuilder Dn, int round_num) {
         StringBuilder CnDn = new StringBuilder();
         StringBuilder Kn = new StringBuilder();
         CnDn.append(Cn).append(Dn);
         int[] arr = {14,17,11,24,1,5,3,28,15,6,21,10,
                                 23,19,12,4,26,8,16,7,27,20,13,2,
                                 41,52,31,37,47,55,30,40,51,45,33,48,
                                 44,49,39,56,34,53,46,42,50,36,29,32};
     
         for(int i=0;i<arr.length;i++)
         {
             Kn.append(CnDn.charAt(arr[i]-1));
         }
         key_48[16 - round_num] = Kn;
   
     }

    private static void InitialPermutaion(StringBuilder M,StringBuilder IP,StringBuilder LPT,StringBuilder RPT) {
        int[] arr = {58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,
                             62,54,46,38,30,22,14,6,64,56,48,40,32,24,16,8,
                             57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,
                             61,33,45,37,29,21,13,5,63,55,47,39,31,23,15,7};
        for(int i=0;i<arr.length;i++)
        {
            IP.append(M.charAt(arr[i]-1));
        }
        //L0 R0 after IP
        for(int i=0;i<32;i++)
        {
            LPT.append(IP.charAt(i));
        }
        for(int i=32;i<64;i++)
        {
            RPT.append(IP.charAt(i));
        }
        
    }

    private static void Rounds_16(StringBuilder LPT, StringBuilder RPT, int round_num) {
        StringBuilder temp = new StringBuilder();
        for(int i=1;i<=16;i++)
        {
            temp = mixer(LPT, RPT,i);
            LPT.setLength(0);
            RPT.setLength(0);
            for(int j=0;j<32;j++)
            {
                LPT.append(temp.charAt(j));
            }
         
            for(int j=32;j<64;j++)
            {
                RPT.append(temp.charAt(j));
            }
        }

         //******* inverse R16L16******
         StringBuilder reversetemp = new StringBuilder();
         reversetemp.append(RPT);
         reversetemp.append(LPT);
         //********* Inverse Permutation**********
         int[] Inv_p_arr = { 40  ,    8 ,  48  ,  16  ,  56  , 24  , 64  ,  32,
                             39  ,    7 ,  47  ,  15  ,  55  , 23  ,  63 ,  31,
                             38  ,   6 ,  46   , 14   ,  54  , 22  ,  62 ,  30,
                             37  ,   5 ,  45   , 13   ,  53  , 21  ,  61 ,  29,
                             36  ,   4 ,  44   , 12   ,   52  , 20 ,  60 ,  28,
                             35  ,   3 ,  43   , 11   ,   51  , 19 ,  59 ,  27,
                             34  ,   2 ,  42   , 10   ,   50  , 18 ,  58 ,  26,
                             33  ,   1 ,  41   ,  9   ,   49  , 17 ,  57 , 25};
         StringBuilder IP_inv = new StringBuilder();
         for(int i=0;i<Inv_p_arr.length;i++)
         {
             IP_inv.append(reversetemp.charAt(Inv_p_arr[i]-1));
         }
         
         System.out.println("Encryption: "+IP_inv);
   
    }

    private static StringBuilder mixer(StringBuilder LPT, StringBuilder RPT,int round_num) {
        StringBuilder temp = new StringBuilder();
        StringBuilder function_op = new StringBuilder();
        function( RPT,round_num,function_op);
        StringBuilder LPT_exor_function = new StringBuilder();
        ExOr(LPT, function_op, LPT_exor_function);
        //****Swapping*****
        LPT = RPT;
        RPT = LPT_exor_function;
        temp.append(LPT);
        temp.append(RPT);
        
        return temp;
    }

    private static void function(StringBuilder RPT,int round_num,StringBuilder function_op) {
        ///*****Expansion Permutation*******
        StringBuilder ER = new StringBuilder();
        int[] arr = {  32   ,  1  ,  2   ,  3  ,   4  ,  5,
                        4   ,  5  ,  6   ,  7  ,   8  ,  9,
                        8   ,  9  , 10   , 11  ,  12  , 13,
                       12   , 13  , 14   , 15  ,  16  , 17,
                       16   , 17  , 18  ,  19  ,  20  , 21,
                       20   , 21 ,  22  ,  23  ,  24  , 25,
                       24   , 25 ,  26  ,  27  ,  28  , 29,
                       28   , 29  , 30  ,  31  ,  32  ,  1};
        
        for(int i=0;i<arr.length;i++)
        {
            ER.append(RPT.charAt(arr[i]-1));
        }
        //****** EX-OR with 48 bit key*******
        StringBuilder key_exor_op = new StringBuilder();
        ExOr(ER,key_48[round_num-1],key_exor_op);
        
        //****** S-BOX Operation************
        StringBuilder sbox_op = new StringBuilder();
        for(int count =1;count<=8;count++)
        {
          switch(count)
            {
                case 1:
                    int[][] sbox_arr1 = {{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
                                        {0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8,},
                                        {4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
                                        {15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}};
                    sbox_op.append(compute(sbox_arr1,key_exor_op,0,5));
                    break;
                case 2:
                    int[][] sbox_arr2 = {{15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
                            {3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
                            {0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
                            {13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}};
                    sbox_op.append(compute(sbox_arr2,key_exor_op,6,11));
                    break;
                case 3:
                    int[][] sbox_arr3 = {{10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8},
                            {13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1},
                            {13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7},
                            {1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12}};
                    sbox_op.append(compute(sbox_arr3,key_exor_op,12,17));
                    break;
                case 4:
                    int[][] sbox_arr4 = {{7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15},
                            {13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9},
                            {10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4},
                            {3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14}};
                    sbox_op.append(compute(sbox_arr4,key_exor_op,18,23));
                    break;
                case 5:
                    int[][] sbox_arr5 = {{2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9},
                            {14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6},
                            {4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14},
                            {11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3}};
                    sbox_op.append(compute(sbox_arr5,key_exor_op,24,29));
                    break;
                case 6:
                    int[][] sbox_arr6 = {{12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11},
                            {10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8},
                            {9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6},
                            { 4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13}};
                    sbox_op.append(compute(sbox_arr6,key_exor_op,30,35));
                    break;
                case 7:
                    int[][] sbox_arr7 = {{4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1},
                            {13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6},
                            { 1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2},
                            {6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12}};
                    sbox_op.append(compute(sbox_arr7,key_exor_op,36,41));
                    break;
                case 8: 
                    int[][] sbox_arr8 = {{13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7},
                            {1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2},
                            {7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8},
                            {2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11}};
                    sbox_op.append(compute(sbox_arr8,key_exor_op,42,47));
                    break;
            }
        }
        
        //******* Straight P-Box operation********
        int[] P_arr = {16,   7 , 20 , 21,
                     29,  12 , 28 , 17,
                      1,  15 , 23 , 26,
                      5,  18 , 31 , 10,
                      2,   8 , 24 , 14,
                     32,  27 ,  3 ,  9,
                     19, 13 , 30  , 6,
                     22,  11,   4 , 25};
        
        for(int i=0;i<P_arr.length;i++)
        {
            function_op.append(sbox_op.charAt(P_arr[i]-1));
        }
    }

    private static void ExOr(StringBuilder ER, StringBuilder key_48, StringBuilder key_exor_op) {
        for(int i=0;i<key_48.length();i++)
        {
            key_exor_op.append((ER.charAt(i) ^ key_48.charAt(i)));
        }
    }
    private static StringBuilder compute(int[][] sbox_arr, StringBuilder key_exor_op, int start, int end) {
        StringBuilder row_binary = new StringBuilder();
        StringBuilder col_binary = new StringBuilder();
        int rowval,colval;
        row_binary.append(key_exor_op.charAt(start));
        row_binary.append(key_exor_op.charAt(end));
        
        for(int i= start+1;i<end;i++)
        {
            col_binary.append(key_exor_op.charAt(i));
        }
        rowval = Integer.parseInt(row_binary.toString(),2);
        colval = Integer.parseInt(col_binary.toString(),2);
        int sbox_int_val = sbox_arr[rowval][colval];
        //converting sbox int value to integer
        StringBuilder binary = new StringBuilder();
        binary.append(Integer.toBinaryString(sbox_int_val));
        for(int n=binary.length(); n<4; n++)
        {
            binary.insert(0, "0");
        }
        return binary;
    }


}
