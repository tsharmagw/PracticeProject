/**@author-Tejasvi Sharma
Counting sort, only for positive integers.
*/
import java.io.*;
import java.util.*;


class Solution {
  public static void main(String[] args) {
  Scanner sc =new Scanner(System.in);
  System.out.print("Enter array Size");
  int size=sc.nextInt();
  int arr[]=new int[size];
  int arr1[]=new int[size];
  int max=-1000;
  for(int i=0;i<size;i++){
    System.out.print("Enter a value");
    arr[i]=sc.nextInt();
    if(max<arr[i])
      max=arr[i];
  }
    
  int countArr[]=new int[max+1];
  for(int i=0;i<=max;i++){
    countArr[i]=0;
  }
    
  for(int i=0;i<size;i++){
    countArr[arr[i]]+=1;
  }
  
  for(int i=1;i<=max;i++){
    countArr[i]=countArr[i-1]+countArr[i];
  }
    
  // for(int i=0;i<=max;i++){
  //   System.out.print(countArr[i]+" ");
  // }
  for(int i=0;i<size;i++){
    arr1[countArr[arr[i]]-1]=arr[i];
    countArr[arr[i]]=countArr[arr[i]]-1;
  }
  //printing sorted data.
  for(int i=0;i<size;i++){
    System.out.print(arr1[i]+" ");
  }
    
  
  
  }
}
