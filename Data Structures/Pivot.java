/**@author-Tejasvi Sharma
Code to rearrange the array according to a given pivot element. All the values less than the pivot are on the left hand side and all the values greater than 
pivot on the left handside.
*/
import java.io.*;
import java.util.*;

class Solution {
  static void pivot(int arr[],int pivot){
    int l=0;
    int h=arr.length-1;
    while(l<h){
      
      while(arr[l]<pivot && l<h){
        l++;
      }
      while(arr[h]>pivot && l<h){
        h--;
      }
      
      int temp=arr[l];
      arr[l]=arr[h];
      arr[h]=temp;
    }
    
  }
  public static void main(String[] args) {
  Scanner sc =new Scanner(System.in);
  System.out.println("Enter a pivot element");
  int n=sc.nextInt();
  System.out.print("Enter array Size");
  int size=sc.nextInt();
  int arr[]=new int[size];
  for(int i=0;i<size;i++){
    System.out.print("Enter a value");
    arr[i]=sc.nextInt();
  }
  pivot(arr,n);
  
  for(int i=0;i<size;i++){
    System.out.print(arr[i]+" ");
  }
  }
}
