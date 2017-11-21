/**@author-Tejasvi Sharma
//code to sort the data using quick sort
*/
import java.io.*;
import java.util.*;

class Solution {
  static void quicksort(int arr[],int l,int h){
    if(l<h){
      int index=pivot(arr,l,h);
      quicksort(arr,l,index-1);
      quicksort(arr,index+1,h);
      
    }
  }
  static int pivot(int arr[],int l,int h){
    int pivot =arr[0];
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
    return l;
    
  }
  public static void main(String[] args) {
  Scanner sc =new Scanner(System.in);
  System.out.print("Enter array Size");
  int size=sc.nextInt();
  int arr[]=new int[size];
  for(int i=0;i<size;i++){
    System.out.print("Enter a value");
    arr[i]=sc.nextInt();
  }
  
  quicksort(arr,0,arr.length-1);
  for(int i=0;i<size;i++){
    System.out.print(arr[i]);
  }  
  
  // for(int i=0;i<size;i++){
  //   System.out.print(arr[i]+" ");
  // }
  }
}
