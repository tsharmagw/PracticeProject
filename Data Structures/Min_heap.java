import java.io.*;
import java.util.*;

class Solution {
  static void minheap(int arr[],int temp,int length){
    int left=(temp+1)*2;
    int right=(temp+1)*2+1;
    if(left>length && right>length){
    
    }else{
      if(left<=length)
        minheap(arr,left-1,length);
      if(right<=length)
        minheap(arr,right-1,length);
        
      if(arr[left-1] < arr[temp] && arr[left-1]< arr[right-1]){
        int t=arr[left-1];
        arr[left-1]=arr[temp];
        arr[temp]=t;
      }else if(arr[right-1]<arr[temp] && arr[right-1]<arr[left-1]){
        int t=arr[right-1];
        arr[right-1]=arr[temp];
        arr[temp]=t;
      }
    }
  }
  public static void main(String[] args) {
    Scanner sc=new Scanner(System.in);
    System.out.print("Enter array size");
    int size=sc.nextInt();
    int arr[]=new int[size];
    for(int i=0;i<size;i++){
      arr[i]=sc.nextInt();
    }
    minheap(arr,0,arr.length);
    for(int i=0;i<size;i++){
      System.out.print(arr[i]+" ");
    }
    
  }
}
