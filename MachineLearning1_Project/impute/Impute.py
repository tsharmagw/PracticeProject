
# coding: utf-8

# In[1]:


#@author Tejasvi Sharma
#code to impute values from
#importing packages
import numpy as np
import pandas as pd


# In[2]:


#@function purpose -> to create imputer objects for columns with missing data values and return those objects, Only for cat columns
# @param train_data, data on which the imputer will learn
# @param col_list, columns with missing data
# @param col_list_dict, dict which has the type for every column
# @param missing_values_list, has the list of data which might be in place of missing data, we will impute these values
missing_values_list=["NA","N.A","#NA"," ",""," ?","na","NULL","null","_"]
def Impute_train_cat(train_data,col_list,col_list_dict):
    impute_dict={}
    for col in col_list:
        if(str.lower(col_list_dict[col]) == 'cat'):
            most_frequent=train_data[col].value_counts().idxmax()
        impute_dict[col]=most_frequent
    
    return(impute_dict)
              
    
def Impute_transform_cat(test_data,col_list,col_list_dict,impute_dict):
    imp_object_list=[]
    for col in col_list:
        if(str.lower(col_list_dict[col]) == 'cat'):
            most_common=impute_dict[col]
            print(most_common)
            def replace_most_common(x):
                if x in missing_values_list:
                    return most_common
                else:
                    return x
            test_data[col]= test_data[col].map(replace_most_common)
   
            
    return(test_data)
            
#tested working, imputing data values      
    



    
    


# In[3]:


#code used to test the module 
# df=pd.read_csv("../train_data.csv")
# dict1=Impute_train_cat(df,['workclass'],{'workclass':'cat'})
# #Impute_transform_cat(df,['workclass'],{'workclass':'cat'},)
# print(dict1)
# df1=Impute_transform_cat(df,['workclass'],{'workclass':'cat'},dict1)

