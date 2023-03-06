#include <cstdio>
#include <cstdlib>
#include <math.h>
#include <limits>
#include "sort.h"


int ivector_length(int* v, int n)
{
  int sum;

  sum = 0;
  for (int i = 0; i < n; i++)
    sum += (v[i] < 0) ? -v[i] : v[i];

  return sum;
}

int* arrayOfvectorLengths(int** A,int rows,int columns){
  int* temp = new int[rows+1];
  for(int j =0;j<=rows;j++){
    temp[j] = ivector_length(A[j], columns);
  }
  return temp;
}

/*
 * insertion sort
 */
void insertion_sort(int** A, int n, int l, int r)
{ 
  int i;
  int* key;

  for (int j = l+1; j <= r; j++)
    {
      key = A[j];
      i = j - 1;

      while ((i >= l) && (ivector_length(A[i], n) > ivector_length(key, n)))
        {
	  A[i+1] = A[i];
	  i = i - 1;
	}

      A[i+1] = key;
    }
}

/*
*   TO IMPLEMENT: Improved Insertion Sort for problem 1.
*/


void insertion_sort_im(int** A, int n, int l, int r)
{ 
   int i;
  int* k1;
  int k2;
     int* Cursr = new int[r+1];
        for(int j =l;j<=r;j++){Cursr[j] = ivector_length(A[j], n);}

  for (int j = 1; j <= r; j++)
    {     
      k1 = A[j];
      k2 = Cursr[j];
      i = j - 1;
      while ((i >= 0) && (Cursr[i] > k2))
        {
              Cursr[i+1] = Cursr[i];
	            A[i+1] = A[i];
	            i = i - 1;
	      }

      A[i+1] = k1;
      Cursr[i+1] = k2;
    }
    delete[] Cursr;  
}


void merge(int** A, int* len, int p, int q, int r, int n)
{
    
    int n1 = q - p + 1;
    int n2 = r - q;

    int* L = new int[n1+1];
    int* R = new int[n2+1];
    int** LA = new int*[n1];
    for(int i = 0; i < n1; ++i){
      LA[i] = new int[n];
      L[i] = len[p + i];
      LA[i] = A[p + i];
    }
    int** RA = new int*[n2];
    for(int j = 0; j < n2; ++j){
      RA[j] = new int[n];
      R[j] = len[q + j + 1];

      RA[j] = A[q + j + 1];
    }


    L[n1] = std::numeric_limits<int>::max();
    R[n2] = std::numeric_limits<int>::max();
    int i = 0;
    int j = 0;
    for (int k = p; k <= r; k++) {
      if (L[i]<=R[j]) {
          len[k] = L[i];
        A[k] = LA[i];
        i = i + 1;
      }
      else {
          len[k] = R[j];
        A[k] = RA[j];
        j = j + 1;
      }
    }
  
    delete[] L;
    delete[] R;
}



void merge_sort_helper(int** A,int* len, int p, int r, int n)
{
  int q;
    
    
    if (p < r) 
    {
       
        
        q = floor((p + r) / 2);
	
		merge_sort_helper(A, len, p, q,n);
		merge_sort_helper(A, len, q + 1, r,n);

	
		merge(A,len, p, q,r,n);
		
    }
   
}

void merge_sort(int** A, int n, int p, int r){
  int* len = new int[r+1];
       for(int j =0;j<=r;j++)
       {
           len[j] = ivector_length(A[j], n);
           
       }
  merge_sort_helper(A,len,p,r,n);
  delete[] len;

}


bool check_sorted(int** A, int n, int l, int r)
{
  for (int i = l+1; i <= r; i++)
    if (ivector_length(A[i-1], n) > ivector_length(A[i], n))
      return false;
  return true;
}



/*
 * Simple function to check that our sorting algorithm did work
 * -> problem, if we find position, where the (i-1)-th element is 
 *    greater than the i-th element.
 */

/*
 * generate sorted/reverse/random arrays of type hw1type
 */
int** create_ivector(int n, int m)
{
  int** iv_array;

  iv_array = new int*[m];
  for (int i = 0; i < m; i++)
    iv_array[i] = new int[n];

  return iv_array;
}

void remove_ivector(int** iv_array, int m)
{
  for (int i = 0; i < m; i++)
    delete[] iv_array[i];
  delete[] iv_array;
}

int** create_sorted_ivector(int n, int m)
{
  int** iv_array;

  iv_array = create_ivector(n, m);
  for (int i = 0; i < m; i++)
    for (int j = 0; j < n; j++)
      iv_array[i][j] = (i+j)/n;

  return iv_array;
}

int** create_reverse_sorted_ivector(int n, int m)
{
  int** iv_array;

  iv_array = create_ivector(n, m);
  for (int i = 0; i < m; i++)
    for (int j = 0; j < n; j++)
      iv_array[i][j] = ((m-i)+j)/n;

  return iv_array;
}

int** create_random_ivector(int n, int m, bool small)
{
  random_generator rg;
  int** iv_array;

  iv_array = create_ivector(n, m);
  for (int i = 0; i < m; i++)
    for (int j = 0; j < n; j++)
      {
	rg >> iv_array[i][j];
	if (small)
	  iv_array[i][j] %= 100;
	else
	  iv_array[i][j] %= 65536;
      }

  return iv_array;
}



/*
*   TO IMPLEMENT: Improved Merge Sort for problem 2.
*/
//void merge_sort(int** A, int n, int p, int r)
//{

//}
// merge_sort(input_array, n, 0, m-1);








