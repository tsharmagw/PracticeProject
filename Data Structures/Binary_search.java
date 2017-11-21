import java.io.*;
import java.util.*;

class Solution {
  public static void main(String[] args) {
    Scanner sc=new Scanner(System.in);
    System.out.println("Enter array size");
    int size=sc.nextInt();
    System.out.println("Enter data to be searched");
    int search=sc.nextInt();
    int arr[]=new int[size];
    for(int i=0;i<size;i++){
      System.out.println("Enter array data")
      arr[i]=sc.nextInt();
    }
    int l=0;
    int h=arr.length-1;
    int flag=0;
    while(l<=h){
      int m=(l+h)/2;
      if(arr[m]==search){
        flag=1;
        System.out.print("Found");
        break;
      }else if(arr[m]<search){
        l=m+1;
      }else{
        h=m-1;
      }
    }
    
    if(flag==0){
      System.out.print("Not Found");
    }
    
  }
}
