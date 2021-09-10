//Most is taken from HW2 list_main.c
//Things like general structure

#include <stdio.h>
#include <string.h>
#include "treemap.h"

int main(int argc, char *argv[]){
  int echo = 0;                                // controls echoing, 0: echo off, 1: echo on
  if(argc > 1 && strcmp("-echo",argv[1])==0) { // turn echoing on via -echo command line option
    echo=1;
  }

  printf("TreeMap Editor\n");
  printf("Commands:\n");
  printf("  quit:            exit the program\n");
  printf("  print:           shows contents of the tree in reverse sorted order\n");
  printf("  add <key> <val>: inserts the given key/val into the tree, duplicate keys are ignored\n");
  printf("  get <key>:       prints FOUND if the name is in the tree, NOT FOUND otherwise\n");
  printf("  clear:           eliminates all key/vals from the tree\n");
  printf("  preorder:        prints contents of the tree in pre-order which is how it will be saved\n");
  printf("  save <file>:     writes the contents of the tree in pre-order to the given file\n");
  printf("  load <file>:     clears the current tree and loads the one in the given file\n");
  
  char cmd[128];
  treemap_t list;
  int success;
  treemap_init(&list);

  while(1){
    printf("TM> ");                 // print prompt
    success = fscanf(stdin,"%s",cmd); // read a command
    if(success==EOF){                 // check for end of input
      printf("\n");                   // found end of input
      break;                          // break from loop
    }

    if( strcmp("quit", cmd)==0 ){     // check for exit command
      if(echo){
        printf("quit\n");
      }
      break;                          // break from loop
    }

    else if ( strcmp("get", cmd) == 0) {        // get command
        fscanf(stdin, "%s", cmd);               // uses treemap_get
        if (echo) {
            printf("get %s\n", cmd);
        }
        char *out;
        out = treemap_get(&list, cmd);
        if (out == NULL) {
            printf("NOT FOUND\n");
        }
        else {
            printf("FOUND: %s\n", out);
        }
    }

    else if( strcmp("save", cmd) == 0) {        // save command
        fscanf(stdin, "%s", cmd);               // uses treemap_save
        if (echo) {
            printf("save %s\n", cmd);
        }
        treemap_save(&list, cmd);
    }

    else if( strcmp("load", cmd) == 0) {        // load command
        fscanf(stdin, "%s", cmd);               // uses treemap_load
        if (echo) {
            printf("load %s\n", cmd);
        }
        int success = treemap_load(&list, cmd);
        if(!success) {
            printf("load failed\n");
        }
    }

    else if( strcmp("add", cmd) == 0) {     // add command
        char key[25], val[25];              // uses treemap_add
        fscanf(stdin, "%s %s", key, val);
        if (echo) {
            printf("add %s %s\n", key, val);
        }
        int ret = treemap_add(&list, key, val);
        if (!ret) {
            printf("modified existing key\n");
        }
    }

    else if( strcmp("clear", cmd)==0 ){   // clear command
      if(echo){                           // uses treemap_clear
        printf("clear\n");
      }
      treemap_clear(&list);
    }

    else if( strcmp("print", cmd)==0 ){   // print command
      if(echo){                           // uses treemap_print_revorder
        printf("print\n");
      }
      treemap_print_revorder(&list);
    }

    else if( strcmp("preorder", cmd)==0 ){   // preorder command
      if(echo){                              // uses treemap_print_preorder
        printf("preorder\n");
      }
      treemap_print_preorder(&list);
    }

    else{                                 // unknown command
      if(echo){
        printf("%s\n",cmd);
      }
      printf("unknown command %s\n",cmd);
    }
  }  

  // end main while loop
  treemap_clear(&list);                      // clean up the list
  return 0;
}