/**@author-Tejasvi Sharma
Implementing BST
*/
import java.io.*;
import java.util.*;

class Node{
  int data; //to store data
  Node left;//pointer to left object reference
  Node right;//pointer to right object reference
} 

class Solution {
  Node head=null;
  //to inset data into a linked List
  void add(Node temp,int data){
    if(temp==null){
      Node n=new Node();
      n.data=data;
      n.left=null;
      n.right=null;
      head=n;
    }else if(data < temp.data){
      if(temp.left==null){
        Node n=new Node();
        n.data=data;
        n.left=null;
        n.right=null;
        temp.left=n;
      }else{
        add(temp.left,data);
      }
    }else if(data > temp.data){
      if(temp.right==null){
        Node n=new Node();
        n.data=data;
        n.left=null;
        n.right=null;
        temp.right=n;
      }else{
        add(temp.right,data);
      }
    }
  }
  
  //to delete data()
  Node delete(Node temp,int data){
     if(temp!=null){
      if(temp.data==data){
        if(temp.left==null && temp.right==null){
            return null;
        }else if(temp.right==null){
          Node temp1=temp.left;
          while(temp1.right!=null){
            temp1=temp1.right;
          }
          int d=temp1.data;
          delete(head,d);
          temp.data=d;
        }else{
          Node temp1=temp.right;
          while(temp1.left!=null){
            temp1=temp1.left;
          }
          int d=temp1.data;
          delete(head,d);
          temp.data=d;
        }
        
      }else{
      temp.left=delete(temp.left,data);
      temp.right=delete(temp.right,data);
      }
      return temp;
    }else{
       return null;
     }
  }
  //to traverse data
  void print(Node temp){
    if(temp!=null){
      print(temp.left);
      System.out.print(temp.data+" ");
      print(temp.right);  
    }
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
        s.add(s.head,data);
        break;
      }
      case 2:{
        System.out.println("Enter data to delete");
        int data=sc.nextInt();
        s.head=s.delete(s.head,data);
        break;
      }
      case 3:{
        s.print(s.head);
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
