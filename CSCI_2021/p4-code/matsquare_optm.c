// optimized versions of matrix diagonal summing
#include "matvec.h"

int matsquare_VER1(matrix_t mat, matrix_t matsq) {
  //This is the first opt suggested, goes per row instead of per segment
  for (int i = 0; i < mat.rows; i++) {
    for (int d = 0; d < mat.rows; d++) {
      MSET(matsq, i, d, 0);
    }
    for (int j = 0; j < mat.cols; j++) {
      int lead = MGET(mat, i, j);
      int k;
      //I do some loop unrolling here, unroll it by three
      for (k = 0; k < mat.rows-3; k += 3) {
        int targ0 = MGET(mat, j, k);
        int targ1 = MGET(mat, j, k+1);
        int targ2 = MGET(mat, j, k+2);
        int cur0 = MGET(matsq, i, k);
        int cur1 = MGET(matsq, i, k+1);
        int cur2 = MGET(matsq, i, k+2);
        int new0 = cur0 + lead*targ0;
        int new1 = cur1 + lead*targ1;
        int new2 = cur2 + lead*targ2;
        MSET(matsq, i, k, new0);
        MSET(matsq, i, k+1, new1);
        MSET(matsq, i, k+2, new2);
      }
      for (; k < mat.rows; k++) {
        int targ = MGET(mat, j, k);
        int cur = MGET(matsq, i, k);
        int new = cur + lead*targ;
        MSET(matsq, i, k, new);
      }
    }
  }
  return 0;
}


int matsquare_OPTM(matrix_t mat, matrix_t matsq) {
  if(mat.rows != mat.cols ||                       // must be a square matrix to square it
     mat.rows != matsq.rows || mat.cols != matsq.cols)
  {
    printf("matsquare_OPTM: dimension mismatch\n");
    return 1;
  }
  // Call to some version of optimized code
  return matsquare_VER1(mat, matsq);
}
