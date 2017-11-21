/**@author-Tejasvi Sharma
Linked List(FIFO)
*/
import java.io.*;
import java.util.*;

class Node{
  int data; //to store data
  Node next; //pointer to next object reference
} 

class Solution {
  Node head=null;
  //to inset data into a linked List
  void add(int data){
    Node n=new Node();
    n.data=data;
    n.next=null;
    if(head==null){
    head=n;  
    }else{
      Node temp=head;
      for(;temp.next!=null;temp=temp.next);
      temp.next=n;
    }
  }
  
  //to delete data()
  int delete(){
    int temp=head.data;
    head=head.next;
    return temp;
  }
  //to traverse data
  void print(){
    for(Node temp=head;temp!=null;temp=temp.next){
      System.out.print(temp.data+" ");
    }
    System.out.println();
  }
  public static void main(String[] args) {
  Scanner sc =new Scanner(System.in);
  int flag=1;
  Solution s=new Solution();
  while(flag==1){
    System.out.println("Menu");
    System.out.println("1.Insert data");
    System.out.println("2.Delete data");
    System.out.println("3.Print List");
    System.out.println("4.Quit");
    System.out.println("Enter your choics");
    int ch=sc.nextInt();
    switch(ch){
      case 1:{
        System.out.println("Enter data");
        int data=sc.nextInt();
        s.add(data);
        break;
      }
      case 2:{
        int d=s.delete();
        System.out.println("Data deleted is "+ d);
        break;
      }
      case 3:{
        s.print();
        break;
      }
      case 4:{
        flag=0;
        break;
      }
      default:{
        System.out.println("wrong choice");
        break;
      }
    }
      
  }
    
  
  }
}
