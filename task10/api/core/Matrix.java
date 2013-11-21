package task10.api.core;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.io.PrintWriter;

public class Matrix<T>
{

   public double[][] A	= null;
   public int n			= 0;

   public Matrix(int n)
   {
      this.n = n;
      A = new double[n][n];
   }

   public double get( int i, int j )
   { return A[i][j]; }

   public void set( int i, int j, double s )
   { A[i][j] = s; }

   public int determinant()
   { return determinant(n); }

   public int determinant( int n )
   {
	   double[][] LU = new double[n][n];
	   
	   for( int i = 0; i < n; i++ )
		   for (int j = 0; j < n; j++)
			   LU[i][j] = A[i][j];
	      
	   int[] piv = new int[n];
	   
	   for( int i = 0; i < n; i++ )
		   piv[i] = i;
	      
	   int sign		= 1;
	   double[] LUrowi	= null;
	   double[] LUcolj	= new double[n];

	   for( int j = 0; j < n; j++ )
	   {
		   for (int i = 0; i < n; i++)
			   LUcolj[i] = LU[i][j];

		   for (int i = 0; i < n; i++)
		   {
			   LUrowi = LU[i];

			   int kmax = Math.min(i,j);
			   double s = 0.0;

			   for (int k = 0; k < kmax; k++)
				   s += LUrowi[k]*LUcolj[k];
	    		  
			   LUrowi[j] = LUcolj[i] -= s;
		   }
	   
		   int p = j;
	         
		   for (int i = j+1; i < n; i++) if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p]))
			   p = i;
		   
		   if (p != j)
		   {
			   for (int k = 0; k < n; k++)
			   {
				   double t = LU[p][k];
				   LU[p][k] = LU[j][k];
				   LU[j][k] = t;
			   }
	        	 
			   int k	= piv[p];
			   piv[p]	= piv[j];
			   piv[j]	= k;
			   sign		= -sign;
		   }

		   if (j < n & LU[j][j] != 0.0)
		   {
			   for (int i = j+1; i < n; i++)
				   LU[i][j] /= LU[j][j];
		   }
	   }

	   int d = sign;
		   
	   for( int j = 0; j < n; j++ )
		   d *= LU[j][j];

	   return d;
   }

   public void print()
   { print(new PrintWriter(System.out,true),n,n); }

   public void print (PrintWriter output, int w, int d) {
      DecimalFormat format = new DecimalFormat();
      format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
      format.setMinimumIntegerDigits(1);
      format.setMaximumFractionDigits(d);
      format.setMinimumFractionDigits(d);
      format.setGroupingUsed(false);
      print(output,w+2);
   }

   public void print ( int width) {
      print(new PrintWriter(System.out,true),width); }

   public void print (PrintWriter output,  int width) {
      output.println();
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            String s = " "+((int)(A[i][j]));
            output.print(s);
         }
         output.println();
      }
      output.println();
   }

   public int[][] toArray()
   {
	   int[][] result = new int[n][n];
	   for(int i=0; i<A.length; i++)
		   for(int j=0; j<A.length; j++)
			   result[i][j]=(int)A[i][j];
	   return result;
   }


}
