package rabbit.struct.common;

import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.io.PrintWriter;

public class Matrix
{

   private double[][] A	= null;
   private int n		= 0;

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

   //Determinant using Gaussian elimination
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
      print(output,format,w+2);
   }

   public void print (NumberFormat format, int width) {
      print(new PrintWriter(System.out,true),format,width); }

   // DecimalFormat is a little disappointing coming from Fortran or C's printf.
   // Since it doesn't pad on the left, the elements will come out different
   // widths.  Consequently, we'll pass the desired column width in as an
   // argument and do the extra padding ourselves.

   /** Print the matrix to the output stream.  Line the elements up in columns.
     * Use the format object, and right justify within columns of width
     * characters.
     * Note that is the matrix is to be read back in, you probably will want
     * to use a NumberFormat that is set to US Locale.
   @param output the output stream.
   @param format A formatting object to format the matrix elements 
   @param width  Column width.
   @see java.text.DecimalFormat#setDecimalFormatSymbols
   */

   public void print (PrintWriter output, NumberFormat format, int width) {
      output.println();  // start on new line.
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            String s = format.format(A[i][j]); // format the number
            int padding = Math.max(1,width-s.length()); // At _least_ 1 space
            for (int k = 0; k < padding; k++)
               output.print(' ');
            output.print(s);
         }
         output.println();
      }
      output.println();   // end with blank line.
   }


}
