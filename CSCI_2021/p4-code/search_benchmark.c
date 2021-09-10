#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include "search.h"

// Complete this main to benchmark the search algorithms outlined in
// the project specification
int main(int argc, char *argv[]){
  int bin = 0, tre = 0, lin = 0, arr = 0;
  //If no letters provided, then it defaults to all true
  if (argc == 4) {
    bin = tre = lin = arr = 1;
  }
  //Otherwise, it checks each thing and determins if the input is there
  else if (argc == 5) {
    int len = strlen(argv[4]);
    for (int i = 0; i < len; i++) {
      if (argv[4][i] == 'a') {arr = 1;;}
      if (argv[4][i] == 'b') {bin = 1;;}
      if (argv[4][i] == 'l') {lin = 1;;}
      if (argv[4][i] == 't') {tre = 1;;}
    }
  }
  //If incorrect input, this shows up, just in case u dumb
  else { 
    printf("usage: ./search_benchmark <minpow> <maxpow> <repeats> [which]\n");
    printf("  which is a combination of:\n");
    printf("    a : Linear Array Search\n");
    printf("    l : Linked List Search\n");
    printf("    b : Binary Array Search\n");
    printf("    t : Binary Tree Search\n");
    printf("    (default all)\n");
    return 1;;
  }
  //Top bar
  printf("LENGTH SEARCHES");
  if (arr) { printf("%11s", "array"); }
  if (lin) { printf("%11s", "list"); }
  if (bin) { printf("%11s", "binary"); }
  if (tre) { printf("%11s", "tree"); }
  printf("\n");

  //Gets the inputs and whatnot
  int i = atoi(argv[1]);
  int final = atoi(argv[2]);
  int rep = atoi(argv[3]);
  int length = 1;
  //This is to get length up to the right value before starting the main loop
  for (int j = 0; j < i; j++) {
    length *= 2;
  }
  for (; i <= final; i++ ) {
    printf("%6d", length);
    printf("%9d", rep*length*2);
    clock_t begin, end;
    int term = (2*length);

    // The general form is gonna be the same for everything
    // Make a list or whatever, do the searching
    // Find the time
    // Print results, easy stuff
    if(arr) {
      int* garr = make_evens_array(length);
      begin = clock();
      for (int j = 0; j < rep; j++) {
        for (int k = 0; k < term; k++) {
          linear_array_search(garr, length, k);
        }
      }
      end = clock();
      double time = ((double) (end - begin)) / CLOCKS_PER_SEC;
      free(garr);
      printf("%11.4e", time);
    }

    if(lin) {
      list_t* garr = make_evens_list(length);
      begin = clock();
      for (int j = 0; j < rep; j++) {
        for (int k = 0; k < term; k++) {
          linkedlist_search(garr, length, k);
        }
      }
      end = clock();
      double time = ((double) (end - begin)) / CLOCKS_PER_SEC;
      list_free(garr);
      printf("%11.4e", time);
    }

    if(bin) {
      int* garr = make_evens_array(length);
      begin = clock();
      for (int j = 0; j < rep; j++) {
        for (int k = 0; k < term; k++) {
          binary_array_search(garr, length, k);
        }
      }
      end = clock();
      double time = ((double) (end - begin)) / CLOCKS_PER_SEC;
      free(garr);
      printf("%11.4e", time);
    }

    if(tre) {
      bst_t* garr = make_evens_tree(length);
      begin = clock();
      for (int j = 0; j < rep; j++) {
        for (int k = 0; k < term; k++) {
          binary_tree_search(garr, length, k);
        }
      }
      end = clock();
      double time = ((double) (end - begin)) / CLOCKS_PER_SEC;
      bst_free(garr);
      printf("%11.4e", time);
    }

    length *= 2;
    printf("\n");
  }
  return 0;
}
