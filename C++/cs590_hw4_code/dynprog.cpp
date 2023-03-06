#include <cstring>
#include <iostream>
#include <limits.h>

#include "dynprog.h"

using namespace std;


/*
 * Bottom up implementation of Smith-Waterman algorithm
 */
void SW_bottomUp(char* X, char* Y, char** P, int** H, int n, int m){
  int a1;
  int a2;
  int a3;

  int i = 0;
  while (i <= n) {
    int j = 0;
    while (j <= m) {
      if (i == 0 || j == 0) {
        P[i][j] = H[i][j] = 0;
      }
      j++;
    }
    i++;
  }

  for (int i = 1; i <= n; i++) {

    int j = 1;
    while (j <= m) {
      if (X[i - 1] == Y[j - 1]) {
        a1 = H[i - 1][j - 1] + 2;
      } else {
        a1 = H[i - 1][j - 1] - 1;
      }
      a2 = H[i - 1][j] - 1;
      a3 = H[i][j - 1] - 1;

      if (a1 >= a2 && a1 >= a3)
        H[i][j] = a1;
      else {
        if (a2 >= a1 && a2 >= a3)
          H[i][j] = a2;
        else
          H[i][j] = a3;
      }

      if (H[i][j] == a1) {
        P[i][j] = 'd';
      } else {
        if (H[i][j] == a2) {
          P[i][j] = 'u';
        } else {
          P[i][j] = 'l';
        }

      }
      j++;
    }

  }

  return;
}

/*
 * Top-down with memoization implementation of Smith-Waterman algorithm
 */
void memoized_SW(char* X, char* Y, char** P, int** H, int n, int m){
int i = 0;
  while (i <= n) {

    int j = 0;
    while (j <= m) {
      H[i][j] = 0;
      P[i][j] = '-';
      j++;
    }

    i++;
  }

  memoized_SW_AUX(X, Y, P, H, n, m);

  return;

}


/*
 * Auxilary recursive function of top-down with memoization implementation of Smith-Waterman algorithm
 */
int memoized_SW_AUX(char* X, char* Y, char** P, int** H, int n, int m){
	
int a1;
  int a2;
  int a3;
  if (m == 0 || n == 0)
    return 0;

  if (H[n][m] != 0) {
    return H[n][m];
  }
  if (X[n - 1] == Y[m - 1]) {
    a1 = memoized_SW_AUX(X, Y, P, H, n - 1, m - 1) + 2;
  } else {
    a1 = memoized_SW_AUX(X, Y, P, H, n - 1, m - 1) - 1;

  }
  a2 = memoized_SW_AUX(X, Y, P, H, n - 1, m) - 1;
  a3 = memoized_SW_AUX(X, Y, P, H, n, m - 1) - 1;

  if (a1 >= a2 && a1 >= a3) {
    H[n][m] = a1;
  } else {
    if (a2 >= a1 && a2 >= a3)
      H[n][m] = a2;
    else
      H[n][m] = a3;
  }

  if (H[n][m] == a1) {
    P[n][m] = 'd';
  } else {
    if (H[n][m] == a2) {
      P[n][m] = 'u';
    } else {
      P[n][m] = 'l';
    }

  }

  return H[n][m];
}

/*
 * Print X'
 */
void print_Seq_Align_X(char* X, char** P, int n, int m)
	
{
  if (m < 0 || n < 0) {
    return;
  }
  if (P[n][m] == 'd') {
    print_Seq_Align_X(X, P, n - 1, m - 1);
    cout << X[n - 1] << endl;
  }
  if (P[n][m] == 'l') {
    print_Seq_Align_X(X, P, n, m - 1);
    cout << "-" << endl;
  }
  if (P[n][m] == 'u') {
    print_Seq_Align_X(X, P, n - 1, m);
    cout << X[n - 1] << endl;
  }
}

/*
 * Print Y'
 */
void print_Seq_Align_Y(char* Y, char** P, int n, int m){
  if (m < 0 || n < 0) {
    return;
  }
  if (P[n][m] == 'd') {
    print_Seq_Align_Y(Y, P, n - 1, m - 1);
    cout << Y[m - 1] << endl;
  }
  if (P[n][m] == 'l') {
    print_Seq_Align_Y(Y, P, n, m - 1);
    cout << Y[m - 1] << endl;
  }
  if (P[n][m] == 'u') {
    print_Seq_Align_Y(Y, P, n - 1, m);
    cout << "-" << endl;
  }
}
