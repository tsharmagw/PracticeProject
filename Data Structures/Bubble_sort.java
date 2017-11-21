/** @author- Tejasvi Sharma
Code for bubble sorting technique.
*/
import java.io.*;
import java.util.*;

class Bubble {
  public static void main(String[] args) {
  Scanner sc =new Scanner(System.in);
  System.out.println("Enter size of array");
  int n=sc.nextInt();
  int arr[]=new int[n];
  for(int i=0;i<n;i++){
    System.out.print("enter a number");
    arr[i]=sc.nextInt();
  }
    
  for(int i=0;i<n-1;i++){
    for(int j=0;j<n-i-1;j++){
      if(arr[j]>arr[j+1]){
        int temp=arr[j];
        arr[j]=arr[j+1];
        arr[j+1]=temp;
      }
    }
  }
  //printing sorted array
  for(int i=0;i<n;i++){
    System.out.print(arr[i]+" ");
  }
  
  }
}
