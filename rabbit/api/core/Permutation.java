package pal.api.core;

import java.util.Arrays;

public class Permutation
{
    private int[] values;

    public Permutation(int size)
    {
        this.values = new int[size];
        
        for (int i = 0; i < size; i++)
            this.values[i] = i;
    }
    
    public Permutation(int ... values)
    { this.values = values; }
    
    public Permutation(Permutation other)
    { this.values = other.values.clone(); }
    
    public boolean equals(Object other)
    { return (other instanceof Permutation) ? Arrays.equals(values, ((Permutation)other).values) : false; }
    
    public int get(int i)
    { return this.values[i]; }
    
    public int[] getValues()
    { return this.values; }
    
    public void set(int index, int value)
    { this.values[index] = value; }

    public String toString()
    { return Arrays.toString(this.values); }

}